var express = require('express');
var jwt = require("jwt-simple");
var auth = require("../passport-auth.js")();
var users = require("../users.js");
var cfg = require("../config.js");
var router = express.Router();

router.post('/token', function (req, res) {
    if (req.body.email && req.body.password) {
        var email = req.body.email;
        var password = req.body.password;

        var user = users.find(function (u) {
            return u.email === email && u.password === password;
        });

        if (user) {
            var jwt_payload = {
                id: user.id
            };
            var token = jwt.encode(jwt_payload, cfg.jwtSecret);
            res.json({
                token: token
            });
        } else {
            res.sendStatus(401);
        }
    } else {
        res.sendStatus(401);
    }
});

module.exports = router;