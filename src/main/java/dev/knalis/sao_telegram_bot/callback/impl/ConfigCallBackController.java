package dev.knalis.sao_telegram_bot.callback.impl;

import dev.knalis.sao_telegram_bot.callback.annotation.AbstractCallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackController;
import dev.knalis.sao_telegram_bot.callback.annotation.CallBackMethod;
import dev.knalis.sao_telegram_bot.callback.annotation.PathVariable;
import dev.knalis.sao_telegram_bot.service.ConfigService;
import dev.knalis.sao_telegram_bot.service.TelegramSenderService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;


@CallBackController("config/{userId}")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigCallBackController extends AbstractCallBackController {
    
    ConfigService configService;
    
    public ConfigCallBackController(TelegramSenderService senderService, ConfigService configService) {
        super(senderService);
        this.configService = configService;
    }
    
    @CallBackMethod("/activate/{configId}")
    public void activate(
            @PathVariable("userId") long userId,
            @PathVariable("configId") long configId) {
        configService.activateConfig(userId, configId);
    }
    
    
}

