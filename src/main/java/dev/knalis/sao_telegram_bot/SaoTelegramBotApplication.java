package dev.knalis.sao_telegram_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SaoTelegramBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaoTelegramBotApplication.class, args);
    }

}
