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
    // handled with passport
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

// const GoogleStrategy = require('passport-google-oauth').OAuth2Strategy;

// module.exports = (passport) => {
//     passport.serializeUser((user, done) => {
//         done(null, user);
//     });
//     passport.deserializeUser((user, done) => {
//         done(null, user);
//     });
//     passport.use(new GoogleStrategy({
//         clientID: "1091678435156-1m8duap8q40vsakohium0758bjuoe9tt.apps.googleusercontent.com",
//         clientSecret: "d6BcdvBiy1jG7QYhz4uRpUe1",
//         callbackURL: "http://localhost:3000/api/auth/callback"
//     },
//         (token, refreshToken, profile, done) => {
//             return done(null, {
//                 profile: profile,
//                 token: token
//             });
//         }));
// };