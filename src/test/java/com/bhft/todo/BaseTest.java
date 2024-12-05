package com.bhft.todo;

import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

public class BaseTest {

    private ValidatedTodoRequests authTodoRequest = new ValidatedTodoRequests(RecSpecs.authSpec());
    private ValidatedTodoRequests unAuthTodoRquest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    public void deleteAllTodos() {
        List<Todo> todos = unAuthTodoRquest.readAll(0, 10);
        todos.stream()
                .forEach(todo -> authTodoRequest.delete(todo.getId()));
    }
}
