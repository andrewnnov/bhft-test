package com.bhft.todo.requests;

import com.bhft.todo.config.Config;
import com.bhft.todo.models.Todo;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TodoRequests extends Requests implements CrudInterface<Todo>, SearchInterface<Todo> {


    public TodoRequests(RequestSpecification reqSpec) {
        super(reqSpec);
        ENDPOINT_URL = Config.getProperty("endpointURI");
    }

    @Override
    @Step("create {entity}")
    public Response create(Todo entity) {
        return given()
                .spec(reqSpec)
                .body(entity)
                .post(ENDPOINT_URL);
    }

    @Override
    @Step("update by id={id} {entity}")
    public Response update(long id, Todo entity) {
        return given()
                .spec(reqSpec)
                .body(entity)
                .put(ENDPOINT_URL + id);
    }

    @Override
    @Step("delete by id={id}")
    public Response delete(long id) {
        return given()
                .spec(reqSpec)
                .delete(ENDPOINT_URL + id);
    }

    @Override
    @Step("readAll offset={offset} limit={limit}")
    public Response readAll(int offset, int limit) {
        return given()
                .queryParam("offset", offset)
                .queryParam("limit", limit)
                .when()
                .get(ENDPOINT_URL);
    }

    @Step("readAll")
    public Response readAll() {
        return given()
                .when()
                .get(ENDPOINT_URL);
    }
}
