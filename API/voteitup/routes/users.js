var express = require('express');
var crypto = require('crypto');
var db = require('../models');
var router = express.Router();

router.get('/', (req, res) => {
    db.User.findAll()
        .then(polls => {
            res.send(polls);
        })
});

router.post('/', (req, res) => {
    db.User.create({
        username: req.body.username,
        bio: req.body.bio,
        email: req.body.email,
        image: req.body.image,
        bgImage: req.body.bgImage,
    }).then(function (user) {
        db.Auth.create({
            token: crypto.createHash('md5').update(req.body.password).digest("hex"),
            userId: user.id
        });
    }).then(() => {
        res.json({
            status: "success"
        });
    }).catch(err =>
        next(err)
    )
});

router.get('/:userId', (req, res) => {
    db.User.findAll({
            where: {
                id: req.params.userId
            }
        })
        .then(polls => {
            res.send(polls);
        })
});

const pollsQuery = {
    include: [{
        model: db.Choice,
        attributes: ['text', [db.sequelize.fn('COUNT', db.Sequelize.col('Choices->Votes.id')), 'votes']],
        include: [{
            model: db.Vote,
            attributes: []
        }]
    }, {
        model: db.Area,
        attributes: ['city', 'country']
    }],
    group: ['Choices.id']
}

router.get('/:userId/polls', (req, res) => {
    pollsQuery.where = {
        UserId: req.params.userId
    }

    db.Poll.findAll(pollsQuery)
        .then(polls => {
            res.send(polls);
        })
});

router.get('/:userId/polls/:pollId', (req, res) => {
    pollsQuery.where = {
        UserId: req.params.userId,
        id: req.params.pollId
    }

    db.Poll.findAll(pollsQuery)
        .then(polls => {
            res.send(polls);
        })
});

router.post('/:userId/polls', (req, res) => {
    db.Poll.create({
        text: req.body.text,
        UserId: req.params.userId
    }).then(poll => {
        db.Area.create({
            city: req.body.area.city,
            country: req.body.area.country,
            PollId: poll.id
        });
        req.body.choices.forEach(choice => {
            db.Choice.create({
                text: choice,
                PollId: poll.id
            });
        });
    }).then(() => {
        res.json({
            status: "success"
        });
    }).catch(err =>
        next(err)
    )
});

module.exports = router;