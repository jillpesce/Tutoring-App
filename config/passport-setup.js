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
        console.log("passport callback function fired");
        console.log(profile); 

        // check if user already exists in DB
        User.findOne({googleId: profile.id}).then((currentUser) => {
            if(currentUser){
                // already have a user
                console.log('user exists already: '+ currentUser);
                done(null, currentUser);
            } else {
                // create user in DB
                console.log('user DOES NOT exist');
                new User({
                    firstName: profile.name.givenName,
                    lastName: profile.name.familyName,
                    googleId: profile.id,
                    email: profile._json.email,
                    picture: profile.photos[0].value
                }).save().then((newUser) => {
                    console.log('new user created: ' + newUser);
                    done(null, newUser);
                });
            }
        });
    })
);