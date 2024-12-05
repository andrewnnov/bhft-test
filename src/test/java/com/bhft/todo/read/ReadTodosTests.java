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

public class ReadTodosTests extends BaseTest {

    private ValidatedTodoRequests unAuthTodoRequest;

    @BeforeEach
    public void setupTestData() {
        unAuthTodoRequest = new ValidatedTodoRequests(RecSpecs.unAuthSpec());
    }

    @BeforeEach
    public void setupEach() {
        deleteAllTodos();
    }

    @Test
    @Description("TC1: Получение пустого списка TODO, когда база данных пуста")
    public void testGetTodosWhenDatabaseIsEmpty() {
        List<Todo> listOfTodo = unAuthTodoRequest.readAll(0, 10);
        Assertions.assertTrue(listOfTodo.isEmpty());
    }

}
