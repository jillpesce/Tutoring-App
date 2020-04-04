const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20');
const keys = require('./keys');
const User = require('../models/User');

passport.serializeUser((user, done) => {
    done(null, user.id)
});

passport.deserializeUser((id, done) => {
    User.findById(id).then((user) => {
        done(null, user);
    });
});

passport.use(
    new GoogleStrategy({
        // options for the strategy
        callbackURL: '/auth/google/callback',
        clientID: keys.google.clientID,
        clientSecret: keys.google.clientSecret
    }, async (accessToken, refreshToken, profile, done) => {
        // check if user already exists in DB
        console.log('checking if user exists');
        console.log(profile);
        User.findOne({email: profile.emails[0].value}).then((currentUser) => {
            if(currentUser){
                // already have a user
                console.log('user exists already: ' + currentUser);
                done(null, currentUser);
            } else {
                // create user in DB
                console.log('user DOES NOT exist');
                new User({
                    name: profile.displayName,
                    email: profile.emails[0].value,
                    school: null,
                    major: null,
                    gradYear: null,
                    picture: profile.photos[0].value,
                    scores: [],
                    courses: [],
                    isTutor: false,
                    isNewUser: true
                }).save().then((newUser) => {
                    console.log('new user created: ' + newUser);
                    done(null, newUser);
                });
            }
        });
    })
);