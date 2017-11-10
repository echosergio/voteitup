"use strict";

module.exports = function (sequelize, DataTypes) {
    var Area = sequelize.define('Area', {
        city: DataTypes.STRING,
        country: DataTypes.STRING
    }, {
        timestamps: false,
        classMethods: {
            associate: function (models) {
                Area.belongsTo(models.Poll, {});
            }
        }
    });

    return Area;
};