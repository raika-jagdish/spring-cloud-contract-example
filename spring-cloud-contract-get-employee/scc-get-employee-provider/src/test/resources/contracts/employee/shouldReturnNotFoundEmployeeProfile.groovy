package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should return an empty employee profile for given details"

    request {
        method(GET())

        urlPath("/employee-management/employee/")
        urlPath($(
                consumer(regex("/employee-management/employee/[0][0-9]{1,}"))
                , producer("/employee-management/employee/0123456789")
        )){
        }

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
                "status": "EMPLOYEE_NOT_FOUND"
        )

    }
}