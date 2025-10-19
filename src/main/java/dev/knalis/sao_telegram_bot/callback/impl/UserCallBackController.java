package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.service.ConsumerService;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import dev.knalis.sao_telegram_bot.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CallBackController("user/{chatId}")
public class UserCallBackController extends AbstractCallBackController {

    ConsumerService consumerService;
    MenuService menuService;
    UserService userService;

    public UserCallBackController(TelegramSenderService senderService, ConsumerService consumerService, MenuService menuService, UserService userService) {
        super(senderService);
        this.consumerService = consumerService;
        this.menuService = menuService;
        this.userService = userService;
    }

    @CallBackMethod
    private void generateMenu(@PathVariable("chatId") long chatId, CallBackInfo callBackInfo) {
        var context = new ComposerContext(String.valueOf(chatId));
        var message = menuService.getUserMenu(context);
        editMessage(callBackInfo.getUser().getId(), callBackInfo.getMessageId(), message);
    }


    // ------------ Location ------------
    @CallBackMethod("/location")
    private void generateLocationMenu(@PathVariable("chatId") long chatId, CallBackInfo callBackInfo) {
        var context = new ComposerContext(String.valueOf(chatId));
        var message = menuService.getLocationMenu(context);
        editMessage(callBackInfo.getUser().getId(), callBackInfo.getMessageId(), message);
    }

    @CallBackMethod("/location/set/{locationId}")
    private void setLocation(@PathVariable("chatId") long chatId,
                             @PathVariable("locationId") int locationId,
                             CallBackInfo callBackInfo) {
        var localeChatId = callBackInfo.getUser().getId();
        try {
            userService.setUserLocation(chatId, (short) locationId);
            sendMessage(localeChatId, "✅ Локация обновлена.");
        } catch (Exception e) {
            sendMessage(localeChatId, "❌ Смена локации временно недоступна.");
        }
        var context = new ComposerContext(String.valueOf(chatId));
        var message = menuService.getUserMenu(context);
        editMessage(localeChatId, callBackInfo.getMessageId(), message);
    }
    
    // ------------ Additional accounts menu ------------

    @CallBackMethod("/account")
    private void openAccountsMenu(@PathVariable("chatId") long chatId, CallBackInfo info) {
        var context = new ComposerContext(chatId);
        context.put(ContextKey.PAGE, "1");
        var message = menuService.getAdditionalAccountsMenu(context);
        editMessage(info.getUser().getId(), info.getMessageId(), message);
    }

    @CallBackMethod("/account/{page}")
    private void openAccountsPage(@PathVariable("chatId") long chatId, @PathVariable("page") int page, CallBackInfo info) {
        var context = new ComposerContext(chatId);
        context.put(ContextKey.PAGE, Integer.toString(Math.max(1, page)));
        var message = menuService.getAdditionalAccountsMenu(context);
        editMessage(info.getUser().getId(), info.getMessageId(), message);
    }

    @CallBackMethod("/account/link/main")
    private void linkMain(@PathVariable("chatId") long chatId, CallBackInfo info) {
        var localeChatId = info.getUser().getId();
        int messageId = info.getMessageId();
        int tempMessageId = sendMessage(localeChatId, "✏️ Введите новый никнейм. Для отмены используйте /cancel.");
        
        Consumer<String> consumer = input -> {
            
            if (!userService.isNickNameValid(input)) {
                sendMessage(localeChatId, "⚠️ Никнейм должен быть от 3 до 20 символов и содержать только буквы, цифры и символы подчеркивания. Попробуйте снова или используйте /cancel для отмены.");
                return;
            }
            
            if (userService.isAccountNameAvailable(input)) {
                sendMessage(localeChatId, "⚠️ Этот никнейм уже занят. Пожалуйста, выберите другой или используйте /cancel для отмены.");
                return;
            }
            
            userService.setUserNickName(chatId, input);
            sendMessage(localeChatId, "✅ Никнейм успешно изменен на: " + input);
            
            var context = new ComposerContext(String.valueOf(chatId));
            var message = menuService.getUserMenu(context);
            editMessage(localeChatId, messageId, message);
            deleteMessage(localeChatId, tempMessageId);
        };
        
        consumerService.addConsumer(localeChatId, consumer);
    }

    @CallBackMethod("/account/link/additional")
    private void linkAdditional(@PathVariable("chatId") long chatId, CallBackInfo info) {
        long userId = info.getUser().getId();
        int originalMessageId = info.getMessageId();
        int promptId = sendMessage(userId, "➕ Введите ник дополнительного аккаунта. Для отмены /cancel.");
        consumerService.addConsumer(userId, input -> {
            if (!userService.isNickNameValid(input)) {
                sendMessage(userId, "⚠️ Некорректный ник. Разрешены символы a-z, A-Z, 0-9 и _. Попробуйте снова.");
                return;
            }
            try {
                userService.linkAdditionalAccount(chatId, input);
                sendMessage(userId, "✅ Дополнительный аккаунт привязан: @" + (input.startsWith("@") ? input.substring(1) : input));
            } catch (Exception e) {
                sendMessage(userId, "❌ Не удалось привязать доп. аккаунт: " + e.getMessage());
            }
            var ctx = new ComposerContext(chatId);
            ctx.put(ContextKey.PAGE, "1");
            var msg = menuService.getAdditionalAccountsMenu(ctx);
            editMessage(userId, originalMessageId, msg);
            deleteMessage(userId, promptId);
        });
    }

    @CallBackMethod("/account/unlink/{username}/{page}")
    private void unlinkAdditional(@PathVariable("chatId") long chatId, @PathVariable("username") String username, @PathVariable("page") int page, CallBackInfo info) {
        long userId = info.getUser().getId();
        try {
            userService.unlinkAdditionalAccount(chatId, username);
            sendMessage(userId, "✅ Аккаунт отвязан: @" + username);
        } catch (Exception e) {
            sendMessage(userId, "❌ Не удалось отвязать аккаунт: " + e.getMessage());
        }
        var ctx = new ComposerContext(chatId);
        ctx.put(ContextKey.PAGE, Integer.toString(Math.max(1, page)));
        var msg = menuService.getAdditionalAccountsMenu(ctx);
        editMessage(userId, info.getMessageId(), msg);
    }
}
