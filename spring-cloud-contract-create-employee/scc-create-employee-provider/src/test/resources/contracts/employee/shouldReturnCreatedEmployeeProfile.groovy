package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should return already created employee profile for given details"

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
                        consumer(regex("[0][0-9]{1,}"))
                        , producer("0123456789"))
        )
    }

    response {
        status OK()
        headers {
            contentType applicationJson()
        }
        body(
                "id": "${anyPositiveInt()}",
                "firstName": $(anyNonEmptyString()),
                "lastName": $(anyNonEmptyString()),
                "identityCardNo": $(fromRequest().body('$.identityCardNo')),
                "status": "EMPLOYEE_FOUND"
        )

    }
}