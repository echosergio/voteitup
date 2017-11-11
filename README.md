# VoteItUp

Plataforma donde los usuarios pueden votar sobre cualquier tema actual. Siendo también red social donde cada usuario tiene su perfil.
Dedicado exclusivamente a votaciones siendo una plataforma de referencia a la hora de realizar encuestas.

## API

https://polar-oasis-43680.herokuapp.com/

## Get auth token  

POST `/api/v1/auth/token`  

Request
```json
{
    "email": "carles@mail.com",
    "password": "1234"
}
```
Response
```json
{
    "token": "eyJ0XAi.eyJpZ0.JVkwEKs"
}
```

## Users

GET `/api/v1/users`  
GET `/api/v1/users/:userId` 

Response
```json
[
    {
        "id": 1,
        "username": "Carles",
        "bio": "Aficionado a la política",
        "email": "carles@mail.com",
        "image": null,
        "bgImage": null,
        "registerDate": "11-11-2017"
    },
    {
        "id": 2,
        "username": "Alfonso",
        "bio": null,
        "email": "alfonso@mail.com",
        "image": null,
        "bgImage": null,
        "registerDate": "11-11-2017"
    }
]
```

POST `/api/v1/users`  

Request
```json
{
    "email": "carles@mail.com",
    "username": "Carles",
    "password": "1234",
    "bio": "Aficionado a la política"
}
```
Response
```json
{
    "status": "success"
}
```

## Polls

GET `/api/v1/polls`  
GET `/api/v1/polls/:pollId`  
GET `/api/v1/polls/:userId` 

Response
```json
[
    {
        "id": 1,
        "text": "#Referendum de independecia #Cataluña",
        "UserId": 1,
        "creationDate": "11-11-2017",
        "Choices": [
            {
                "text": "Sí",
                "votes": 0
            },
            {
                "text": "No",
                "votes": 2
            }
        ],
        "Area": {
            "city": "Barcelona",
            "country": "España"
        }
    },
    {
        "id": 2,
        "text": "#HuelgaDocentes",
        "UserId": 2,
        "creationDate": "11-11-2017",
        "Choices": [
            {
                "text": "Sí",
                "votes": 0
            },
            {
                "text": "No",
                "votes": 0
            }
        ],
        "Area": {
            "city": "Madrid",
            "country": "España"
        }
    }
]
```
 
GET `/api/v1/:pollId/activity`  
GET `/api/v1/:pollId/activity?daysback=3` 

Response
```json
[
    {
        "votes": 11,
        "date": "01-11-2017"
    },
    {
        "votes": 17,
        "date": "02-11-2017"
    },
    {
        "votes": 9,
        "date": "03-11-2017"
    }
]
```

GET `/api/v1/users/:userId/polls`  
GET `/api/v1/users/:userId/polls/:pollId` 

Response
```json
[
    {
        "id": 1,
        "text": "#Referendum de independecia #Cataluña",
        "UserId": 1,
        "creationDate": "11-11-2017",
        "Choices": [
            {
                "text": "Sí",
                "votes": 0
            },
            {
                "text": "No",
                "votes": 2
            }
        ],
        "Area": {
            "city": "Barcelona",
            "country": "España"
        }
    }
]
```

POST `/api/v1/users/:userId/polls`  

Request
```json
{
    "text": "#HuelgaAlumnos",
    "choices": [ "Sí", "No" ],
    "area": {
        "city": "Madrid",
        "country": "España"
    }
}
```
Response
```json
{
    "status": "success"
}
```

GET `/api/v1/users/:userId/activity`  

Response
```json
[
    {
        "id": 1,
        "text": "#Referendum de independecia #Cataluña",
        "choice": "No"
    },
    {
        "id": 2,
        "text": "#HuelgaDocentes",
        "choice": "Sí"
    }
]
```