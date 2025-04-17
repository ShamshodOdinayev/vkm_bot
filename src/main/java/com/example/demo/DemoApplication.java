package com.example.demo;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@SpringBootApplication
public class DemoApplication {

    private MyTelegramBot myTelegramBot;

    public void DemoApplication(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    public DemoApplication(MyTelegramBot myTelegramBot) {
        this.myTelegramBot = myTelegramBot;
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(myTelegramBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

