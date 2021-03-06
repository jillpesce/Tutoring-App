var router = require('express').Router();
var User = require('../models/User');

var authCheck = (req, res, next) => {
    if(!req.user) {
        // not logged in
        res.redirect('/auth/login');
    } else {
        // logged in
        console.log('user is logged in tryna get to profile');
        next();
    }
};

router.get('/', authCheck, (req, res) => {
    // res.send("you are logged in this is your profile: " + req.user.firstName);
    console.log('in profile');
    res.render('profile', { user: req.user });
});

router.get('/editProfile', authCheck, (req, res) => {
    console.log('in edit profile');
    res.render('editProfile', { user: req.user, message: "" });
});

router.get('/search', (req, res) => {
    const searchName = req.query.name;
    User.find( { $text: { $search: searchName } }, function(err, users) {
        if (err) {
            console.log(err);
            return res.sendStatus(500);
        }
        res.json(users);
    });
});

router.get('/getAllUsers', (req, res) => {
    console.log('hit getAllUsers endpoint');
    User.find((err, allUsers) => {
        if (err) {
            console.log(err);
            return res.sendStatus(500);
        } else {
            res.json(allUsers);
        }
    })
});

module.exports = router;