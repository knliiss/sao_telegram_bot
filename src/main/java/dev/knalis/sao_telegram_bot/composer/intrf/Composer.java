package dev.knalis.sao_telegram_bot.composer;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface MessageComposer {

    default SendMessage compose(MessageContext context) {
        String chatId;
        try {
            chatId = context.get(ContextKey.CHAT_ID);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Missing CHAT_ID in context");
        }

        List<List<InlineKeyboardButton>> buttons = composeButtons(context);
        InlineKeyboardMarkup markup = null;
        if (buttons != null && !buttons.isEmpty()) {
            markup = new InlineKeyboardMarkup(buttons);
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(composeText(context))
                .replyMarkup(markup)
                .build();
    }

    String composeText(MessageContext context);

    List<List<InlineKeyboardButton>> composeButtons(MessageContext context);

}