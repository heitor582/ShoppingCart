# Description
This application is a simulation of a shopping cart, allowing users to create multiple carts, list them, empty them, checkout, add items, and remove items, using the following libraries and architecture below:

### Stack
- Java 17
- Spring Boot 3
- Spring Doc
- PostgresSQL
- Spring Web
- Undertow
- JPA
- JUnit 5
- TestContainers ( necessary docker )
- Gradle
- Redis
- Docker and Docker Compose

### Architecture
To build this shopping cart, I utilized an architecture similar to MVC, with services, controllers, and entities. For database access, JPA provides the repository. The flow follows Controller -> Service -> Entity and Repository. I aimed to maintain the responsibilities of entities within themselves, striving to keep the service responsible for receiving information, calling the entity to handle the logic, and saving it to the database.

# Clone
```bash
# Clone github project
$ git clone https://github.com/heitor582/ShoppingCart.git

# Enter the folder
$ cd ./ShoppingCart
```
# Installation
## Running the app with docker
```bash
# Initiate the docker
$ docker-compose up --build -d
```
# Tests
```bash
# All tests
$ ./gradlew test
# Unit tests
$ ./gradlew test --tests '*Test'
# Integration tests
# To Run the integration tests its necessary the docker up
$ ./gradlew test --tests '*IT'
```
# Executing OpenApi/Swagger
In the url after run the program access for enter the swagger and see all the routes with the respective parameters.
 ```bash
 http://localhost:8080/api/swagger-ui/index.html
```
 - `/api/carts`
   - `POST /`-> create a cart
   - `DELETE /{id}`-> close cart by id
   - `GET /`-> get all carts
   - `GET /{id}`-> get cart by id
   - `DELETE /{id}/empty`-> empty a cart removing all his items
   - `PATCH /{id}/add/item`-> add item into a cart by his id
   - `PATCH /{id}/remove/item`-> remove item of a cart by his id

### For the items its only possible to add items of this table
| Product ID | Name    | Price     |
|------------|---------|-----------|
| 1          | T-shirt | USD 12.99 |
| 2          | Jeans   | USD 25.00 |
| 3          | Dress   | USD 20.65 |

# Import archive of Postman
### First what is Postman?
Postman is a program to make, organize and view the result of api requests.
### Running the app
At the root of the project is a json that contains data that the postman program processes and transforms into pre-made requests.

In the postman click import -> Upload Files

![image](https://user-images.githubusercontent.com/58075535/124396541-92e1f900-dce0-11eb-9a0f-68eed8e69eb7.png)
![image](https://user-images.githubusercontent.com/58075535/124396554-9bd2ca80-dce0-11eb-9ceb-69372af6613f.png)


Import the json that is at the root of the project called 'Cart.postman_collection.json'


