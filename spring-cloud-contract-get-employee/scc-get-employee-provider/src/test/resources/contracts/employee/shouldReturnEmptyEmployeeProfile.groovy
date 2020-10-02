package contracts.employee

import org.springframework.cloud.contract.spec.Contract


Contract.make {
    description "should return a employee profile for given aadhar no."

    request {
        method(GET())

        urlPath("/employee-management/employee/")
        urlPath($(
                consumer(regex("/employee-management/employee/[0-9]{1,}"))
                , producer("/employee-management/employee/1234567890")
        )){
        }

        headers {
            contentType(applicationJson())
            accept(applicationJson())
        }
    }

    response {
        status NOT_FOUND()
        headers {
            contentType applicationJson()
        }
        body(
                "aadharNo": fromRequest().path(2),
                "status": false,
                "message": "Employee not found"
        )

    }
}