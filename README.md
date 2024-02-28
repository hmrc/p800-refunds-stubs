
# p800-refunds-stubs

## Scenarios
The microservice responds with configured response depending on the incoming request data.

### Example Test Data

| Nino pattern | Behaviour of Check Reference API            | Behaviour of Trace Individual API | Behaviour of Issue Payable Order API |
|--------------|---------------------------------------------|-----------------------------------|--------------------------------------|
| `AB999999C`  | 200 - Happy path                            | 200 - Happy path                  | 200 - Happy path                     |
| `AB099999C`  | 404 - Nino And P800Reference aren't matched | N/A                               | N/A                                  |
| `AB199999C`  | 422 - Refund is already taken               | N/A                               | N/A                                  |
| `AB299999C`  | 422 - Unprocessable Entity                  | N/A                               | N/A                                  |
| `AB399999C`  | 400 - BadRequest                            | N/A                               | N/A                                  |
| `AB499999C`  | 403 - Forbidden                             | N/A                               | N/A                                  |
| `AB599999C`  | 500 - InternalServerError                   | N/A                               | N/A                                  |
| `AB699999C`  | 200 - Happy path                            | 404 - Not Found                   | N/A                                  |
| `AB799999C`  | 200 - Happy path                            | 400 - BadRequest                  | N/A                                  |
| `AB899999C`  | 200 - Happy path                            | 500 - InternalServerError         | N/A                                  |
| `AB899999C`  | 200 - Happy path                            | 500 - InternalServerError         | N/A                                  |
| `AB991999C`  | 200 - Happy path                            | 200 - Happy path                  | 422 - RefundAlreadyTaken             |


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
| `*********`                         | Happy Path (200)    |

### `PUT /nps-json-service/nps/v1/api/accounting/issue-payable-order/:identifier/:paymentNumber`
For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario            |
|-------------------------------------|---------------------|
| `***0*****`                         | RefundAlreadyTaken  |
| `*********`                         | Happy Path (200)    |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").