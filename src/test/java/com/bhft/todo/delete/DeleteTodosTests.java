package com.bhft.todo.delete;

import com.bhft.todo.BaseTest;
import com.bhft.todo.generators.TestDataGenerator;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.TodoRequests;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bhft.todo.generators.TestDataGenerator.*;

public class DeleteTodosTests extends BaseTest {

    private ValidatedTodoRequests authValidatedTodoRequests;
    private TodoRequests unAuthTodoRequest;

    @BeforeEach
    public void setupTestData() {
        authValidatedTodoRequests = new ValidatedTodoRequests(RecSpecs.authSpec());
        unAuthTodoRequest = new TodoRequests(RecSpecs.unAuthSpec());
        softly = new SoftAssertions();
    }

    @Test
    @DisplayName("TC1: Авторизованный юзер удаляет объект")
    public void testDeleteExistingTodoWithValidAuth() {
        Todo todo = generateTestData(Todo.class);
        authValidatedTodoRequests.create(todo);
        authValidatedTodoRequests.delete(todo.getId());
        List<Todo> listTodo = authValidatedTodoRequests.readAll();

        boolean found = listTodo.stream()
                .anyMatch(t -> t.getId() == todo.getId());

        Assertions.assertFalse(found, "Удаленная задача все еще присутствует в списке TODO");
    }

    @Test
    @DisplayName("TC2: Неавторизованный юзер не может удалить объект")
    public void testDeleteExistingTodoWithoutAuth() {
        Todo todo = generateTestData(Todo.class);
        authValidatedTodoRequests.create(todo);

        unAuthTodoRequest.delete(todo.getId());

        List<Todo> listTodo = authValidatedTodoRequests.readAll();
        Assertions.assertFalse(listTodo.isEmpty());
    }
}
