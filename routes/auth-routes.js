// create instance of router
var express = require('express');
var router = express.Router();

var passport = require('passport');

// auth login 
router.get('/login', (req, res) => {
    res.render('login', { user : req.user });
});

// auth logout
router.get('/logout', (req, res) => {
    // handled with passports
    req.logout();
    res.redirect('/');
});

// auth with google
router.get('/google', passport.authenticate('google', {
    scope: ['profile', 'email']
}));

// callback route for google to redirect to
router.get('/google/callback', passport.authenticate('google'), (req, res) => {
    //res.send(req.user);
    res.redirect('/profile/');
});

module.exports = router;