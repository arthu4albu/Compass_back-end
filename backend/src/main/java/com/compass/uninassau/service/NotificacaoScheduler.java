package com.compass.uninassau.service;

import com.compass.uninassau.entity.NotificacaoAgendada;
import com.compass.uninassau.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoScheduler {

    @Autowired
    private NotificacaoRepository repository;

    // Executa a cada 1 minuto
    @Scheduled(cron = "0 * * * * *")
    public void enviarNotificacoes() {
        LocalDateTime agora = LocalDateTime.now().withSecond(0).withNano(0);

        List<NotificacaoAgendada> notificacoes = repository.findByEnviadaFalseAndDataHoraBefore(agora);

        for (NotificacaoAgendada n : notificacoes) {
            boolean enviado = enviarParaExpo(n);
            if (enviado) {
                // Remove ou marca como enviada
                repository.delete(n);
                // ou: n.setEnviada(true); repository.save(n);
            }
        }
    }

    private boolean enviarParaExpo(NotificacaoAgendada n) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            String bodyJson = """
            {
                "to": "%s",
                "title": "%s",
                "body": "%s"
            }
            """.formatted(n.getExpoPushToken(), n.getTitulo(), n.getMensagem());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://exp.host/--/api/v2/push/send"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Log da resposta do Expo
            System.out.println("Notificação enviada para: " + n.getExpoPushToken());
            System.out.println("Resposta do Expo: " + response.body());

            return true;

        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
            return false;
        }
    }
}
