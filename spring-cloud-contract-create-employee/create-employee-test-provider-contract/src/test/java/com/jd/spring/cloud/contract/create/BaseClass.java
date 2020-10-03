package com.jd.spring.cloud.contract.create;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CreateEmployeeApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
        , properties = {
        "management.server.port=0",
        "server.port=0"
}
)
@AutoConfigureStubRunner(
        ids = {"com.jd.spring:get-employee-test-provider-contract:8180"}
        , stubsMode = StubRunnerProperties.StubsMode.LOCAL)
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