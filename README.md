
# p800-refunds-stubs

This microservice built using Scala 2.13, Play 3 and MongoDB allows development and testing by providing a simulation
of the required API endpoints for a logged out P800 journey.

---

## Technical  Documentation

### Running the Service Locally

To run the service via SBT, simply run

`sbt run`

This will run the service on port `10151`.

If running in tandem with other microservices in the p800 refunds project, start the service manager
profile `P800_REFUNDS_ALL`. e.g. `sm2 --start P800_REFUNDS_ALL`.

### Java version

This project build on JDK v11.
In particular, this version of java is used when building this service: `OPENJDK_VERSION_11_0_23_0_9`

### Unit tests

Run `sbt test` in a terminal to run the unit tests.

---

## Endpoints Summary

| Path                                                                                            | Method | Description                                        |
|:------------------------------------------------------------------------------------------------|:-------|:---------------------------------------------------|
| /nps/nps-json-service/nps/v1/api/reconciliation/p800/:identifier/:paymentNumber                 | GET    | Check Reference API at NPS                         |
| /nps/nps-json-service/nps/v1/api/individual/trace-individual                                    | POST   | Trace Individual API at NPS                        |
| /nps/nps-json-service/nps/v1/api/accounting/issue-payable-order/:identifier/:paymentNumber      | PUT    | Issue Payable Order API at NPS                     |
| /nps/pay-as-you-earn/individuals/:identifier/repayments/suspension                              | PUT    | Suspend Overpaymant API at NPS                     |
| /nps/pay-as-you-earn/individuals/:identifier/repayments/bacs-payment                            | PUT    | Make Bacs Repayment API at NPS                     |
| /risking/claims/:claimId/bank-details                                                           | POST   | EDH API#1133: Get Bank Details Risk Result v2.1.   |
| /risking/exceptions/:clientUId                                                                  | POST   | Case Management API#1132: Notify Risking Exception |
| /connect/token                                                                                  | POST   | Authenticate with Ecospend and get access token    |
| /api/v2.0/banks                                                                                 | GET    | Retreive a list of banks from Ecospend             |
| /api/v2.0/consents                                                                              | POST   | Create an Ecospend consent                         |
| /api/v2.0/accounts/summary                                                                      | GET    | Retreive account information for a given consent   |

