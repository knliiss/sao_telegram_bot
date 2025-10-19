package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.service.MessagePackService;
import dev.knalis.sao_telegram_bot.service.SettingsService;
import dev.knalis.sao_telegram_bot.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static dev.knalis.sao_telegram_bot.util.KeyboardUtil.formCallbackButtons;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMenuComposer implements BackComposer {
    
    UserService userService;
    MessagePackService messagePackService;
    SettingsService settingsService;

    @Override
    public String composeText(ComposerContext context) {
        String chatIdStr = context.get(ContextKey.CHAT_ID);
        Long chatId = Long.valueOf(chatIdStr);
        
        var user = userService.getUser(chatId);
        if (user == null) return "Пользователь не найден";

        String currentPackName = "не выбран";
        try {
            var settings = settingsService.getSettings(chatId, "OTHER");
            if (settings != null && settings.getMessagePackId() != null) {
                var pack = messagePackService.getPackById(settings.getMessagePackId());
                if (pack != null) currentPackName = pack.getName();
            }
        } catch (Exception ignored) {}

        StringBuilder builder = new StringBuilder();
        builder.append("<b>👤 Информация о пользователе</b>\n\n")
                .append("<b>ID:</b> <code>").append(user.getId()).append("</code>\n")
                .append("<b>Имя пользователя:</b> @").append(user.getUsername() != null ? user.getUsername() : "не задано").append("\n")
                .append("<b>Ник:</b> ").append(user.getNickname() != null ? user.getNickname() : "не задан").append("\n")
                .append("<b>Баланс:</b> ").append(user.getBalance() != null ? user.getBalance() : 0.0).append(" 💰\n")
                .append("<b>Локация:</b> ").append(user.getLocation()).append("\n")
                .append("<b>Выбранный пак сообщений:</b> ").append(currentPackName).append("\n\n");

        if (user.getSubscription() != null) {
            builder.append("<b>Подписка:</b> ")
                    .append(user.getSubscription().getPlan())
                    .append(" (до ")
                    .append(user.getSubscription().getEndDate() == null ? "" : user.getSubscription().getEndDate())
                    .append(")\n\n");
        } else {
            builder.append("<b>Подписка:</b> отсутствует\n");
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            builder.append("<b>Роли:</b> ").append(String.join(", ", user.getRoles())).append("\n");
        }

        if (user.getAdditionalAccounts() != null && !user.getAdditionalAccounts().isEmpty()) {
            builder.append("<b>Дополнительные аккаунты:</b> ")
                    .append(String.join(", ", user.getAdditionalAccounts()))
                    .append("\n");
        }

        return builder.toString();
    }


    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        String chatIdStr = context.get(ContextKey.CHAT_ID);
        return formCallbackButtons(
                List.of(
                        Button.builder().callbackData("user/" + chatIdStr + "/location").text("📍Изменить локацию").build().toInlineButton()
                ),
                List.of(
                    Button.builder().callbackData("reminder/" + chatIdStr).text("🔔 Напоминания").build().toInlineButton()
                ),
                List.of(
                        Button.builder().callbackData("user/" + chatIdStr + "/account").text("⚙️ Управление аккаунтами").build().toInlineButton()
                ),
                generateBackButton(context, "message/menu")
        );
    }
}
