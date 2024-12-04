package com.bhft.todo.specs;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.List;

public class RecSpecs {
    public static RequestSpecBuilder baseSpecBuilder() {
        RequestSpecBuilder reqSpecBuilder = new RequestSpecBuilder();
        reqSpecBuilder.setContentType(ContentType.JSON);
        reqSpecBuilder.addFilters(List.of(
                new AllureRestAssured(),
                new ResponseLoggingFilter(),
                new ResponseLoggingFilter()));
        return reqSpecBuilder;
    }

    public static RequestSpecification unAuthSpec() {
        return baseSpecBuilder().build();
    }

    public static RequestSpecification authSpec() {
        RequestSpecBuilder reqSpecBuilder = baseSpecBuilder();
        reqSpecBuilder.setAuth(
                io.restassured.RestAssured.preemptive().basic("admin", "admin")
        );
        return reqSpecBuilder.build();
    }
}
