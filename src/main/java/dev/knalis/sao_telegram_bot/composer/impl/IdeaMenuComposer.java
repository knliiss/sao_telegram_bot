package dev.knalis.sao_telegram_bot.composer.impl;

import dev.knalis.sao_telegram_bot.composer.ComposerContext;
import dev.knalis.sao_telegram_bot.composer.ContextKey;
import dev.knalis.sao_telegram_bot.composer.intrf.PageComposer;
import dev.knalis.sao_telegram_bot.composer.intrf.ListableComposer;
import dev.knalis.sao_telegram_bot.composer.intrf.BackComposer;
import dev.knalis.sao_telegram_bot.client.dto.IdeaDTO;
import dev.knalis.sao_telegram_bot.model.button.Button;
import dev.knalis.sao_telegram_bot.service.IdeaService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class IdeaMenuComposer implements PageComposer, ListableComposer<IdeaDTO>, BackComposer {
    private final IdeaService ideaService;
    private static final int PAGE_SIZE = 5;

    @Override
    public String composeText(ComposerContext context) {
        String pageStr = context.getOrDefault(ContextKey.PAGE.toString(), "1");
        int page = Integer.parseInt(pageStr);
        long userId = Long.parseLong(context.get(ContextKey.CHAT_ID));
        int total = ideaService.getIdeasByUserId(userId).size();
        int totalPage = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        return "<b>💡 Идеи</b>\n\nСтраница: " + page + "/" + totalPage + "\nВыберите идею, чтобы удалить её.";
    }

    @Override
    public List<List<InlineKeyboardButton>> composeButtons(ComposerContext context) {
        String pageStr = context.getOrDefault(ContextKey.PAGE.toString(), "1");
        int page = Integer.parseInt(pageStr);
        long userId = Long.parseLong(context.get(ContextKey.CHAT_ID));

        List<IdeaDTO> all = ideaService.getIdeasByUserId(userId);
        int total = all.size();
        int totalPage = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));

        int from = Math.max(0, (page - 1) * PAGE_SIZE);
        int to = Math.min(total, from + PAGE_SIZE);
        List<IdeaDTO> ideas = from < to ? all.subList(from, to) : List.of();

        Function<IdeaDTO, String> callbackMapper = idea -> "idea/delete/" + idea.getId() + "/" + page;
        Function<IdeaDTO, String> textMapper = idea -> "🗑 " + (idea.getContent().length() > 30 ? idea.getContent().substring(0, 30) + "…" : idea.getContent());
        List<List<InlineKeyboardButton>> rows = new ArrayList<>(buildListOfTypeButtons(ideas, 1, callbackMapper, textMapper));

        // footer pagination
        rows.add(generateFooter("idea/", page, totalPage));
        // back
        rows.add(generateBackButton(context, "message/menu"));
        return rows;
    }
}
