var express = require('express');
var auth = require("../passport-auth.js")();
var router = express.Router();

var authRoutes = require('./auth');
var surveysRoutes = require('./surveys');
var usersRoutes = require('./users');

router.get('/status', (req, res) =>
  res.json({
    status: "ok"
  })
);

router.use('/auth', authRoutes);
router.use('/surveys', auth.authenticate(), surveysRoutes);
router.use('/users', auth.authenticate(), usersRoutes);

module.exports = router;
