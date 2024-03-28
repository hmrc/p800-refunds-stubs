
# p800-refunds-stubs

## Scenarios

The microservice responds with configured response depending on the incoming request data.

### Example Test Data

| Nino pattern | Behaviour of Check Reference API  (1)       | Behaviour of Trace Individual API (1) | Behaviour of Get Bank Risk Result API (2) | Behaviour of Issue Payable Order API (2) |  Claim Overpayment API (6)  | Behaviour of Suspend Overpayment API (4) | Notify Case Management (5) |
|--------------|---------------------------------------------|---------------------------------------|-------------------------------------------|------------------------------------------|-----------------------------|------------------------------------------|----------------------------|
| `AB999999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | 200 - Happy path                         |  200 - Happy path           | N/A                                      | N/A                        |
| `AB099999C`  | 404 - Nino And P800Reference aren't matched | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB199999C`  | 422 - Refund is already taken               | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB299999C`  | 422 - Unprocessable Entity                  | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB399999C`  | 400 - BadRequest                            | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB499999C`  | 403 - Forbidden                             | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB599999C`  | 500 - InternalServerError                   | N/A                                   | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB699999C`  | 200 - Happy path                            | 404 - Not Found                       | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB799999C`  | 200 - Happy path                            | 400 - BadRequest                      | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB899999C`  | 200 - Happy path                            | 500 - InternalServerError             | N/A                                       | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB929999C`  | 200 - Happy path                            | 200 - Happy path                      | 400 - Bad Request                         | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB939999C`  | 200 - Happy path                            | 200 - Happy path                      | 403 - Forbidden                           | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB949999C`  | 200 - Happy path                            | 200 - Happy path                      | 404 - Resource not found                  | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB959999C`  | 200 - Happy path                            | 200 - Happy path                      | 500 - Des issues                          | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB969999C`  | 200 - Happy path                            | 200 - Happy path                      | 503 - Dependent system issues             | N/A                                      |  N/A                        | N/A                                      | N/A                        |
| `AB909999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | 422 - RefundAlreadyTaken                 |  N/A                        | N/A                                      | N/A                        |
| `AB999991C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | 200 - Happy path                         |  400 - Bad Request          | N/A                                      | N/A                        |
| `AB999992C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | N/A                                      |  403 - Forbidden            | N/A                                      | N/A                        |
| `AB999993C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | N/A                                      |  500 - InternalServerError  | N/A                                      | N/A                        |
| `AB999994C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | N/A                                      |  422 - Refund Already Taken | N/A                                      | N/A                        |
| `AB999995C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | N/A                                      |  422 - Suspended            | N/A                                      | N/A                        |
| `AB919999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | Happy Path (200)                         | 200 - Happy Path           |
| `AB919919C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | Happy Path (200)                         | 400 - Bad Request          |
| `AB919929C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | Happy Path (200)                         | 403 - Forbidden            |
| `AB919939C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | Happy Path (200)                         | 500 - InternalServerError  |
| `AB919099C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | InternalServerError (500)                | 200 - Happy Path           |
| `AB919039C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      |  N/A                        | InternalServerError (500)                | 500 - InternalServerError  |

| Nino pattern | Behaviour of Check Reference API  (1)       | Behaviour of Trace Individual API (1) | Behaviour of Get Bank Risk Result API (2) | Behaviour of Issue Payable Order API (2) | Behaviour of Suspend Overpayment API (4) |
|--------------|---------------------------------------------|---------------------------------------|-------------------------------------------|------------------------------------------|------------------------------------------|
| `AB999999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Happy path (Pay)                    | 200 - Happy path                         | N/A                                      |
| `AB099999C`  | 404 - Nino And P800Reference aren't matched | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB199999C`  | 422 - Refund is already taken               | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB299999C`  | 422 - Unprocessable Entity                  | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB399999C`  | 400 - BadRequest                            | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB499999C`  | 403 - Forbidden                             | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB599999C`  | 500 - InternalServerError                   | N/A                                   | N/A                                       | N/A                                      | N/A                                      |
| `AB699999C`  | 200 - Happy path                            | 404 - Not Found                       | N/A                                       | N/A                                      | N/A                                      |
| `AB799999C`  | 200 - Happy path                            | 400 - BadRequest                      | N/A                                       | N/A                                      | N/A                                      |
| `AB899999C`  | 200 - Happy path                            | 500 - InternalServerError             | N/A                                       | N/A                                      | N/A                                      |
| `AB919999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      | Happy Path (200)                         |
| `AB919099C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Unhappy Path (Do Not Pay)           | N/A                                      | InternalServerError (500)                |
| `AB929999C`  | 200 - Happy path                            | 200 - Happy path                      | 400 - Bad Request                         | N/A                                      | N/A                                      |
| `AB939999C`  | 200 - Happy path                            | 200 - Happy path                      | 403 - Forbidden                           | N/A                                      | N/A                                      |
| `AB949999C`  | 200 - Happy path                            | 200 - Happy path                      | 404 - Resource not found                  | N/A                                      | N/A                                      |
| `AB959999C`  | 200 - Happy path                            | 200 - Happy path                      | 500 - Des issues                          | N/A                                      | N/A                                      |
| `AB969999C`  | 200 - Happy path                            | 200 - Happy path                      | 503 - Dependent system issues             | N/A                                      | N/A                                      |
| `AB909999C`  | 200 - Happy path                            | 200 - Happy path                      | 200 - Pay - Happy Path                    | 422 - RefundAlreadyTaken                 | N/A                                      |

