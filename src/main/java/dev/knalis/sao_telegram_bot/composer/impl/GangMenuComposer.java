package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.service.GangService;
import dev.knalis.sao_telegram_bot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GangMenuComposer implements BackComposer {
    private final GangService gangService;
    private final UserService userService;
    
    @Override
    public String composeText(ComposerContext context) {
        var sb = new StringBuilder();
        sb.append("<b>👥 Банды</b>").append("\n\n");
        var strChatId = context.get(ContextKey.CHAT_ID);
        var user = userService.getUser(Long.parseLong(strChatId));
        var gangId = user.getGangId();
        if (gangId == null) {
            sb.append("Вы не состоите в банде.").append("\n");
            sb.append("Создайте свою банду или попросите кого то пригласить вас.");
        } else {
            var gang = gangService.getGang(gangId);
            sb.append("Вы состоите в банде: <b>").append(gang.getName()).append("</b>").append("\n");
            sb.append("Ваша роль: <b>").append(gang.getOwner().getId().equals(user.getId()) ? "Владелец" : "Участник").append("</b>").append("\n\n");
            sb.append("Участники банды:").append("\n");
            gang.getMembers().forEach(member -> {
                sb.append("- ").append(member.getUsername() != null ? member.getUsername() : "Пользователь " + member.getId()).append("\n");
            });
        }
        return sb.toString();
    }
    
    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        var chatId = context.get(ContextKey.CHAT_ID);
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        var user = userService.getUser(Long.parseLong(chatId));
        var gangId = user.getGangId();
        if (gangId == null) {
            buttons.add(List.of(
                    Button.builder().callbackData("gang/create").text("Создать банду - 250💰").build().toInlineButton()
            ));
        } else {
            var gang = gangService.getGang(gangId);
            var members = gang.getMembers();
            if (gang.getOwner().getId().equals(user.getId())) {
                for (var member : members) {
                    if (!member.getId().equals(user.getId())) {
                        buttons.add(List.of(
                                Button.builder().callbackData("gang/transfer/" + member.getId()).text("Передать право собственности " + (member.getUsername() != null ? member.getUsername() : "Пользователю " + member.getId())).build().toInlineButton(),
                                Button.builder().callbackData("gang/kick/" + member.getId()).text("Исключить " + (member.getUsername() != null ? member.getUsername() : "Пользователя " + member.getId())).build().toInlineButton()
                        ));
                    }
                }
            }
            buttons.add(List.of(
                    Button.builder().callbackData("gang/leave").text("Покинуть банду").build().toInlineButton()
            ));
        }
        
        buttons.add(generateBackButton(context, "message/menu"));
        return buttons;
    }
}
