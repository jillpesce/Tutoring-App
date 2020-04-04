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

// set up BodyParser
var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

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
        res.render('signup', { user: req.user, message: "Please enter a 4 number year." });
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
<<<<<<< Updated upstream
});

app.listen(3000,  () => {
	console.log('Listening on port 3000');
=======
});

// save a new timeslot
app.get('/makeTimeslot', (req, res) => {
    console.log('hit save timeslot endpoint');
    console.log('with email' + req.query.email);
    console.log('with date' + req.query.date);
    console.log('with name' + req.query.name);
    var courses = [];

    if (req.query.course) {
        if (req.query.course.length > 1) {
            courses = req.query.course;
        } else if (req.query.course.length = 1) {
            courses = [req.query.course];
        } else {
            courses = [];
        }
    }
    
    var newTimeslot = new Timeslot ({
        tutorEmail: req.query.email,
        tutorName: req.query.name, 
        date: req.query.date,
        courses: courses,
    });

    console.log(newTimeslot);

    newTimeslot.save((err) => {
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

var Timeslot = require('./models/Timeslot');

// save a new appointment request
app.get('/bookAppointment', (req, res) => {
    console.log('hit save appt endpoint');
    var newAppointment = new Appointment({
        tutorEmail: req.query.tutoremail,
        tutorName: req.query.tutorname, 
        tuteeEmail: req.query.tuteeemail,
        tuteeName: req.query.tuteename, 
        date: req.query.date,
        confirmed: false
    });

    newAppointment.save((err) => {
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

var Appointment = require('./models/Appointment');

app.get('/getAllTimeslots', (req, res) => {
	console.log("getting all Timeslots");
    Timeslot.find((err, all) => {
        if (err) {
            console.log(err);
            res.json({});
        } else if (!all) {
            console.log('did NOT find timeslots');
            res.json({});
        } else {
            console.log('found timeslots');
            console.log(all);
            res.send(all);
        }
    });
});

app.listen(3000, () => {
    console.log('Listening on port 3000');
>>>>>>> Stashed changes
});
