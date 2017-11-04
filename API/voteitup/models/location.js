"use strict";

module.exports = function (sequelize, DataTypes) {
    var PollLocation = sequelize.define('PollLocation', {
        city: DataTypes.STRING,
        country: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                PollLocation.belongsTo(models.Poll, {
                    as: 'poll'
                });
            }
        }
    });

    return PollLocation;
};