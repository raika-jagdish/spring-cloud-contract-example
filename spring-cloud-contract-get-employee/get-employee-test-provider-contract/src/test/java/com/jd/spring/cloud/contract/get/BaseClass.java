package com.jd.spring.cloud.contract.get;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , properties = {
        "management.server.port=0",
        "server.port=0"
}
)

public abstract class BaseClass {

    @LocalServerPort
    private int port;

    @Value("${app.employeeBaseURI:http://localhost}")
    String employeeBaseURI;

    @Value("${app.employeeBasePath:/employee-management/employee}")
    String employeeBasePath;

    @Before
    public void setup() {
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = employeeBaseURI;
        if (RestAssured.baseURI.contains("localhost")) {
            RestAssured.port = port;
        }
    }

    public String getUrlPath() {
        return employeeBasePath;
    }
}