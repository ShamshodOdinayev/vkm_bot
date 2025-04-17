package com.example.demo;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {

    private final MyTelegramBot bot;

    public WebhookController(MyTelegramBot bot) {
        this.bot = bot;
    }

    @PostMapping("/webhook")
    public void onUpdateReceived(@RequestBody Update update) {
        bot.onUpdateReceived(update);
    }
}
