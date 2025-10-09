package dev.knalis.sao_telegram_bot.composer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MessageContext {
    Map<String, String> variables = new ConcurrentHashMap<>();

    public MessageContext(String chatId) {
        put(ContextKey.CHAT_ID, chatId);
    }

    public void put(String key, String value) throws IllegalArgumentException {
        variables.put(key, value);
    }

    public void put(ContextKey key, String value) throws IllegalArgumentException {
        put(key.toString(), value);
    }

    public String get(String key) {
        String value = variables.get(key);
        if (value == null)
            throw new IllegalArgumentException("Variable '%s' not found".formatted(key));
        return value;
    }

    public String get(ContextKey key) {
        return get(key.toString());
    }
}
