package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.service.MessagePackService;
import dev.knalis.sao_telegram_bot.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessagePackDetailsComposer implements BackComposer {

    MessagePackService messagePackService;
    private final UserService userService;
    
    @Override
    public String composeText(ComposerContext context) {
        long userId = Long.parseLong(context.get(ContextKey.CHAT_ID));
        String packId = context.get("messagePackId");
        var pack = messagePackService.getPackById(packId);
        var user = userService.getUser(userId);
        boolean owned = user.getOwnedMessagePacksIds().contains(packId);
        

        StringBuilder sb = new StringBuilder();
        sb.append("<b>").append(pack.getEmoji() != null ? pack.getEmoji() + " " : "").append(pack.getName()).append("</b>\n\n");
        sb.append("Стоимость: ").append(pack.getCost() != null ? pack.getCost() : "-").append(" 💰\n");
        sb.append("Редкость: ").append(pack.getRarity() != null ? pack.getRarity() : "-").append("\n\n");
        sb.append("Сообщений: ").append(pack.getMessages() != null ? pack.getMessages().size() : 0).append("\n");
        if (owned) sb.append("\n✅ У вас уже куплен этот пакет.");
        return sb.toString();
    }

    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        long userId = Long.parseLong(context.get(ContextKey.CHAT_ID));
        String packId = context.get("messagePackId");
        String backPage = context.getOrDefault(ContextKey.PAGE.toString(), "1");
        
        var user = userService.getUser(userId);
        boolean owned = user.getOwnedMessagePacksIds().contains(packId);
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        if (!owned) {
            rows.add(List.of(Button.builder()
                    .text("Купить")
                    .callbackData("messagepack/" + packId + "/buy/" + backPage)
                    .build().toInlineButton()));
        }

        rows.add(List.of(Button.builder()
                .text("⬅️ Назад к списку")
                .callbackData("messagepack/" + backPage)
                .build().toInlineButton()));

        rows.add(List.of(Button.builder().text("🏠 Меню").callbackData("message/menu").build().toInlineButton()));
        return rows;
    }
}

