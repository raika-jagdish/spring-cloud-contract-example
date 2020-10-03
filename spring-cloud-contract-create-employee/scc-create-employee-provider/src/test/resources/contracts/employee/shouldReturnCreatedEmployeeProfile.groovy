package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should create an employee profile for given details"

    request {
        method(POST())

        urlPath("/employee-management/employee") {
        }

        headers {
            contentType(applicationJson())
            accept(applicationJson())
        }

        body(
                "firstName": anyAlphaUnicode(),
                "lastName": anyAlphaUnicode(),
                "identityCardNo": $(
                        consumer(regex("[1-9][0-9]{1,}"))
                        , producer("1234567890"))
        )
    }

    response {
        status CREATED()
        headers {
            contentType applicationJson()
        }
        body(
                "id": "${anyPositiveInt()}",
                "firstName": $(fromRequest().body('$.firstName')),
                "lastName": $(fromRequest().body('$.lastName')),
                "identityCardNo": $(fromRequest().body('$.identityCardNo')),
                "status": "EMPLOYEE_FOUND"
        )

    }
}