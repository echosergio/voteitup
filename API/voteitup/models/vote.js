"use strict";

module.exports = function (sequelize, DataTypes) {
    var Vote = sequelize.define('Vote', {
        date: DataTypes.DATE
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Vote.belongsTo(models.User, {});
            }
        }
    });

    return Vote;
};