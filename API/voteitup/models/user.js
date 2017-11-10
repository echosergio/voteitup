"use strict";

module.exports = function (sequelize, DataTypes) {
    var User = sequelize.define('User', {
        username: DataTypes.STRING,
        bio: DataTypes.STRING,
        email: DataTypes.STRING,
        image: DataTypes.STRING,
        bgImage: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                User.hasMany(models.Activity, {});
                User.hasMany(models.Poll, {});
                User.hasOne(models.Auth, {});
            }
        }
    });

    return User;
};