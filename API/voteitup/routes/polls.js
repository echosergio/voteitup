var express = require('express');
var db = require('../models');
var router = express.Router();

router.get('/', function (req, res) {
    db.Poll.findAll({
        include: [{
            model: db.Choice,
            attributes: ['text', [db.sequelize.fn('COUNT', db.Sequelize.col('Choices->Votes.id')), 'votes']],
            include: [{
                model: db.Vote,
                attributes: []
            }]
        },{
            model: db.Area,
            attributes: ['city', 'country']
        }],
        group: ['Choices.id']
    }).then(polls => {
        res.send(polls);
    })
});

module.exports = router;