As this service provides a simulation for other existing APIs see the documentation for the relevant APIs via
the [API hub](https://admin.tax.service.gov.uk/api-hub).

And with the Ecospend AIS documentation:

- [AIS](https://docs.ecospend.com/new/guides.html#aisIntro)
- [Access Token](https://docs.ecospend.com/new/references.html?render=consents&url_render=ais-v2)
- [Banks](https://docs.ecospend.com/new/references.html?render=banks&url_render=ais-v2)
- [Consents](https://docs.ecospend.com/new/references.html?render=consents&url_render=ais-v2)
- [Accounts](https://docs.ecospend.com/new/references.html?render=accounts&url_render=ais-v2)

---

## Scenarios

The microservice responds with configured response depending on the incoming request data.

## Example Test Data

### Cheque

| Nino       | Check Reference API (1)                  | Payable Order API (2)      |
|------------|------------------------------------------|----------------------------|
| `AB999999C`| 200 - Happy Path                         | 200 - Happy Path           |
| `AB990999C`| 200 - Happy Path (Optional Fields Empty) | 200 - Happy Path           |
| `AB099999C`| 404 - NINO & Reference don't match       | N/A                        |
| `AB199999C`| 422 - Refund already taken               | N/A                        |
| `AB299999C`| 422 - Unprocessable Entity               | N/A                        |
| `AB399999C`| 400 - BadRequest                         | N/A                        |
| `AB499999C`| 403 - Forbidden                          | N/A                        |
| `AB599999C`| 500 - InternalServerError                | N/A                        |
| `AB909999C`| 200 - Happy Path                         | 422 - Refund already taken |
| `AB979999C`| 200 - Happy Path                         | 500 - InternalServerError  |

### Bank Transfer (Validation checks)

| Nino       | Check Reference API (1)                  | Trace Individual API (1)                 |
|------------|------------------------------------------|------------------------------------------|
| `AB999999C`| 200 - Happy Path                         | 200 - Happy Path                         |
| `AB990999C`| 200 - Happy Path (Optional Fields Empty) | 200 - Happy Path                         |
| `AB991999C`| 200 - Happy Path                         | 200 - Happy Path (Optional Fields Empty) |
| `AB099999C`| 404 - NINO & Reference don't match       | N/A                                      |
| `AB199999C`| 422 - Refund already taken               | N/A                                      |
| `AB299999C`| 422 - Unprocessable Entity               | N/A                                      |
| `AB399999C`| 400 - BadRequest                         | N/A                                      |
| `AB499999C`| 403 - Forbidden                          | N/A                                      |
| `AB599999C`| 500 - InternalServerError                | N/A                                      |
| `AB699999C`| 200 - Happy Path                         | 404 - Not Found                          |
| `AB799999C`| 200 - Happy Path                         | 400 - BadRequest                         |
| `AB899999C`| 200 - Happy Path                         | 500 - InternalServerError                |

### Bank Transfer (Valid Account)

| Nino        | Check Reference API (1)                  | Trace Individual API (1)                 | Get Bank Risk Result API (2) | Claim Overpayment API (6) |
|-------------|------------------------------------------|------------------------------------------|------------------------------|---------------------------|
| `AB999999C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 200 - Happy Path          |
| `AB990999C` | 200 - Happy Path (Optional Fields Empty) | 200 - Happy Path                         | 200 - Happy Path             | 200 - Happy Path          |
| `AB991999C` | 200 - Happy Path                         | 200 - Happy Path (Optional Fields Empty) | 400 - BadRequest             | N/A                       |
| `AB929999C` | 200 - Happy Path                         | 200 - Happy Path                         | 400 - BadRequest             | N/A                       |
| `AB939999C` | 200 - Happy Path                         | 200 - Happy Path                         | 403 - Forbidden              | N/A                       |
| `AB949999C` | 200 - Happy Path                         | 200 - Happy Path                         | 404 - Not Found              | N/A                       |
| `AB959999C` | 200 - Happy Path                         | 200 - Happy Path                         | 500 - DES issues             | N/A                       |
| `AB969999C` | 200 - Happy Path                         | 200 - Happy Path                         | 503 - Systems Not Responding | N/A                       |
| `AB999991C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 400 - BadRequest          |
| `AB999992C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 403 - Forbidden           |
| `AB999993C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 500 - InternalServerError |
| `AB999994C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 422 - Refund Already Taken|
| `AB999995C` | 200 - Happy Path                         | 200 - Happy Path                         | 200 - Happy Path             | 422 - Suspended           |

### Bank Transfer (Fails Security Checks)

| Nino        | Check Reference API (1)                  | Trace Individual API (1)  | Get Bank Risk Result API (2) | Name-Matching | Case Management API (5)   | Suspend Overpayment API (4) |
|-------------|------------------------------------------|---------------------------|------------------------------|---------------|---------------------------|-----------------------------|
| `AB919999C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 200 - Happy Path          | 200 - Happy Path            |
| `NN999999C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Happy Path             | Fail          | N/A                       | N/A                         |
| `AB910999C` | 200 - Happy Path (Optional Fields Empty) | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 200 - Happy Path          | 200 - Happy Path            |
| `AB919919C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 400 - BadRequest          | N/A                         |
| `AB919929C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 403 - Forbidden           | N/A                         |
| `AB919939C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 500 - InternalServerError | N/A                         |
| `AB919099C` | 200 - Happy Path                         | 200 - Happy Path          | 200 - Do Not Pay             | Pass          | 200 - Happy Path          | 500 - InternalServerError   |

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
| `***0*****`                         | Happy path (200) (Optional fields empty)    |
| `*********`                         | Happy path (200)                            |

### `POST /nps-json-service/nps/v1/api/individual/trace-individual?exactMatch=true&returnRealName=true`

For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario                                 |
|-------------------------------------|------------------------------------------|
| `**6******`                         | NotFound (404)                           |
| `**7******`                         | BadRequest (400)                         |
| `**8******`                         | InternalServerError (500)                |
| `**9******`                         | Happy Path (200)                         |
| `****1****`                         | Happy Path (200) (Optional fields empty) |

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

| NINO pattern for MakeBacsRepayment API | Scenario                                         |
|----------------------------------------|--------------------------------------------------|
| `*******1*`                            | Bad Request (400)                                |
| `*******2*`                            | Forbidden (403)                                  |
| `*******3*`                            | InternalServerError (500)                        |
| `*******4*`                            | RefundAlreadyTaken - Unprocessable Entity (422)  |
| `*******5*`                            | Suspended - Unprocessable Entity (422)           |
| `*********`                            | Happy Path (200)                                 |

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
