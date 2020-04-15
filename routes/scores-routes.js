var router = require('express').Router();
var User = require('../models/User');
var Chart = require('chart.js');

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
    var date = new Date(); 
    var timestamp = date.getTime();
    var course = req.body.course;
    scoreTuple = [timestamp, req.body.inputScore, course];
    req.user.scores.push(scoreTuple);
    console.log(req.user.scores);
    var newValues = { $set: {
        scores: req.user.scores
    }};

    User.updateOne({email: req.user.email}, newValues).then(() => {
        console.log('New scores = ' + JSON.stringify(req.user.scores));

        var data = [];
        var count = 1;
        for (var i = 0; i < req.user.scores.length; i++) {
            if (req.user.scores[i][2] == course) {
                data.push({
                    "x": req.user.scores[i][1],
                    "y": count
                });
                count++;
            }
        }

        res.render('progress', { course: course, data: data, user: req.user });
    });
});

module.exports = router;