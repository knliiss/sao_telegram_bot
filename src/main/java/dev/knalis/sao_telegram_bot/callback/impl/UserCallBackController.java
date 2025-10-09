package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
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
                             @PathVariable("locationId") short locationId,
                             CallBackInfo callBackInfo) {
        userService.setUserLocation(chatId, locationId);
        var context = new ComposerContext(String.valueOf(chatId));
        var message = menuService.getUserMenu(context);
        editMessage(callBackInfo.getUser().getId(), callBackInfo.getMessageId(), message);
    }

    // ------------ NickName ------------

    @CallBackMethod("/nickname/set")
    private void setAccountName(@PathVariable("chatId") long chatId, CallBackInfo callBackInfo) {
        var localeChatId = callBackInfo.getUser().getId();
        int messageId = callBackInfo.getMessageId();
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
}
