package com.bhft.todo.delete;

import com.bhft.todo.generators.TestDataGenerator;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.TodoRequests;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DeleteTodosTests {

    private ValidatedTodoRequests authValidatedTodoRequests;
    private TodoRequests unAuthTodoRequest;


    @BeforeEach
    public void setupTestData() {
        authValidatedTodoRequests = new ValidatedTodoRequests(RecSpecs.authSpec());
        unAuthTodoRequest = new TodoRequests(RecSpecs.unAuthSpec());
    }

    @Test
    @DisplayName("TC1: Успешное удаление существующего TODO с корректной авторизацией.")
    public void testDeleteExistingTodoWithValidAuth() {

        Todo todo = TestDataGenerator.generateTestData(Todo.class);
        authValidatedTodoRequests.create(todo);
        authValidatedTodoRequests.delete(todo.getId());
        List<Todo> listTodo = authValidatedTodoRequests.readAll(0, 10);

        // Проверяем, что удаленная задача отсутствует в списке
        boolean found = false;
        for (Todo t : listTodo) {
            if (t.getId() == todo.getId()) {
                found = true;
                break;
            }
        }
        Assertions.assertFalse(found, "Удаленная задача все еще присутствует в списке TODO");
    }

    @Test
    @DisplayName("TC2: Попытка удаления без авторизации.")
    public void testDeleteExistingTodoWithoutAuth() {

        Todo todo = TestDataGenerator.generateTestData(Todo.class);
        authValidatedTodoRequests.create(todo);
        unAuthTodoRequest.delete(todo.getId());
        List<Todo> listTodo = authValidatedTodoRequests.readAll(0, 10);

        Assertions.assertFalse(listTodo.isEmpty());
    }


}
