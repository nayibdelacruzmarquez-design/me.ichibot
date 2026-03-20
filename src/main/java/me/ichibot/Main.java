package me.ichibot;

import me.ichibot.bot.TelegramCerebro;
import me.ichibot.hardware.ConexionSerial;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ConexionSerial placa = new ConexionSerial();
        String puerto = "COM3"; // CAMBIA SEGÚN TU PC
        String token = "8796152929:AAGp1WxcbOX8-g7BD9C_trE_XMHMCSNF4Pk";

        if (placa.conectar(puerto)) {
            try (TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication()) {
                // Inyectamos la placa al cerebro del bot
                app.registerBot(token, new TelegramCerebro(token, placa));
                logger.info("🚀 IchiBot Pro v2 en línea!");
                Thread.currentThread().join();
            } catch (Exception e) {
                logger.error("Error: " + e.getMessage());
            }
        }
    }
}