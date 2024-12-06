package com.bhft.todo.create;

import com.bhft.todo.BaseTest;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.TodoRequests;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import io.qameta.allure.Epic;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
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
        softly = new SoftAssertions();
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @DisplayName("ТС1: Юзер создает объект с валидными данными")
    public void testCreateTodoWithValidData() {

        Todo newTodo = generateTestData(Todo.class);

        unAuthValidatedTodoRequests.create(newTodo);
        List<Todo> listTodo = unAuthValidatedTodoRequests.readAll();

        Assertions.assertEquals(listTodo.get(0).getText(), newTodo.getText());
    }

    @Test
    @DisplayName("ТС2: Юзер не может создать объект с существующим id")
    public void testCreateResourceDuplicateShouldReturnConflict400() {
        Todo newTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequests.create(newTodo);

        Todo existTodoTheSameId = new Todo(newTodo.getId(), "Same Id", true);
        unAuthTodoRequest.create(existTodoTheSameId)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST);

        List<Todo> listTodo = unAuthValidatedTodoRequests.readAll();

        Assertions.assertEquals(1, listTodo.size());
        Assertions.assertEquals(newTodo.getText(), listTodo.get(0).getText());
    }

    @Test
    @DisplayName("ТС3: Юзер успешно создает несколько объектов")
    public void testCreateMultipleTodoWithValidData() {
        Todo newTodo = generateTestData(Todo.class);
        Todo newTodo2 = generateTestData(Todo.class);
        Todo newTodo3 = generateTestData(Todo.class);

        unAuthValidatedTodoRequests.create(newTodo);
        unAuthValidatedTodoRequests.create(newTodo2);
        unAuthValidatedTodoRequests.create(newTodo3);

        List<Todo> listTodo = unAuthValidatedTodoRequests.readAll();
        softly.assertThat(listTodo.get(0).getText()).isEqualTo(newTodo.getText());
        softly.assertThat(listTodo.get(1).getText()).isEqualTo(newTodo2.getText());
        softly.assertThat(listTodo.get(2).getText()).isEqualTo(newTodo3.getText());
        softly.assertThat(listTodo.size()).isEqualTo(3);
        softly.assertAll();
    }
}
