var express = require('express');
var jwt = require("jwt-simple");
var crypto = require('crypto');
var auth = require("../passport-auth.js")();
var db = require('../models');
var cfg = require("../config.js");
var router = express.Router();

router.post('/token', function (req, res) {
    if (req.body.email && req.body.password) {
        var email = req.body.email;
        var password = req.body.password;

        db.User.find({
            include: [{
                model: db.Auth
            }],
            where: {
                email: email
            }
        }).then(user => {
            if (user && user.Auth.token === crypto.createHash('md5').update(password).digest("hex")) {
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
        })
    } else {
        res.sendStatus(401);
    }
});

module.exports = router;