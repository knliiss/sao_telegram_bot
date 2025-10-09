package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.client.api.UserMessagePacksApiClient;
import dev.knalis.sao_telegram_bot.client.api.UsersApiClient;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.model.button.Button;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

import static dev.knalis.sao_telegram_bot.util.KeyboardUtil.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMenuComposer implements BackComposer {

    UsersApiClient userClient;
    UserMessagePacksApiClient userMessagePacksClient;

    @Override
    public String composeText(ComposerContext context) {
        String chatIdStr = context.get(ContextKey.CHAT_ID);
        Long chatId = Long.valueOf(chatIdStr);
        var messagePack = userMessagePacksClient.getUserMessagePacks(chatId, true).getFirst();
        var user = userClient.getUser(chatId);

        if (user == null) {
            return "_Пользователь не найден_";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("*👤 Информация о пользователе*\n\n");
        builder.append("*ID:* `").append(user.getId()).append("`\n");
        builder.append("*Имя пользователя:* @").append(user.getUsername() != null ? user.getUsername() : "не задано").append("\n");
        builder.append("*Ник:* ").append(user.getNickname() != null ? user.getNickname() : "не задан").append("\n");
        builder.append("*Баланс:* ").append(user.getBalance() != null ? user.getBalance() : 0.0).append(" 💰\n");
        builder.append("*Локация:* ").append(user.getLocation()).append("\n");
        builder.append("*Выбранный пак сообщений:* ").append(messagePack.getName()).append("\n");

        if (user.getSubscription() != null) {
            builder.append("*Подписка:* ").append(user.getSubscription().getPlan())
                    .append(" (до ").append(user.getSubscription().getEndDate()).append(")\n");
        } else {
            builder.append("*Подписка:* отсутствует\n");
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            builder.append("*Роли:* ").append(String.join(", ", user.getRoles())).append("\n");
        }

        if (user.getAdditionalAccounts() != null && !user.getAdditionalAccounts().isEmpty()) {
            builder.append("*Дополнительные аккаунты:* ")
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
                        Button.builder().callbackData("user/" + chatIdStr + "/notification").text("🔔 Уведомления").build().toInlineButton()
                ),
                List.of(
                        Button.builder().callbackData("user/" + chatIdStr + "/account").text("⚙️ Управление аккаунтами").build().toInlineButton()
                )


        );
    }
}
