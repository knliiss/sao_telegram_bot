package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@CallBackController("config/{userId}")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigCallBackController {

    @CallBackMethod("activate/{configId}")
    public void activate(
            @PathVariable("userId") long userId,
            @PathVariable("configId") long configId) {

    }

}

