# Auth API Spec

## Register

Endpoint : POST /api/v1/auth/register

Request Body :

```json
{
  "name" : "sita nuria",
  "email" : "mirona2208@scubalm.com",
  "password" : "qwerty123"
}
```

Response Body (Success) :
```json
{
  "success": true,
  "message": "Please check yout email at afdulrohmat03@gmail.com to activate yout account",
  "data" : {
    "name": "name",
    "email": "nameExample@gmail.com"
  }
}
```

## Activation Email

Endpoint : POST /api/v1/auth/verify-email

Request Body :

```json
{
  "activation_code" : 445855,
  "email" : "nameExample@gmail.com"
}
```

Response Body (Success) :
```json
{
  "success": true,
  "message": "User successfully activated. Please login then",
  "data" : null
}
```

Response Body (Failed) :
```json
{
  "success": false,
  "message": "Invalid activation code",
  "data" :null
}
```

## Resend Code Activation

Endpoint : POST /api/v1/auth/resend-activation-code

Request Body :

```json
{
  "email" : "nameExample@gmail.com"
}
```

Response Body (Success) :
```json
{
  "success": true,
  "message": "The new activation code has been successfully sent to your email ",
  "data" : null
}
```

Response Body (Failed) :
```json
{
  "success": false,
  "message": "Error",
  "data" :null
}
```

## Login

Endpoint : POST /api/v1/auth/login

Request Body :

```json
{
  "email" : "afdulrohmat03@gmail.com",
  "password" : "qwerty123"
}
```

Response Body (Success) :
```json
{
  "success": true,
  "message": "User succesfully activated. Please login then",
  "data" : {
    "user": {
      "_id": "656536aaf96f3599ac26547e",
      "name": "afdul rohmat",
      "email": "afdulrohmat03@gmail.com",
      "password": "$2a$10$x9xMF25om9p7OETbThynFejkfmVMcoOLVaH3atlztAV5mJGoNCdH2",
      "role": "user",
      "isVerified": "false",
      "courses": [],
      "createdAt": "2023-11-28T00:39:06.538Z",
      "updatedAt": "2023-11-28T00:39:06.538Z",
      "__v": 0
    },
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1NjUzNmFhZjk2ZjM1OTlhYzI2NTQ3ZSIsImlhdCI6MTcwMTEzMjAzMiwiZXhwIjoxNzAxMTMyMzMyfQ.GBSZ0XGPcOzevuxm_aWIcbFf4p1R_dxnQmT30VCCFJc"
  }
}
```

Response Body (Failed) :
```json
{
  "success": false,
  "message": "Invalid email or password",
  "data" : null
}
```

## Refresh Token

Endpoint : GET /api/v1/auth/refresh-token

Request Body :

Response Body (Success) :
```json
{
  "success": true,
  "message": "User succesfully activated. Please login then",
  "data" : {
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjY1NjUzNmFhZjk2ZjM1OTlhYzI2NTQ3ZSIsImlhdCI6MTcwMTEzMjMxOSwiZXhwIjoxNzAxMTMyNjE5fQ.R2X5UTbEDn-pqEdHw2KYcw0ZcDHCMFynpKzfD-Y-Ruk"
  }
}
```


## Logout

Endpoint : GET /api/v1/auth/logout

Response Body (Success) :
```json
{
  "success": false,
  "message": "Logged out successfully",
  "data" : null
}
```
