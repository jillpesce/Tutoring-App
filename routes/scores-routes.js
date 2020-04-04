var router = require('express').Router();
var User = require('../models/User');

var authCheck = (req, res, next) => {
    if(!req.user) {
        // not logged in
        res.redirect('/auth/login');
    } else {
        // logged in
        next();
    }
};

//scores route
router.get('/', authCheck, (req, res) => {
    res.render('scores', { user: req.user });
});

//Post new score
router.post('/save', (req, res) => {
    req.user.scores.push(req.body.inputScore);
    console.log(req.user.scores);
    var newValues = { $set: {
        scores: req.user.scores
    }};
    User.updateOne({email: req.user.email}, newValues).then(() => {
        console.log('New scores = ' + req.user.scores);
        res.redirect('/scores');
    });
});
module.exports = router;