package com.bhft.todo.requests;

import com.bhft.todo.models.Todo;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

import java.util.List;

public class ValidatedTodoRequests extends Requests implements CrudInterface<Todo>, SearchInterface<Todo> {

    private TodoRequests todoRequests;

    public ValidatedTodoRequests(RequestSpecification reqSpec) {
        super(reqSpec);
        ENDPOINT_URL = "/todos/";
        todoRequests = new TodoRequests(reqSpec);
    }

    @Override
    public Object create(Todo entity) {
        return todoRequests.create(entity)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED);

    }

    @Override
    public Object update(long id, Todo entity) {
        return todoRequests.update(id, entity)
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Override
    public String delete(long id) {
        return todoRequests.delete(id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT)
                .extract().body()
                .asString();
    }

    @Override
    public List<Todo> readAll(int offset, int limit) {
        Todo[] todos = todoRequests.readAll(offset, limit)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract().as(Todo[].class);
        return List.of(todos);
    }
}
