package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should create a employee profile for given details"

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
                "aadharNo": anyPositiveInt()
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
                "LastName": $(fromRequest().body('$.lastName')),
                "aadharNo": $(fromRequest().body('$.aadharNo')),
                "status": false,
                "message": "New employee created"
        )

    }
}