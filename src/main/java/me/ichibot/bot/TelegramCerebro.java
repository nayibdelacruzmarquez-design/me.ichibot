package me.ichibot.bot;

import me.ichibot.hardware.ConexionSerial;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramCerebro implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient client;
    private final ConexionSerial placa;

    public TelegramCerebro(String token, ConexionSerial placa) {
        this.client = new OkHttpTelegramClient(token);
        this.placa = placa;
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (text) {
                case "/start" -> enviarMenu(chatId, "Panel de Control IchiBot:");
                case "💡 Foco 1 ON" -> { placa.enviarComando("1"); responder(chatId, "Foco 1 encendido"); }
                case "🌑 Foco 1 OFF" -> { placa.enviarComando("0"); responder(chatId, "Foco 1 apagado"); }
                case "💡 Foco 2 ON" -> { placa.enviarComando("3"); responder(chatId, "Foco 2 encendido"); }
                case "🌑 Foco 2 OFF" -> { placa.enviarComando("2"); responder(chatId, "Foco 2 apagado"); }
                case "🌱 Humedad" -> { placa.enviarComando("H"); responder(chatId, "Consultando sensor..."); }
                default -> responder(chatId, "Usa los botones.");
            }
        }
    }

    private void enviarMenu(long chatId, String txt) {
        ReplyKeyboardMarkup menu = ReplyKeyboardMarkup.builder()
                .keyboardRow(new KeyboardRow("💡 Foco 1 ON", "🌑 Foco 1 OFF"))
                .keyboardRow(new KeyboardRow("💡 Foco 2 ON", "🌑 Foco 2 OFF"))
                .keyboardRow(new KeyboardRow("🌱 Humedad"))
                .resizeKeyboard(true).build();
        try { client.execute(SendMessage.builder().chatId(String.valueOf(chatId)).text(txt).replyMarkup(menu).build()); } catch (Exception e) {}
    }

    private void responder(long id, String t) {
        try { client.execute(SendMessage.builder().chatId(String.valueOf(id)).text(t).build()); } catch (Exception e) {}
    }
}