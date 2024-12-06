package com.bhft.todo.socket;

import org.awaitility.Awaitility;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

public class WebSocketClient {
    private static final long DEFAULT_TIMEOUT_MILLIS = 5000;
    private static final long POLL_INTERVAL_MILLIS = 100;
    private WebSocket webSocket;
    private final LinkedBlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    public void connect(String uri) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> wsFuture = client.newWebSocketBuilder()
                .buildAsync(URI.create(uri), new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        webSocket.request(1);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        messageQueue.offer(data.toString());
                        webSocket.request(1);
                        return null;
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        error.printStackTrace();
                    }
                });

        webSocket = wsFuture.get();
    }

    public String waitForMessage() throws InterruptedException {
        final String[] result = new String[1];
        Awaitility.await()
                .atMost(DEFAULT_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
                .pollInterval(POLL_INTERVAL_MILLIS, TimeUnit.MILLISECONDS)
                .until(() -> {
                    result[0] = messageQueue.poll();
                    return result[0] != null; // Условие завершения ожидания
                });
        return result[0];
    }

    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Test done").join();
        }
    }
}
