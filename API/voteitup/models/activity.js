"use strict";

module.exports = function (sequelize, DataTypes) {
    var Activity = sequelize.define('Activity', {
        date: DataTypes.DATE,
        type: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Activity.belongsTo(models.User, {});
            }
        }
    });

    return Activity;
};