package dev.knalis.sao_telegram_bot.service;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReminderService {
    public List<String> getReminders(Long userId) {
        return List.of("Встреча", "Тренировка", "Позвонить");
    }
}

