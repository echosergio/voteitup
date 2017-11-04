var express = require('express');
var router = express.Router();

router.get('/', (req, res) =>
    res.json([
        {
            "id": 1,
            "user_id": 1,
            "text": "#Referendum de independencia #Cataluña",
            "options": [
                {
                    "name": "Si",
                    "votes": "678452"
                },
                {
                    "name": "No",
                    "votes": "3286423"
                }
            ],
            "location": {
                "country": "Spain",
                "city": "Madrid"
            }
        },
        {
            "id": 2,
            "user_id": 1,
            "text": "#HuelgaDocentes",
            "options": [
                {
                    "name": "Si",
                    "votes": "73627"
                },
                {
                    "name": "No",
                    "votes": "2736"
                }
            ],
            "location": {
                "country": "Spain",
                "city": "Madrid"
            }
        }
    ])
);

module.exports = router;