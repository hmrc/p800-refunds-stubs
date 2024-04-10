
# p800-refunds-stubs

## Scenarios

The microservice responds with configured response depending on the incoming request data.

## Example Test Data

### Cheque

| Nino       | Check Reference API (1)            | Payable Order API (2)      |
|------------|------------------------------------|----------------------------|
| `AB999999C`| 200 - Happy Path                   | 200 - Happy Path           |
| `AB099999C`| 404 - NINO & Reference don't match | N/A                        |          
| `AB199999C`| 422 - Refund already taken         | N/A                        |               
| `AB299999C`| 422 - Unprocessable Entity         | N/A                        |          
| `AB399999C`| 400 - BadRequest                   | N/A                        |          
| `AB499999C`| 403 - Forbidden                    | N/A                        |     
| `AB599999C`| 500 - InternalServerError          | N/A                        |
| `AB909999C`| 200 - Happy Path                   | 422 - Refund already taken |
| `AB979999C` | 200 - Happy Path                   | 500 - InternalServerError  |

### Bank Transfer (Validation checks)

| Nino       | Check Reference API (1)            | Trace Individual API (1)  |
|------------|------------------------------------|---------------------------|
| `AB999999C`| 200 - Happy Path                   | 200 - Happy Path          |
| `AB099999C`| 404 - NINO & Reference don't match | N/A                       |
| `AB199999C`| 422 - Refund already taken         | N/A                       |           
| `AB299999C`| 422 - Unprocessable Entity         | N/A                       |    
| `AB399999C`| 400 - BadRequest                   | N/A                       | 
| `AB499999C`| 403 - Forbidden                    | N/A                       |  
| `AB599999C`| 500 - InternalServerError          | N/A                       |
| `AB699999C`| 200 - Happy Path                   | 404 - Not Found           |
| `AB799999C`| 200 - Happy Path                   | 400 - BadRequest          |
| `AB899999C`| 200 - Happy Path                   | 400 - InternalServerError |

### Bank Transfer (Valid Account)

| Nino       | Check Reference API (1) | Trace Individual API (1)  | Get Bank Risk Result API (2) | Claim Overpayment API (6) |
|------------|------------------------ |---------------------------|------------------------------|---------------------------|
| `AB999999C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 200 - Happy Path          |
| `AB929999C`| 200 - Happy Path        | 200 - Happy Path          | 400 - BadRequest             | N/A                       |
| `AB939999C`| 200 - Happy Path        | 200 - Happy Path          | 403 - Forbidden              | N/A                       |
| `AB949999C`| 200 - Happy Path        | 200 - Happy Path          | 403 - Not Found              | N/A                       |
| `AB959999C`| 200 - Happy Path        | 200 - Happy Path          | 500 - DES issues             | N/A                       |
| `AB969999C`| 200 - Happy Path        | 200 - Happy Path          | 503 - Systems Not Responding | N/A                       |
| `AB999991C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 400 - BadRequest          |
| `AB999992C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 403 - Forbidden           |
| `AB999993C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 500 - InternalServerError |
| `AB999994C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 422 - Refund Already Taken|
| `AB999995C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Happy Path             | 422 - Suspended           |

### Bank Transfer (Fails HMRC Fraud Check)

| Nino       | Check Reference API (1) | Trace Individual API (1)  | Get Bank Risk Result API (2) | Case Management API (5)   | Suspend Overpayment API (4) | 
|------------|------------------------ |---------------------------|------------------------------|---------------------------|-----------------------------|
| `AB919999C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Do Not Pay             | 200 - Happy Path          | 200 - Happy Path            |
| `AB919919C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Do Not Pay             | 400 - BadRequest          | N/A                         |
| `AB919929C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Do Not Pay             | 403 - Forbidden           | N/A                         |
| `AB919939C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Do Not Pay             | 500 - InternalServerError | N/A                         |
| `AB919099C`| 200 - Happy Path        | 200 - Happy Path          | 200 - Do Not Pay             | 200 - Happy Path          | 500 - InternalServerError   |

## Details per API

### `GET /nps-json-service/nps/v1/api/reconciliation/p800/:identifier/:paymentNumber`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario                                    |
|-------------------------------------|---------------------------------------------|
| `**0******`                         | Nino And P800Reference aren't matched (404) |
| `**1******`                         | Refund is already taken (422)               |
| `**2******`                         | Unprocessed Entity (422)                    |
| `**3******`                         | BadRequest (400)                            |
| `**4******`                         | Forbidden (403)                             |
| `**5******`                         | InternalServerError (500)                   |
| `*********`                         | Happy path (200)                            |

### `POST /nps-json-service/nps/v1/api/individual/trace-individual?exactMatch=true&returnRealName=true`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario                  |
|-------------------------------------|---------------------------|
| `**6******`                         | NotFound (404)            |
| `**7******`                         | BadRequest (400)          |
| `**8******`                         | InternalServerError (500) |
| `**9******`                         | Happy Path (200)          |

### `PUT /nps-json-service/nps/v1/api/accounting/issue-payable-order/:identifier/:paymentNumber`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for IssuePayableOrder API   | Scenario                  |
|------------------------------------------|---------------------------|
| `***0*****`                              | RefundAlreadyTaken (422)  |
| `***7*****`                              | InternalServerError (500) |
| `*********`                              | Happy Path (200)          |

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

### `POST /risking/exceptions/:clientUId`

For this endpoint the selection is driven via the NINO number provided in the request body JSON according to the
below table.

Note: to reach this API call the GetBankRiskResult API must reply with a 'Do Not Pay (200)' response.

| NINO pattern for Notify Case Management API | Scenario                                         |
|---------------------------------------------|--------------------------------------------------|
| `******1**`                                 | Bad Request (400)                                |
| `******2**`                                 | Forbidden (403)                                  |
| `******3**`                                 | InternalServerError (500)                        |
| `*********`                                 | Happy Path (200)                                 |

### `PUT /nps-json-service/nps/v1/api/accounting/suspend-overpayment/:identifier/:paymentNumber

For this endpoint the selection is driven via the NINO number provided as the `:identifier` URL parameter according to
the below table.

| NINO pattern for SuspendOverpayment API | Scenario                  |
|-----------------------------------------|---------------------------|
| `*****0***`                             | InternalServerError (500) |
| `*********`                             | Happy Path (200)          |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
