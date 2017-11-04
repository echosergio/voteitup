var express = require('express');
var db = require('../models');
var router = express.Router();

router.get('/', function (req, res) {
    db.Poll.findAll().then(users => {
        console.log(users)
      })
      res.send({});
});


module.exports = router;