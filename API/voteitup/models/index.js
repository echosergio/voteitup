var fs = require('fs'),
    path = require('path'),
    Sequelize = require('sequelize'),
    sequelize = null,
    db = {};

var basename = path.basename(module.filename);

sequelize = new Sequelize('voteitup', null, null, {
    dialect: 'sqlite',
    storage: './db/core.sqlite'
});

fs.readdirSync(__dirname)
    .filter(function (file) {
        return (file.indexOf('.') !== 0) && (file !== basename);
    })
    .forEach(function (file) {
        var model = sequelize.import(path.join(__dirname, file));
        if (model instanceof Array) {
            model.forEach(function (m) {
                db[m.name] = m;
            });
        } else {
            db[model.name] = model;
        }
    });

Object.keys(db).forEach(function (modelName) {
    if ('associate' in db[modelName]) {
        db[modelName].associate(db);
    }
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

module.exports = db;