### `GET /nps-json-service/nps/v1/api/reconciliation/p800/:identifier/:paymentNumber`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario                              |
|-------------------------------------|---------------------------------------|
| `**0******`                         | Nino And P800Reference aren't matched |
| `**1******`                         | Refund is already taken               |
| `**2******`                         | Unprocessed Entity                    |
| `**3******`                         | BadRequest                            |
| `**4******`                         | Forbidden                             |
| `**5******`                         | InternalServerError                   |
| `*********`                         | Happy path (200)                      |

### `POST /nps-json-service/nps/v1/api/individual/trace-individual?exactMatch=true&returnRealName=true`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario            |
|-------------------------------------|---------------------|
| `**6******`                         | NotFound            |
| `**7******`                         | BadRequest          |
| `**8******`                         | InternalServerError |
| `**9******`                         | Happy Path (200)    |

### `PUT /nps-json-service/nps/v1/api/accounting/issue-payable-order/:identifier/:paymentNumber`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for IssuePayableOrder API   | Scenario            |
|------------------------------------------|---------------------|
| `***0*****`                              | RefundAlreadyTaken  |
| `*********`                              | Happy Path (200)    |

### `PUT /nps-json-service/nps/v1/api/accounting/claim-overpayment/:identifier/:paymentNumber

For this endpoint the selection is driven via the NINO number provided as the `:identifier` URL parameter according to
the below table.

| NINO pattern for ClaimOverpayment API | Scenario                                         |
|---------------------------------------|--------------------------------------------------|
| `*******1*`                           | Bad Request (400)                                |
| `*******2*`                           | Forbidden (403)                                  |
| `*******3*`                           | InternalServerError (500)                        |
| `*******4*`                           | RefundAlreadyTaken - Unprocessable Entity (422)  |
| `*******5*`                           | Suspended - Unprocessable Entity (422)           |
| `*********`                           | Happy Path (200)                                 |

### `PUT /nps-json-service/nps/v1/api/accounting/suspend-overpayment/:identifier/:paymentNumber

For this endpoint the selection is driven via the NINO number provided as the `:identifier` URL parameter according to
the below table.

| NINO pattern for SuspendOverpayment API | Scenario                  |
|-----------------------------------------|---------------------------|
| `*****0***`                             | InternalServerError (500) |
| `*********`                             | Happy Path (200)          |

### `POST /risking/claims/:claimId/bank-details`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for GetBankRiskResult API | Scenario                                                                            |
|----------------------------------------|-------------------------------------------------------------------------------------|
| `***1*****`                            | Do Not Pay (200)                                                                    |
| `***2*****`                            | Submission has not passed validation (400)                                          |
| `***3*****`                            | Forbidden (403)                                                                     |
| `***4*****`                            | Resource not found (404)                                                            |
| `***5*****`                            | DES is currently experiencing problems that require live service intervention (500) |
| `***6*****`                            | Dependent systems are currently not responding (503)                                |
| `*********`                            | Pay (Happy Path) (200)                                                              |

### `POST /risking/exceptions/:clientUId`

For this endpoint the selection is driven via the NINO number provided in the request body JSON according to the
below table.

Note: to reach this API call the GetBankRiskResult API must reply with a 'Do Not Pay (200)' response, hence the
required `1` in the fourth value of the NINO.

| NINO pattern for Notify Case Management API | Scenario                                         |
|---------------------------------------------|--------------------------------------------------|
| `***1**1**`                                 | Bad Request (400)                                |
| `***1**2**`                                 | Forbidden (403)                                  |
| `***1**3**`                                 | InternalServerError (500)                        |
| `***1*****`                                 | Happy Path (200)                                 |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
