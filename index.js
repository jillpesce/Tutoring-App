// set up Express
var express = require('express');
var app = express();
var authRoutes =  require('./routes/auth-routes');
var profileRoutes =  require('./routes/profile-routes');

var passportSetup = require('./config/passport-setup');
var passport = require('passport');
var mongoose = require('mongoose');
var keys = require('./config/keys');

var cookieSession = require('cookie-session');

var User = require('./models/User');

// set up EJS
app.set('view engine', 'ejs');

app.use(cookieSession({
	maxAge: 24 * 60 * 60 * 1000,
	keys: [keys.session.cookieKey]
}));

// initialize passport
app.use(passport.initialize());
app.use(passport.session());

// conncet to mongodb
mongoose.connect(keys.mongodb.dbURI, () => {
	console.log('connected to mongodb');
});

//set up routes
app.use('/auth', authRoutes);
app.use('/profile', profileRoutes);

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
	res.render('home', { user: req.user });
});

app.listen(3000,  () => {
	console.log('Listening on port 3000');
});


