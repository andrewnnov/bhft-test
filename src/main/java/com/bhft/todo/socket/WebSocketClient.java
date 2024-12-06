package com.bhft.todo.socket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class WebSocketClient {
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

    public String waitForMessage(long timeoutMillis) throws InterruptedException {
        return messageQueue.poll(timeoutMillis, java.util.concurrent.TimeUnit.MILLISECONDS);
    }

    public void close() {
        if (webSocket != null) {
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Test done").join();
        }
    }
}
