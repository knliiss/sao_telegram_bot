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
                *🏠 Главное меню*
                
                Выберите действие ниже, чтобы продолжить:
                
                *📚 Задачи*
                — Просмотреть активные  
                — Создать новую  
                
                *🧭 Навигация*
                — Мой профиль  
                — Настройки  
                
                *ℹ️ Дополнительно*
                — Помощь  
                — О боте
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
