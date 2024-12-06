package com.bhft.todo.socket;

import com.bhft.todo.BaseTest;
import com.bhft.todo.config.Config;
import com.bhft.todo.generators.TestDataGenerator;
import com.bhft.todo.models.Todo;
import com.bhft.todo.models.WebSocketResponse;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;

public class WebSocketTests extends BaseTest {
    private static WebSocketClient wsClient;
    private static String wsUrl;

    private ValidatedTodoRequests unAuthValidatedTodoRequest;

    @BeforeEach
    public void setupTestData() {
        unAuthValidatedTodoRequest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
        softly = new SoftAssertions();
        objectMapper = new ObjectMapper();
    }

    @BeforeAll
    public static void setUp() throws Exception {
        wsUrl = Config.getProperty("wsUrl");
        wsClient = new WebSocketClient();
        wsClient.connect(wsUrl);
    }

    @AfterAll
    public static void tearDown() {
        wsClient.close();
    }

    @Test
    @DisplayName("Проверка получения уведомления о создании нового TODO")
    public void testTodoCreationNotification() throws Exception {
        Todo todo = TestDataGenerator.generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(todo);

        String msg = wsClient.waitForMessage();

        WebSocketResponse response = objectMapper.readValue(msg, WebSocketResponse.class);

        softly.assertThat("new_todo").isEqualTo(response.getType());
        softly.assertThat(todo.getId()).isEqualTo(response.getData().getId());
        softly.assertThat(todo.getText()).isEqualTo(response.getData().getText());
        softly.assertThat(todo.isCompleted()).isEqualTo(response.getData().isCompleted());
        softly.assertAll();
    }
}
