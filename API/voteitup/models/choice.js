"use strict";

module.exports = function (sequelize, DataTypes) {
    var Choice = sequelize.define('Choice', {
        text: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Choice.hasMany(models.Vote, {
                    as: 'votes'
                });
            }
        }
    });

    return Choice;
};