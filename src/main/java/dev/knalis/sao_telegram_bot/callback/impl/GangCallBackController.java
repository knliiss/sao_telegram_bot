package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.service.ConsumerService;
import dev.knalis.sao_telegram_bot.service.GangService;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@CallBackController("gang")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GangCallBackController extends AbstractCallBackController {
    
    MenuService menuService;
    GangService gangService;
    ConsumerService consumerService;
    
    public GangCallBackController(TelegramSenderService senderService, MenuService menuService, GangService gangService, ConsumerService consumerService) {
        super(senderService);
        this.menuService = menuService;
        this.gangService = gangService;
        this.consumerService = consumerService;
    }

    @CallBackMethod
    public void openMenu(CallBackInfo info) {
        var userId = info.getUser().getId();
        var messageId = info.getMessageId();
        var context = new ComposerContext(userId);
        var message = menuService.getGangMenu(context);
        editMessage(userId, messageId, message);
    }
    
    @CallBackMethod("/kick/{targetId}")
    public void kickMember(@PathVariable("targetId") long targetId, CallBackInfo info) {
        var messageId = info.getMessageId();
        var userId = info.getUser().getId();
        var gangId = gangService.getGangByUserId(userId).getId();
        var context = new ComposerContext(userId);
        gangService.kickMember(userId, gangId, targetId);
        var message = menuService.getGangMenu(context);
        editMessage(userId, messageId, message);
    }
    
    @CallBackMethod("/leave")
    public void leaveGang(CallBackInfo info) {
        var userId = info.getUser().getId();
        var messageId = info.getMessageId();
        var context = new ComposerContext(userId);
        
        gangService.leaveGang(userId);
        var message = menuService.getAllGangMenu(context);
        editMessage(userId, messageId, message);
    }
    
    @CallBackMethod("/join/{gangId}")
    public void joinGang(@PathVariable("gangId") long gangId, CallBackInfo info) {
        var messageId = info.getMessageId();
        var userId = info.getUser().getId();
        var context = new ComposerContext(userId);
        gangService.joinGang(userId, gangId);
        var message = menuService.getGangMenu(context);
        editMessage(userId, messageId, message);
    }
    
    @CallBackMethod("/create")
    public void createGang(CallBackInfo info) {
        var userId = info.getUser().getId();
        var messageId = info.getMessageId();
        var context = new ComposerContext(userId);
        consumerService.addConsumer(userId, input -> {
            if (input.length() < 3 || input.length() > 20) {
                sendMessage(userId, "⚠️ Название банды должно быть от 3 до 20 символов.");
                return;
            }
            if (gangService.isGangNameAvailable(input)) {
                gangService.createGang(userId, input);
                var message = menuService.getGangMenu(context);
                editMessage(userId, messageId, message);
            } else {
                sendMessage(userId, "⚠️ Такое название уже занято. Пожалуйста, выберите другое.");
            }
        });
    }
    
    @CallBackMethod("/transfer/{newOwnerId}")
    public void transferOwnership(@PathVariable("newOwnerId") long newOwnerId, CallBackInfo info) {
        var messageId = info.getMessageId();
        var userId = info.getUser().getId();
        var context = new ComposerContext(userId);
        gangService.transferOwnership(userId, newOwnerId);
        var message = menuService.getGangMenu(context);
        editMessage(userId, messageId, message);
    }
    
}
