# User API Spec

## Register User

Endpoint : POST /api/users

Request Body :

```json
{
  "username" : "ibnu",
  "password" : "18nu",
  "name" : "Ibnu Rabbani"
}
```

Response Body (Success) : 
```json
{
  "data" : "OK"
}
```

Response Body (Failed) :
```json
{
  "errors" : "Username must not blank, ???"
}
```

## Login User

Endpoint : POST /api/auth/login

Request Body :

```json
{
  "username" : "ibnu",
  "password" : "18nu"
}
```

Response Body (Success) :
```json
{
  "data" : {
    "token" : "TOKEN",
    "expiredAt" : 438436988948 //milliseconds
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors" : "Username or Password wrong"
}
```

## Get User

Endpoint : GET /api/users/current

Request Header : 

- X-API-TOKEN : TOKEN(Mandatory)

Response Body (Success) :
```json
{
  "data" : {
    "username" : "ibnu",
    "name" : "M Ibnu Rabbani"
  }
}
```

Response Body (Failed, 401) :
```json
{
  "errors" : "Unautorized"
}
```
## Update User

Endpoint : PATCH /api/users/current

Request Header : 

- X-API-TOKEN : TOKEN(Mandatory)

Request Body : 

```json
{
  "name" : "Ibnu Rabbani", // put if only want to update name
  "password" : "new password" // put if only want to update password
}
```

Response Body (Success) :
```json
{
  "data" : {
    "username" : "ibnu",
    "name" : "M Ibnu Rabbani"
  }
}
```

Response Body (Failed) :
```json
{
  "errors" : "Unautorized"
}
```

## Logout User

Endpoint : DELETE /api/auth/logout

Request Header :

- X-API-TOKEN : TOKEN(Mandatory)

Response Body (Success) :
```json
{
  "data" : "OK"
}
```