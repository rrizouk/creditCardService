# credit-card-processing-service

## Technology stack
- Java8
- SpringBoot

### Architecture
- The service is a RESTFUL stateless service and can be used in a distributed architecture such as micro service.

## Building the app
- run maven command mvn clean install -U

### Running the app
- Application.java is the entry point which will start Spring Boot app
- use Postman or some other Http client to make REST calls

## endpoints

  URL: /credit-card/add
  METHOD: POST
  PAYLOAD: {"customerName": "RYAD","cardNumber": "79927398713", "limit": "999"}
  Response: HTTP STATUS: 201

   URL: /credit-card/charge
   METHOD: POST
   PAYLOAD: {"customerName": "RYAD", "cardNumber": "79927398713", "amount": "900"}
   Response: HTTP STATUS: 200 with payload {"cardNumber":"79927398713","balance":"B#900.00"}

   running this again will return an error about exceeding the balance limit
   Response: HTTP STATUS: 500

   URL: /credit-card/credit
   METHOD: POST
   PAYLOAD: {"customerName": "RYAD", "cardNumber": "79927398713", "amount": "900"}
   Response: HTTP STATUS: 200 with payload {"cardNumber":"79927398713","balance":"B#0.00"}

   running this again will keep increasing the negative balance e.g.
   {"cardNumber":"79927398713","balance":"B#-900.00"}


Notes:
  - more test coverage is needed namely integration tests to avoid having to start the app to ensure that the whole thing is working e.g. (cucumber)
  - end to end test suite should also be added e.g. Selenium, serenity or just testing with REST calls
  - a lot of scope for refactoring especially the controller which is too big,
  - the DB schema is just an illustration, the real world will have many more tables