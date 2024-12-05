package com.bhft.todo.update;

import com.bhft.todo.BaseTest;
import com.bhft.todo.generators.TestDataGenerator;
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
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @DisplayName("TC1: Обновление существующего TODO корректными данными.")
    public void testUpdateExistingTodoWithValidData() {
        Todo originalTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(originalTodo);
        Todo updatedTodo = generateTestData(Todo.class);
        unAuthValidatedTodoRequest.update(originalTodo.getId(), updatedTodo);
        List<Todo> listTodo = unAuthValidatedTodoRequest.readAll(0, 10);

        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(listTodo.size())
                .as("Check size of list")
                .isEqualTo(1);

        softAssertions.assertThat(updatedTodo.getText())
                .as("Check text of updated todo")
                .isEqualTo(listTodo.get(0).getText());

        softAssertions.assertThat(updatedTodo.isCompleted())
                .as("Check completed status of updated todo")
                .isEqualTo(listTodo.get(0).isCompleted());
        softAssertions.assertAll();
    }

    @Test
    @DisplayName("TC2: Попытка обновления TODO с несуществующим id.")
    public void testUpdateNonExistentTodo() {

        Todo updatedTodo = TestDataGenerator.generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(updatedTodo);

        unAuthTodoRequest.update(updatedTodo.getId() - 1L, updatedTodo)
                .then()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @DisplayName("TC3: Обновление TODO без изменения данных (передача тех же значений).")
    public void testUpdateTodoWithoutChangingData() {
        Todo originalTodo = TestDataGenerator.generateTestData(Todo.class);
        unAuthValidatedTodoRequest.create(originalTodo);
        unAuthValidatedTodoRequest.update(originalTodo.getId(), originalTodo);
        List<Todo> listTodo = unAuthValidatedTodoRequest.readAll(0, 10);


        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(originalTodo.getId()).isEqualTo(listTodo.get(0).getId());
        softAssertions.assertThat(originalTodo.getText()).isEqualTo(listTodo.get(0).getText());
    }
}
