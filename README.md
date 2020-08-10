# Web Quiz Engine

A simple engine for creating and solving quizzes through REST API.

It uses an embedded H2 database to store all data in the file system.

## Running the application

- Building app
```
gradlew build
```

- Running app
```
java -jar build/libs/*.jar
```

By default, it runs on the port `8889` and works with the `quizdb` database in the project's directory.

## Description

The service API supports creating, getting, deleting and solving quizzes.
Each quiz has an id, title, text, possible options. Some of the options are correct.

## Operations and their results

To perform any actions with quizzes a user has to be registered and then authorized via HTTP Basic Auth.
Otherwise, the service returns the `HTTP 401 (Unauthorized)`.

---

### Register a new user

To register a new user, you need to send a JSON with `email` and `password` via `POST` request.

```
curl -X POST -H "Content-Type: application/json" http://localhost:8889/api/register \
-d '{"email":"test@gmail.com", "password": "secret"}'
```

The service returns `HTTP 200 (OK)`, if the registration has been completed successfully.

If the email is already taken by another user, the service will return `HTTP 400 (Bad Request)`.

---

### Create a new quiz

To create a new quiz, you need to send a JSON via `POST` request with the following keys: 
- `title`: string;
- `text`: string;
- `options`: an array of strings (should contain at least 2 items); 
- `answer`: an array of indexes of correct options.

```
curl --user test@gmail.com:secret -X POST -H "Content-Type: application/json" -d '{"title":"The Java Logo", "text":"What is depicted on the Java logo?", "options": ["Robot", "Tea leaf", "Cup of coffee", "Bug"], "answer": [2]}' http://localhost:8889/api/quizzes
```

The response contains JSON with generated `id`.

If the request JSON does not contain `title` or `text`, or number of options in the quiz is less than 2, then the response is `HTTP 404 (Not Found)`.

---

### Get a quiz

To get an info about a quiz, you need to specify its `id` in url.

```
curl --user test@gmail.com:secret -X GET http://localhost:8889/api/quizzes/1
```

If the quiz does not exist, the server returns `HTTP 404 (Not Found)`.

---

### Get all quizzes

Obtaining all quizzes is performed page by page: 10 quizzes at once.

```
curl --user test@gmail.com:secret -X GET http://localhost:8889/api/quizzes
```

The response contains a JSON with quizzes (inside `content`) and some additional metadata.

We can pass the `page` param to navigate through pages `/api/quizzes?page=1`. 
Pages start from 0 (the first page).

If there is no quizzes, `content` is empty.

In all cases, the status code is `HTTP 200 (OK)`.

---

### Solve a quiz

To solve a quiz, you need to pass an answer JSON with key `answer` and options indexes via `POST` request.

```
curl --user test@gmail.com:secret -X POST -H 'Content-Type: application/json' http://localhost:8889/api/quizzes/1/solve -d '{"answer": [2]}'
```

It is also possible to send an empty array of options because some quizzes may not have correct options.

The result is determined by the value of the boolean `success` key in the JSON response.

If the specified quiz does not exist, the server returns `HTTP 404 (Not Found)`.

---

### Get all completions of quizzes

The API provides an operation to get all completions of quizzes for a user.
A response is separated by pages, since the service may return a lot of data.

```
curl --user test@gmail.com:secret -X GET  http://localhost:8889/api/quizzes/completed
```

The response contains a JSON with quizzes (inside `content`) and some additional metadata.

Since it is allowed to solve a quiz multiple times, the response may contain duplicate quizzes, 
but with different completion date.

---

### Delete a quiz

It is possible to delete a quiz, but this can only be done by its creator.

```
curl --user test@gmail.com:secret -X DELETE  http://localhost:8889/api/quizzes/1
```

If the operation was successful, the service returns `HTTP 204 (No Content)`.

If the specified quiz does not exist, the server returns `HTTP 404 (Not Found)`.
If the specified user is not the creator of this quiz, the response contains `HTTP 403 (Forbidden)`.
