package com.bhft.todo.requests;

import io.restassured.specification.RequestSpecification;

public class Requests {
    protected String ENDPOINT_URL;
    protected RequestSpecification reqSpec;

    public Requests(RequestSpecification reqSpec) {
        this.reqSpec = reqSpec;
    }
}
