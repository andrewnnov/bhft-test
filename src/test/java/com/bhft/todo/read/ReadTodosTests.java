package com.bhft.todo.read;

import com.bhft.todo.BaseTest;
import com.bhft.todo.models.Todo;
import com.bhft.todo.requests.ValidatedTodoRequests;
import com.bhft.todo.specs.RecSpecs;
import io.qameta.allure.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.bhft.todo.generators.TestDataGenerator.generateTestData;

public class ReadTodosTests extends BaseTest {

    private ValidatedTodoRequests unAuthValidatedTodoRequest;

    @BeforeEach
    public void setupTestData() {
        unAuthValidatedTodoRequest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @Description("TC1: Получение пустого списка TODO, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> listOfTodo = unAuthValidatedTodoRequest.readAll(0, 10);
        Assertions.assertTrue(listOfTodo.isEmpty());
    }

    @Test
    @Description("TC2: Получение списка TODO, когда база данных не пуста")
    public void testGetTodosWhenDatabaseIsNOTEmpty() {
        Todo newTodoFirst = generateTestData(Todo.class);
        Todo newTodoSecond = generateTestData(Todo.class);


        unAuthValidatedTodoRequest.create(newTodoFirst);
        unAuthValidatedTodoRequest.create(newTodoSecond);

        List<Todo> listOfTodo = unAuthValidatedTodoRequest.readAll(0, 10);
        Assertions.assertEquals(listOfTodo.size(), 2);
        Assertions.assertEquals(newTodoFirst.getId(), listOfTodo.get(0).getId());
        Assertions.assertEquals(newTodoSecond.getId(), listOfTodo.get(1).getId());
    }



}
