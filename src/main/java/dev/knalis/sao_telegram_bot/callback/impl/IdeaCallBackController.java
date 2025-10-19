package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.CallBackInfo;
import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.service.IdeaService;
import dev.knalis.sao_telegram_bot.service.MenuService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@CallBackController("idea")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdeaCallBackController extends AbstractCallBackController {
    
    MenuService menuService;
    IdeaService ideaService;
    
    public IdeaCallBackController(TelegramSenderService senderService, MenuService menuService, IdeaService ideaService) {
        super(senderService);
        this.menuService = menuService;
        this.ideaService = ideaService;
    }
    
    @CallBackMethod("/{page}")
    public void getMenu(@PathVariable("page") String page, CallBackInfo info) {
        var chatId = info.getUser().getId();
        var messageId = info.getMessageId();
        var context = new ComposerContext(chatId);
        context.put(ContextKey.PAGE, page);
        context.put(ContextKey.BACK_CALLBACK_URL, "message/menu");
        var message = menuService.getIdeaMenu(context);
        editMessage(chatId, messageId, message);
    }
    
    @CallBackMethod("/delete/{ideaId}/{page}")
    public void delete(@PathVariable("page") String page , @PathVariable("ideaId") String ideaId , CallBackInfo info) {
        var chatId = info.getUser().getId();
        var messageId = info.getMessageId();
        var context = new ComposerContext(chatId);
        ideaService.deleteIdea(ideaId);
        context.put(ContextKey.PAGE, page);
        context.put(ContextKey.BACK_CALLBACK_URL, "message/menu");
        var message = menuService.getIdeaMenu(context);
        editMessage(chatId, messageId, message);
    }
    
}
