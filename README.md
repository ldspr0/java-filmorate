# java-filmorate
Template repository for Filmorate project.

# Data Model:
![Database diagram](/db_diagram.png)

# Rest API Description:
<details open>
<summary>Film Endpoints</summary>

```
Method: GET
URI: /films
Response: 200
Description: Endpoint to get all films
```
```
Method: POST
Input Body: Film object in JSON
URI: /films
Response: 201
Description: Endpoint to create film object
```
```
Method: PUT
Input Body: Film object in JSON with correct id
URI: /films
Response: 200
Description: Endpoint to update film object
```
```
Method: PUT
URI: /films/{id}/like/{userId}
Response: 200
Description: Endpoint to add "like" to the film
```
```
Method: DELETE
URI: /films/{id}/like/{userId}
Response: 200
Description: Endpoint to remove likes from the film
```
```
Method: GET
Input: count is optional, default is 10
URI: /films/popular?count={intValue}
Response: 200
Description: Endpoint to get most popular films from best to worst
```
</details>

<details open>
<summary>User Endpoints</summary>

```
Method: GET
URI: /users
Response: 200
Description: Endpoint to get all users
```
```
Method: POST
Input Body: User object in JSON
URI: /users
Response: 201
Description: Endpoint to create user object
```
```
Method: PUT
Input Body: User object in JSON with correct id
URI: /users
Response: 200
Description: Endpoint to update user object
```
```
Method: PUT
URI: /users/{id}/friends/{friendId}
Response: 200
Description: Endpoint to add friend to the user
```
```
Method: DELETE
URI: /users/{id}/friends/{friendId}
Response: 200
Description: Endpoint to remove friend from the user
```
```
Method: GET
URI: /users/{id}/friends
Response: 200
Description: Endpoint to get all friends of the user
```
```
Method: GET
URI: /users/{id}/friends/common/{otherId}
Response: 200
Description: Endpoint to get all common friends between 2 users
```
</details>


