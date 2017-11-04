"use strict";

module.exports = function (sequelize, DataTypes) {
    var Auth = sequelize.define('Auth', {
        token: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Auth.belongsTo(models.User, {
                    as: 'user'
                });
            }
        }
    });

    return Auth;
};