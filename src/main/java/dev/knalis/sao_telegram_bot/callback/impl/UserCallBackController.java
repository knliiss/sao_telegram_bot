package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import dev.knalis.sao_telegram_bot.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CallBackController("user/{chatId}")
public class UserCallBackController extends AbstractCallBackController {

    MenuService menuService;
    UserService userService;

    public UserCallBackController(TelegramSenderService senderService, MenuService menuService, UserService userService) {
        super(senderService);
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

    // ------------ Account Name ------------

    @CallBackMethod("/account/setname")
    private void setAccountName(@PathVariable("chatId") long chatId, CallBackInfo callBackInfo) {

    }
}
