package com.bhft.todo.read;

import com.bhft.todo.BaseTest;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import io.qameta.allure.Description;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bhft.todo.generators.TestDataGenerator.generateTestData;

@DisplayName("Тесты на чтение списка Todos")
public class ReadTodosTests extends BaseTest {

    private ValidatedTodoRequests unAuthValidatedTodoRequest;

    @BeforeEach
    public void setupTestData() {
        unAuthValidatedTodoRequest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
        softly = new SoftAssertions();
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @DisplayName("TC1: Юзер получает пустой список Todos, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> listOfTodo = unAuthValidatedTodoRequest.readAll();
        Assertions.assertTrue(listOfTodo.isEmpty());
    }

    @Test
    @DisplayName("TC2: Юзер получает список Todos, когда база данных не пуста")
    public void testGetTodosWhenDatabaseIsNOTEmpty() {
        Todo newTodoFirst = generateTestData(Todo.class);
        Todo newTodoSecond = generateTestData(Todo.class);

        unAuthValidatedTodoRequest.create(newTodoFirst);
        unAuthValidatedTodoRequest.create(newTodoSecond);

        List<Todo> listOfTodo = unAuthValidatedTodoRequest.readAll();
        softly.assertThat(listOfTodo.size()).isEqualTo(2);
        softly.assertThat(newTodoFirst.getId()).isEqualTo(listOfTodo.get(0).getId());
        softly.assertThat(newTodoSecond.getId()).isEqualTo(listOfTodo.get(1).getId());
        softly.assertAll();
    }

    @Test
    @DisplayName("TC3: Юзер получает список Todos, используя параметры offset=0 и limit=5 для пагинации")
    public void testGetTodosWithOffset0AndLimit5() {
        createMultipleTodo(10);

        List<Todo> todos = unAuthValidatedTodoRequest.readAll(0, 5);

        softly.assertThat(todos.size()).isLessThanOrEqualTo(5);
        softly.assertThat(todos).isNotEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("TC4: Юзер получает список Todos, используя параметры offset=5 и limit=5 для пагинации")
    public void testGetTodosWithOffset5AndLimit5() {
        createMultipleTodo(6);

        List<Todo> todos = unAuthValidatedTodoRequest.readAll(5, 5);

        softly.assertThat(todos.size()).isLessThanOrEqualTo(1);
        softly.assertThat(todos).isNotEmpty();
        softly.assertAll();
    }

    @Test
    @DisplayName("TC5: Юзер получает весь список Todos, используя параметры offset=0 и limit=100 для пагинации")
    public void testGetTodosWithOffset0AndLimit100() {
        createMultipleTodo(25);

        List<Todo> todos = unAuthValidatedTodoRequest.readAll(0, 100);

        softly.assertThat(todos.size()).isLessThanOrEqualTo(25);
        softly.assertThat(todos).isNotEmpty();
        softly.assertAll();
    }









}
