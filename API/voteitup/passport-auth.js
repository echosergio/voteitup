var passport = require("passport");
var JwtStrategy = require('passport-jwt').Strategy,
    ExtractJwt = require('passport-jwt').ExtractJwt;
var users = require("./users.js");
var cfg = require("./config.js");

var opts = {
    secretOrKey: cfg.jwtSecret,
    jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken()
};

module.exports = function () {
    passport.use(new JwtStrategy(opts, function (jwt_payload, done) {
        var user = users[jwt_payload.id] || null;
        if (user) {
            return done(null, {
                id: user.id
            });
        } else {
            return done(new Error("User not found"), null);
        }
    }));

    return {
        initialize: function () {
            return passport.initialize();
        },
        authenticate: function () {
            return passport.authenticate('jwt', cfg.jwtSession);
        }
    };
};