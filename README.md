
# p800-refunds-stubs

## Scenarios
The microservice responds with configured response depending on the incoming request data.

### Example Test Data

| Nino pattern | Behaviour of Check Reference API            |
|--------------|---------------------------------------------|
| `AB999999C`  | 200 - Happy path                            |
| `AB099999C`  | 404 - Nino And P800Reference aren't matched |
| `AB199999C`  | 422 - Refund is already taken               |
| `AB299999C`  | 422 - Unprocessable Entity                  |
| `AB399999C`  | 400 - BadRequest                            |
| `AB499999C`  | 403 - Forbidden                             |
| `AB599999C`  | 500 - InternalServerError                   |


### `GET /nps-json-service/nps/v1/api/reconciliation/p800/:identifier/:paymentNumber`
For this endpoint the selection is driven via the NINO number using according to below table.

| Nino pattern for CheckReference API | Scenario                                  |
|-------------------------------------|-------------------------------------------|
| `**0******`                         | Nino And P800Reference aren't matched     |
| `**1******`                         | Refund is already taken                   |
| `**2******`                         | Unprocessed Entity                        |
| `**3******`                         | BadRequest                                |
| `**4******`                         | Forbidden                                 |
| `**5******`                         | InternalServerError                       |
| `*********`                         | Happy path                                |

### License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").