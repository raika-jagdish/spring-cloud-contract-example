package com.jd.spring.cloud.contract.create;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jd.spring.cloud.contract.create.model.Employee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = ConsumeCreateEmployeeWithContractTest.Autoconfig.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(
        ids = {"com.jd.spring:scc-create-employee-provider:${version:+}"}
        , stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class ConsumeCreateEmployeeWithContractTest {

    @Autowired
    StubFinder stubFinder;

    @Test
    public void testGetExistingEmployeeUsingStub() throws Exception {
        // expect: 'WireMocks are running'
        then(stubFinder.findStubUrl("com.jd.spring", "scc-create-employee-provider")).isNotNull();

        // and: 'Stub is running'
        then(stubFinder.findAllRunningStubs().isPresent("scc-create-employee-provider")).isTrue();

        // and: 'Stubs were registered and make an actual call to the wiremock stub'
        String URL = stubFinder.findStubUrl("scc-create-employee-provider").toString() + "/employee-management/employee";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", APPLICATION_JSON_VALUE);
        headers.set("Content-Type", APPLICATION_JSON_VALUE);
        Employee emp = new Employee();
        emp.setFirstName("Jagdish");
        emp.setLastName("Raika");
        emp.setIdentityCardNo("1234567890");
        ResponseEntity<String> response =new RestTemplate()
                .exchange(URL, POST, new HttpEntity<>(emp,headers), String.class);

        System.out.println("response.getBody().toString(): "+ response.getBody());

        // then:
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getHeaders().get("Content-Type").contains("application/json.*"));

        System.out.println(response.getBody());

        // and:
        DocumentContext parsedJson = JsonPath.parse(response.getBody());
        assertThatJson(parsedJson).field("['id']").matches("[1-9][0-9]{0,}");
        assertThatJson(parsedJson).field("['firstName']").matches("[\\p{L}]*");
        assertThatJson(parsedJson).field("['lastName']").matches("[\\p{L}]*");
        assertThatJson(parsedJson).field("['identityCardNo']").isEqualTo(1234567890);
        assertThatJson(parsedJson).field("['status']").matches("EMPLOYEE_FOUND");
    }

    @Test
    public void testCreateNewEmployeeUsingStub() throws Exception {
        // expect: 'WireMocks are running'
        then(stubFinder.findStubUrl("com.jd.spring", "scc-create-employee-provider")).isNotNull();

        // and: 'Stub is running'
        then(stubFinder.findAllRunningStubs().isPresent("scc-create-employee-provider")).isTrue();

        // and: 'Stubs were registered and make an actual call to the wiremock stub'
        String URL = stubFinder.findStubUrl("scc-create-employee-provider").toString() + "/employee-management/employee";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", APPLICATION_JSON_VALUE);
        headers.set("Content-Type", APPLICATION_JSON_VALUE);
        Employee emp = new Employee();
        emp.setFirstName("Jagdish");
        emp.setLastName("Raika");
        emp.setIdentityCardNo("0123456789");
        ResponseEntity<String> response =new RestTemplate()
                .exchange(URL, POST, new HttpEntity<>(emp,headers), String.class);

        System.out.println("response.getBody().toString(): "+ response.getBody());

        // then:
        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getHeaders().get("Content-Type").contains("application/json.*"));

        System.out.println(response.getBody());

        // and:
        DocumentContext parsedJson = JsonPath.parse(response.getBody());
        assertThatJson(parsedJson).field("['id']").matches("[1-9][0-9]{0,}");
        assertThatJson(parsedJson).field("['firstName']").matches("Jagdish");
        assertThatJson(parsedJson).field("['lastName']").matches("Raika");
        assertThatJson(parsedJson).field("['identityCardNo']").isEqualTo("0123456789");
        assertThatJson(parsedJson).field("['status']").matches("NEW_EMPLOYEE_CREATED");
    }

    @SpringBootConfiguration
    public static class Autoconfig{}
}
