package com.bhft.todo;

import com.bhft.todo.config.Config;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.bhft.todo.generators.TestDataGenerator.generateTestData;

public class BaseTest {

    private ValidatedTodoRequests authTodoRequest = new ValidatedTodoRequests(RecSpecs.authSpec());
    private ValidatedTodoRequests unAuthTodoRquest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
    protected SoftAssertions softly;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = Config.getProperty("baseURI");
        RestAssured.port = Integer.parseInt(Config.getProperty("port"));
    }

    public void deleteAllTodos() {
        List<Todo> todos = unAuthTodoRquest.readAll();
        todos.stream()
                .forEach(todo -> authTodoRequest.delete(todo.getId()));
    }

    public List<Todo> createMultipleTodo(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    Todo todo = generateTestData(Todo.class);
                    unAuthTodoRquest.create(todo);
                    return todo;
                })
                .collect(Collectors.toList());
    }
}
