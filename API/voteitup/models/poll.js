"use strict";

module.exports = function (sequelize, DataTypes) {
    var Poll = sequelize.define('Poll', {
        text: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Poll.hasMany(models.Choice, {});
                Poll.hasOne(models.Area, {});
            }
        }
    });

    return Poll;
};