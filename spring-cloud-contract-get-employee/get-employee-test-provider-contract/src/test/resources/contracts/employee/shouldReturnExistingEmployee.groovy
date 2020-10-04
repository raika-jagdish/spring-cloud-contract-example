package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should return an employee profile for given details."

    request {
        method(GET())

        urlPath("/employee-management/employee/")
        urlPath($(
                consumer(regex("/employee-management/employee/[1-9][0-9]{0,}"))
                , producer("/employee-management/employee/1234567890")
        ))

        headers {
            contentType(applicationJson())
            accept(applicationJson())
        }
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                "id": "${(regex('[1-9][0-9]{0,}'))}",
                "firstName": anyAlphaUnicode(),
                "lastName": anyAlphaUnicode(),
                "identityCardNo": fromRequest().path(2),
                "status": "EMPLOYEE_FOUND"
        )
    }
}