package com.bhft.todo.create;

import com.bhft.todo.BaseTest;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.TodoRequests;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bhft.todo.generators.TestDataGenerator.generateTestData;

public class CreateTodosTests extends BaseTest {
    private ValidatedTodoRequests unAuthValidatedTodoRequests;
    private TodoRequests unAuthTodoRequest;


    @BeforeEach
    public void setupTestData() {
        unAuthValidatedTodoRequests = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
        unAuthTodoRequest = new TodoRequests(RecSpecs.unAuthSpec());
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @DisplayName("ТС1: Проверка создание объекта с валидными данными")
    public void testCreateTodoWithValidData() {
        Todo newTodo = generateTestData(Todo.class);

        unAuthValidatedTodoRequests.create(newTodo);
        List<Todo> listTodo = unAuthValidatedTodoRequests.readAll(0, 10);
        Assertions.assertEquals(listTodo.get(0).getText(), newTodo.getText());
    }

    @Test
    @DisplayName("ТС2: Попытка создать ресурс с данными, которые уже существуют")
    public void testCreateResourceDuplicateShouldReturnConflict409() {
        Todo newTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequests.create(newTodo);

        Todo existTodoTheSameId = new Todo(newTodo.getId(), "Same Id", true);
        unAuthTodoRequest.create(existTodoTheSameId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);


        List<Todo> listTodo = unAuthValidatedTodoRequests.readAll(0, 10);
        Assertions.assertEquals(1, listTodo.size());
        Assertions.assertEquals(newTodo.getText(), listTodo.get(0).getText());
    }


}
