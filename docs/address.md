# Address API Spec

## Create Address

Endpoint : POST /api/contacts/{idContact}/addresses

Request Header : 

- X-API-TOKEN : TOKEN(Mandatory)

Request Body : 

```json
{
  "street" : "Jalan Jalan",
  "province" : "Jakarta Selatan",
  "city" : "Jakarta",
  "country" : "Indonesia",
  "postalCode" : "00001"
}
```

Response Body (Success) :

```json
{
  "data" : {
    "id" : "random-string",
    "street" : "Jalan Jalan",
    "province" : "Jakarta Selatan",
    "city" : "Jakarta",
    "country" : "Indonesia",
    "postalCode" : "00001"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Contact is not found"
}
```

## Update Address

Endpoint : PUT /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : TOKEN(Mandatory)

Request Body :

```json
{
  "street" : "Jalan Kemana",
  "province" : "Jakarta Selatan",
  "city" : "Jakarta",
  "country" : "Indonesia",
  "postalCode" : "00001"
}
```

Response Body (Success) :

```json
{
  "data" : {
    "street" : "Jalan Kemana",
    "province" : "Jakarta Selatan",
    "city" : "Jakarta",
    "country" : "Indonesia",
    "postalCode" : "00001"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Address is not found"
}
```

## Get Address

Endpoint : GET /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : TOKEN(Mandatory)

Response Body (Success) :

```json
{
  "data" : {
    "street" : "Jalan Kemana",
    "province" : "Jakarta Selatan",
    "city" : "Jakarta",
    "country" : "Indonesia",
    "postalCode" : "00001"
  }
}
```

Response Body (Failed) :

```json
{
  "errors" : "Address is not found"
}
```

## Remove Address

Endpoint : DELETE /api/contacts/{idContact}/addresses/{idAddress}

Request Header :

- X-API-TOKEN : TOKEN(Mandatory)

Response Body (Success) :

```json
{
  "data" : "OK"
}
```

Response Body (Failed) :

```json
{
  "errors" : "Address is not found"
}
```

## List Address

Endpoint : GET /api/contacts/{idContact}/addresses

Request Header :

- X-API-TOKEN : TOKEN(Mandatory)

Response Body (Success) :

```json
{
  "data" : []
}
```

Response Body (Failed) :

```json
{
  "errors" : "Contact is not found"
}
```