package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.intrf.FullComposer;
import dev.knalis.sao_telegram_bot.composer.intrf.ListableComposer;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class LocationMenuComposer implements FullComposer, ListableComposer<Integer> {
    @Override
    public String composeText(ComposerContext context) {
        return "<b> 🗺️ Меню локаций</b>\n\n" +
                "Здесь вы можете выбрать или изменить вашу текущую локацию. " +
                "Выбор правильной локации поможет персонализировать ваш опыт и предоставить релевантную информацию.";
    }

    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        return List.of();
    }
}
