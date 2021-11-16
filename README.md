# Employees
Basic REST demo project. Provides a basic SpringBoot web app which exposes REST API
Application is working on default port 8080

Can be run under any IDE or compiled to .jar file.

## Tests
Some test are also provided.

## Endpoint
Application exposes endpoint *employees* with the following operations:
- > curl --request GET --url http://localhost:8080/employees
- > curl --request GET --url http://localhost:8080/employees/{id}
- > curl --request POST \
  --url http://localhost:8080/employees \
  --header 'Content-Type: application/json' \
  --data '{
  "id":null,
  "name":"Jan",
  "surname":"Malinowski",
  "grade":"private",
  "salary":{
  "amount":"12.21",
  "currency":"PLN"
  }
  }
- > curl --request PUT \
  --url http://localhost:8080/employees/cb944135-52f3-44e6-b3 \
  --header 'Content-Type: application/json' \
  --data '{rovided
  "id":"cb944135-52f3-44e6-b3",
  "name":"Jan",
  "surname":"Kods",
  "grade":"sergant",
  "salary":{
  "amount":"12.21",
  "currency":"PLN"
  }
  }
- > curl --request DELETE \
  --url http://localhost:8080/employees/81a38070-9175-4b2c-8e1a-20dbbff4f217
## Error handling
Basic error handling with descriptive messages provided