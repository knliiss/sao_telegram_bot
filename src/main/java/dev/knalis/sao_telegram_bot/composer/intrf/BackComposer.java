package dev.knalis.sao_telegram_bot.composer.intrf;

import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.util.KeyboardUtil;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface BackComposer extends Composer {

    default List<InlineKeyboardButton> generateBackButton(String backCallback) {
        return KeyboardUtil.formCallBackButtonsRow(Button.builder().text("🔙 Назад").callbackData(backCallback).build());
    }
}
