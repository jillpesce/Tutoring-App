// set up Express
var express = require('express');
var app = express();
var authRoutes =  require('./routes/auth-routes');
var profileRoutes =  require('./routes/profile-routes');
var scoresRoutes =  require('./routes/scores-routes');

var passportSetup = require('./config/passport-setup');
var passport = require('passport');
var mongoose = require('mongoose');
var keys = require('./config/keys');

var cookieSession = require('cookie-session');

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

// set up EJS
app.set('view engine', 'ejs');

app.use(cookieSession({
    maxAge: 24 * 60 * 60 * 1000,
    keys: [keys.session.cookieKey]
}));

//public styles folder
app.use(express.static('public'));

// initialize passport
app.use(passport.initialize());
app.use(passport.session());

// connect to mongodb
mongoose.connect(keys.mongodb.dbURI, () => {
    console.log('connected to mongodb');
});

// mongoose.connect(keys.mongodb.dbURI, {
//     useUnifiedTopology: true,
//     useNewUrlParser: true})
//     .then(() => console.log("connected to mongodb"))
//     .catch(err => console.error("An error has occured", err));

//set up routes
app.use('/auth', authRoutes);
app.use('/profile', profileRoutes);
app.use('/scores', scoresRoutes);

app.get('/find', (req, res) => {
    const email = req.query.email;
    console.log(email);
    if (email) {
        User.findOne( {email: email}, (err, user) => {
            if (err) {
                console.log(err);
                res.json({});
            } else if (!user) {
                console.log('did NOT find user');
                res.json({});
            } else {
                console.log('found user');
                res.send(user);
            }
        });
    }
    //console.log(res);
});

app.get('/save', (req, res) => {
    console.log('hit save endpoint');
    const email = req.query.email;
    var newUser = new User ({
        name: req.query.name,
        email: req.query.email,
        school: req.query.school,
        major: req.query.major,
        gradYear: req.query.gradYear,
        bio: req.query.bio
    });
    
    newUser.save((err) => {
        if (err) {
            console.log('fail');
            console.log(err);
            res.json({'result' : 'fail'});
        } else {
            console.log('success');
            res.json({'result' : 'success'});
        }
    })
})

// create home route
app.get('/', (req, res) => {
    if (req.user && req.user.isTutor) {
        res.render('tutor-home', { user: req.user });
    } else {
        res.render('tutee-home', { user: req.user });
    }
});

app.get('/find', (req, res) => {
    const email = req.query.email;
    console.log(email);
    if (email) {
        User.findOne( {email: email}, (err, user) => {
            if (err) {
                console.log(err);
                res.json({});
            } else if (!user) {
                console.log('did NOT find user');
                res.json({});
            } else {
                console.log('found user');
                res.send(user);
            }
        });
    }
    //console.log(res);
});

app.get('/save', (req, res) => {
    console.log('hit save endpoint');
    const email = req.query.email;
    var newUser = new User ({
        name: req.query.name,
        email: req.query.email,
        school: req.query.school,
        major: req.query.major,
        gradYear: req.query.gradYear,
        bio: req.query.bio
    });
    
    newUser.save((err) => {
        if (err) {
            console.log('fail');
            console.log(err);
            res.json({'result' : 'fail'});
        } else {
            console.log('success');
            res.json({'result' : 'success'});
        }
    })
});

var User = require('./models/User');

app.post('/createProfile', function(req, res) {
    console.log('in post func');
    console.log('gradYear: ' + req.body.gradYear);
    // if(req.body.school == null || req.body.school == "") {
    //     // display error and ask to fill out again
    //     res.render('signup', { user: req.user, message: "Please enter a school." });
    // } else 
    if(req.body.gradYear == null || (req.body.gradYear).length != 4) {
        // display error and ask to fill out again
        res.render('signup', { user: req.user, message: "Please enter a 4 digit year." });
    } else if(req.body.major == null || req.body.major == "") {
        // display error and ask to fill out again
        res.render('signup', { user: req.user, message: "Please enter a major." });
    } else if(req.body.bio == null || req.body.bio == "") {
        // display error and ask to fill out again
        res.render('signup', { user: req.user, message: "Please enter a short bio." });
    } else {
        // update fields for user
        console.log('updating user');
        var newValues = { $set: {
            name: req.body.name,
            email: req.body.email,
            school: req.body.school,
            gradYear: req.body.gradYear,
            major: req.body.major,
            bio: req.body.bio,
            isNewUser: false 
        }};
        User.updateOne({email: req.body.email}, newValues).then(() => {
            console.log('Updated - ' + req.body.email);
            res.redirect('/profile/');
        });
    }
});
//Post toggle tutor status
app.post('/toggleTutor', function(req, res) {
    var newValues = { $set: {
        isTutor: !req.user.isTutor 
    }};
    User.updateOne({email: req.user.email}, newValues).then(() => {
        console.log('Is ' + req.user.email + ' a tutor? ' + req.user.isTutor);
        res.redirect('/');
    });
});
//Post new score
app.post('/scores/save', (req, res) => {
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
app.listen(3000,  () => {
    console.log('Listening on port 3000');
});
