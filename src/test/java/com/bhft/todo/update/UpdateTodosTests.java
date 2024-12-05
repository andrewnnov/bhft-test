package com.bhft.todo.update;

import com.bhft.todo.BaseTest;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.TodoRequests;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import org.apache.http.HttpStatus;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bhft.todo.generators.TestDataGenerator.generateTestData;

public class UpdateTodosTests extends BaseTest {

    private ValidatedTodoRequests unAuthValidatedTodoRequest;
    private TodoRequests unAuthTodoRequest;

    @BeforeEach
    public void setupTestData() {
        unAuthValidatedTodoRequest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
        unAuthTodoRequest = new TodoRequests(RecSpecs.unAuthSpec());
        softly = new SoftAssertions();
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @DisplayName("TC1: Юзер обновляет объект корректными данными.")
    public void testUpdateExistingTodoWithValidData() {
        Todo originalTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(originalTodo);
        Todo updatedTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.update(originalTodo.getId(), updatedTodo);
        List<Todo> listTodo = unAuthValidatedTodoRequest.readAll();

        softly.assertThat(listTodo.size())
                .as("Check size of list")
                .isEqualTo(1);

        softly.assertThat(updatedTodo.getText())
                .as("Check text of updated todo")
                .isEqualTo(listTodo.get(0).getText());

        softly.assertThat(updatedTodo.isCompleted())
                .as("Check completed status of updated todo")
                .isEqualTo(listTodo.get(0).isCompleted());
        softly.assertAll();
    }

    @Test
    @DisplayName("TC2: Юзер не может обновить объект с несуществующим id.")
    public void testUpdateNonExistentTodo() {

        Todo updatedTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(updatedTodo);

        unAuthTodoRequest.update(updatedTodo.getId() - 1, updatedTodo)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("TC3: Юзер обновляет объект без изменения данных (передача тех же значений).")
    public void testUpdateTodoWithoutChangingData() {
        Todo originalTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(originalTodo);

        unAuthValidatedTodoRequest.update(originalTodo.getId(), originalTodo);

        List<Todo> listTodo = unAuthValidatedTodoRequest.readAll();

        softly.assertThat(originalTodo.getId()).isEqualTo(listTodo.get(0).getId());
        softly.assertThat(originalTodo.getText()).isEqualTo(listTodo.get(0).getText());
        softly.assertAll();
    }
}
