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
                - 👤 Аккаунт: Просмотр и управление вашим аккаунтом.
                - 🛒 Магазин: Покупка дополнительных функций и пакетов сообщений.
                - 💡 Идеи: Оставить свою идеи для бота и посмотреть статус оставленных ранее идей.
                - ⚙️ Настройки: Настройка параметров бота под ваши предпочтения.
                
                Нажмите на соответствующую кнопку, чтобы перейти к выбранному разделу.
                Если у вас возникнут вопросы, не стесняйтесь обращаться <a href="https://t.me/Statleykill">ко мне</a>.

        """;
    }


    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        var chatId = context.get(ContextKey.CHAT_ID);
        return KeyboardUtil.formCallbackButtons(generateBackButton("message/delete"),
                List.of(Button.builder().callbackData("user/" + chatId).text("👤 Аккаунт").build().toInlineButton()),
                List.of(Button.builder().callbackData("message/test/testmessage").text("test").build().toInlineButton())
        );
    }
}
