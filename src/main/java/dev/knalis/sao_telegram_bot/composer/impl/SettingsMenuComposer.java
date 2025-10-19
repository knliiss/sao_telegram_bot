package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.ListableComposer;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.client.dto.SettingsDTO;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.service.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SettingsMenuComposer implements ListableComposer<String>, BackComposer {
    private final SettingsService settingsService;

    @Override
    public String composeText(ComposerContext context) {
        String category = context.getOrDefault("category", "OTHER");
        return "<b>⚙️ Настройки</b>\n\nКатегория: " + category + "\nНажмите, чтобы переключить.";
    }

    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        long chatId = Long.parseLong(context.get(ContextKey.CHAT_ID));
        String category = context.getOrDefault("category", "OTHER");
        SettingsDTO settings = settingsService.getSettings(chatId, category);
        Map<String, Boolean> map = settings.getNotificationSettings();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        for (var entry : map.entrySet()) {
            String key = entry.getKey();
            boolean value = Boolean.TRUE.equals(entry.getValue());
            String label = (value ? "✅ " : "❌ ") + key;
            rows.add(List.of(Button.builder()
                    .text(label)
                    .callbackData("settings/" + chatId + "/update/" + category + "/one/" + key)
                    .build().toInlineButton()));
        }

        rows.add(List.of(
                Button.builder().text("Включить все").callbackData("settings/" + chatId + "/update/" + category + "/all/true").build().toInlineButton(),
                Button.builder().text("Выключить все").callbackData("settings/" + chatId + "/update/" + category + "/all/false").build().toInlineButton()
        ));

        rows.add(generateBackButton(context, "message/menu"));
        return rows;
    }
}
