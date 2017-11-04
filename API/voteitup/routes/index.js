var express = require('express');
var auth = require("../passport-auth.js")();
var router = express.Router();

var surveysRoutes = require('./surveys');
var authRoutes = require('./auth');

router.get('/status', (req, res) =>
  res.json({
    status: "ok"
  })
);

router.use('/auth', authRoutes);
router.use('/surveys', auth.authenticate(), surveysRoutes);

module.exports = router;
