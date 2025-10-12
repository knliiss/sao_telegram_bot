package dev.knalis.sao_telegram_bot.service;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.impl.LocationMenuComposer;
import dev.knalis.sao_telegram_bot.composer.impl.MenuComposer;
import dev.knalis.sao_telegram_bot.composer.impl.ReminderMenuComposer;
import dev.knalis.sao_telegram_bot.composer.impl.UserMenuComposer;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MenuService {

    MenuComposer menuComposer;
    UserMenuComposer userMenuComposer;
//    LocationMenuComposer locationMenuComposer;
//    ReminderMenuComposer reminderMenuComposer;

    public SendMessage getMenu(ComposerContext context) {
        return menuComposer.compose(context);
    }

    public SendMessage getUserMenu(ComposerContext context) {
        return userMenuComposer.compose(context);
    }

    public SendMessage getLocationMenu(ComposerContext context) {
        // TODO implement location menu
        return null;
    }

    public SendMessage getReminderMenu(ComposerContext context) {
    return null;
    }
    
    public SendMessage getSettingsMenu(ComposerContext context) {
        return null;
    }
}
