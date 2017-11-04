var express = require('express');
var auth = require("../passport-auth.js")();
var router = express.Router();

var authRoutes = require('./auth');
var pollsRoutes = require('./polls');
var usersRoutes = require('./users');

router.get('/status', (req, res) =>
  res.json({
    status: "ok"
  })
);

router.use('/auth', authRoutes);
router.use('/polls', auth.authenticate(), pollsRoutes);
router.use('/users', auth.authenticate(), usersRoutes);

module.exports = router;
