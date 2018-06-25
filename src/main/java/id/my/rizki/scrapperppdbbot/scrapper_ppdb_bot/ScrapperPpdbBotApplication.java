package id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot;

import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.component.BotPolling;
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.entity.RegistrarInformation;
import id.my.rizki.scrapperppdbbot.scrapper_ppdb_bot.service.Scrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ScrapperPpdbBotApplication {

    @Autowired
    private static Scrap scrap;

    static Logger logger = LoggerFactory.getLogger(ScrapperPpdbBotApplication.class);

    public static HashMap<Long, ArrayList<RegistrarInformation>> subscriber = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // TODO Menambahkan fungsi subscribe dan pesan otomatis terjadwal ketika ada perubahan data pendaftar
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                subscriber.forEach((key, value) -> {
                    value.stream().forEach(val -> {
                        RegistrarInformation registrarInformation = scrap.getSchoolStatistic(val.getSchool());
                        if (!registrarInformation.toString().equalsIgnoreCase(val.toString())) {
                            SendMessage response = new SendMessage();
                            Long chatId = key;
                            response.setChatId(chatId);
                            response.setText(registrarInformation.toString());
                            BotPolling bp = new BotPolling();
                            try {
                                bp.sendMessage(response);
                                logger.info("Sent message \"{}\" to {}", "update", chatId);
                            } catch (TelegramApiException e) {
                                logger.error("Failed to send message \"{}\" to {} due to error: {}", "fail update", chatId, e.getMessage());
                            }
                        }
                    });
                });
            }
        }, 0, 5, TimeUnit.SECONDS);
        SpringApplication.run(ScrapperPpdbBotApplication.class, args);
    }
}
