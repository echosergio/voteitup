# VoteItUp

Plataforma donde los usuarios pueden votar sobre cualquier tema actual. Siendo también red social donde cada usuario tiene su perfil.
Dedicado exclusivamente a votaciones siendo una plataforma de referencia a la hora de realizar encuestas.


## API

### Get auth token  

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

### Users

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
        "bgImage": null
    },
    {
        "id": 2,
        "username": "Alfonso",
        "bio": null,
        "email": "alfonso@mail.com",
        "image": null,
        "bgImage": null
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

### Polls

GET `/api/v1/polls`  
GET `/api/v1/polls/:userId` 

Response
```json
[
    {
        "id": 1,
        "text": "#Referendum de independecia #Cataluña",
        "UserId": 1,
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

GET `/api/v1/users/:userId/polls`  
GET `/api/v1/users/:userId/polls/:pollId` 

Response
```json
[
    {
        "id": 1,
        "text": "#Referendum de independecia #Cataluña",
        "UserId": 1,
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

