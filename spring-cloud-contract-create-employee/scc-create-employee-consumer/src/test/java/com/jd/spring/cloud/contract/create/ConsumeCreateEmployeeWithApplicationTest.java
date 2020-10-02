package com.jd.spring.cloud.contract.create;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {"location.getEmployee.url=http://localhost:8090/employee-management"})
@AutoConfigureStubRunner(
        ids = {"com.jd.spring:scc-get-employee-provider:8090"}
        , stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ConsumeCreateEmployeeWithApplicationTest {

    @LocalServerPort
    private int port;

    @Value("${app.createEmployeeURI:http://localhost}")
    String createEmployeeURI;

    @Value("${app.createEmployeeBasePath:/employee-management/employee}")
    String createEmployeeBasePath;

    @Before
    public void setup() {

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = createEmployeeURI;
        if (RestAssured.baseURI.contains("localhost")) {
            RestAssured.port = port;
        }
    }

    @Test
    public void validate_shouldCreateEmployeeProfile() throws Exception {
        // given:
        RequestSpecification request = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body("{\"firstName\":\"Jagdish\",\"LastName\":\"Raika\",\"aadharNo\":1234567890}");

        // when:
        Response response = given().spec(request)
                .queryParam("salesChannel", "channel")
                .post(createEmployeeBasePath);

        // then:
        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.header("Content-Type")).matches("application/json.*");

        System.out.println(response.getBody().asString());

        // and:
        DocumentContext parsedJson = JsonPath.parse(response.getBody().asString());
        assertThatJson(parsedJson).field("['id']").matches("([1-9]\\\\d*)");
        assertThatJson(parsedJson).field("['firstName']").matches("Jagdish");
        assertThatJson(parsedJson).field("['LastName']").matches("Raika");
        assertThatJson(parsedJson).field("['aadharNo']").isEqualTo(1234567890);
        assertThatJson(parsedJson).field("['status']").matches("(false)");
        assertThatJson(parsedJson).field("['message']").matches("New employee created");
    }
}
