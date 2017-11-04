var fs = require('fs'),
    path = require('path'),
    Sequelize = require('sequelize'),
    sequelize = null,
    db = {};

var basename = path.basename(module.filename);

const CLASSMETHODS = 'classMethods';
const ASSOCIATE = 'associate';

sequelize = new Sequelize('voteitup', null, null, {
    dialect: 'sqlite',
    storage: './db/core.sqlite'
});

fs
    .readdirSync(__dirname)
    .filter(function (file) {
        return (file.indexOf('.') !== 0) && (file !== basename);
    })
    .forEach(function (file) {
        var model = sequelize.import(path.join(__dirname, file));
        db[model.name] = model;
    });

Object.keys(db).forEach(function (modelName) {
    if (CLASSMETHODS in db[modelName].options) {
        if (ASSOCIATE in db[modelName].options[CLASSMETHODS]) {
            db[modelName].options.classMethods.associate(db);
        }
    }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;