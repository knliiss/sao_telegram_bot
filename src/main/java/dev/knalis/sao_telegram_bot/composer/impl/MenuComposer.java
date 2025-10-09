package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.util.KeyboardUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

@Component
public class MenuComposer implements BackComposer {

    @Override
    public String composeText(ComposerContext context) {
        return """
                <b>📋 Главное меню</b>
                
                Выберите один из пунктов ниже, чтобы продолжить:
                - 👤 <b>Аккаунт</b>: Просмотр и управление вашим аккаунтом.
                - 🛒 <b>Магазин</b>: Покупка дополнительных функций и пакетов сообщений.
                - 💡 <b>Идеи</b>: Оставить свою идею и проверить статус ранее отправленных.
                - ⚙️ <b>Настройки</b>: Персонализация параметров.
                
                Если возникнут вопросы — пишите @Statleykill
                """;
    }


    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        var chatId = context.get(ContextKey.CHAT_ID);
        return KeyboardUtil.formCallbackButtons(generateBackButton("message/delete"),
                List.of(Button.builder().callbackData("user/" + chatId).text("👤 Аккаунт").build().toInlineButton()),
                List.of(Button.builder().callbackData("shop/" + chatId).text("🛒 Магазин").build().toInlineButton()),
                List.of(Button.builder().callbackData("idea/" + chatId).text("💡 Идеи").build().toInlineButton()),
                List.of(Button.builder().callbackData("settings/" + chatId).text("⚙️ Настройки").build().toInlineButton())
        );
    }
}
