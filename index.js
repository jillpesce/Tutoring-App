// set up Express
var express = require('express');
var app = express();
var authRoutes =  require('./routes/auth-routes');
var profileRoutes =  require('./routes/profile-routes');
var scoresRoutes =  require('./routes/scores-routes');

var User = require('./models/User');

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
app.use(express.static('public'));

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

//set up routes
app.use('/auth', authRoutes);
app.use('/profile', profileRoutes);
app.use('/scores', scoresRoutes);

// create home route
app.get('/', (req, res) => {
    if (req.user && req.user.isTutor) {
        res.render('tutor-home', { user: req.user });
    } else {
        res.render('tutee-home', { user: req.user });
    }
});

app.get('/aboutUs', (req, res) => {
	console.log("in aboutUs route");
	res.render('aboutUs', { user: req.user });
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


app.post('/createProfile', function(req, res) {
    if(req.body.school == "--Select a school--") {
        // display error and ask to fill out again
        res.render('signup', { user: req.user, message: "Please select a school." });
	} else if(req.body.gradYear == null || (req.body.gradYear).length != 4) {
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

app.post('/editProfile', function(req, res) {
	console.log('in editProf func');
	if(req.body.gradYear == null || (req.body.gradYear).length != 4) {
        // display error and ask to fill out again
        res.render('editProfile', { user: req.user, message: "Please enter a 4 number year." });
    } else if(req.body.major == null || req.body.major == "") {
        // display error and ask to fill out again
        res.render('editProfile', { user: req.user, message: "Please enter a major." });
    } else if(req.body.bio == null || req.body.bio == "") {
        // display error and ask to fill out again
        res.render('editProfile', { user: req.user, message: "Please enter a short bio." });
    } else {
		// update fields for user
		console.log('updating user');
        var newValues = { $set: {
            name: req.body.name,
            school: req.body.school,
            gradYear: req.body.gradYear,
            major: req.body.major,
            bio: req.body.bio
        }};
        User.updateOne({email: req.user.email}, newValues).then(() => {
            console.log('edited profile for - ' + req.body.email);
            res.redirect('/profile/');
        });
    }
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

app.get('/findTimeslot', (req, res) => {
    const email = req.query.email;
    const date = req.query.date;
    console.log("trying to find timeslot with " + email +", " +date);
    if (email) {
        Timeslot.findOne( {tutorEmail: email, date: date}, (err, timeslot) => {
            if (err) {
                console.log(err);
                res.json({});
            } else if (!timeslot) {
                console.log('timeslot does not exist');
                res.json({'result':'fail'});
            } else {
                console.log('timeslot exists');
                res.send({'result': 'true'});
            }
        });
	}
});

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

app.get('/cancelAppointment', (req, res) => {
    console.log('hit cancel appt endpoint');
    var tutorEmail = req.query.tutorEmail;
    var tuteeEmail = req.query.tuteeEmail;
    var data = req.query.date;
    Appointment.findOneAndDelete( 
        {tutorEmail: tutorEmail, tuteeEmail: tuteeEmail, date: data}, (err) => {
            if (err) {
                console.log(err);
                res.json({'result' : 'failed'});
            } else {
                console.log('success');
                res.json({'result' : 'success'});
            }
    });
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

app.get('/findTuteeAppointments', (req, res) => {
    const tuteeEmail = req.query.tuteeEmail;
    console.log("trying to find confirmed tutee appointments with " + tuteeEmail);
    if (tuteeEmail) {
        Appointment.find( {tuteeEmail: tuteeEmail, confirmed: true}, (err, appts) => {
            if (err) {
                console.log(err);
                res.json({});
            } else if (!appts) {
                console.log('no appointments');
                res.json({});
            } else {
                console.log('appointments exists');
                console.log(appts);
                res.send(appts);
            }
        });
	}
});

app.get('/findTutorAppointments', (req, res) => {
    const tutorEmail = req.query.tutorEmail;
    console.log("trying to find confirmed tutee appointments with " + tutorEmail);
    if (tutorEmail) {
        Appointment.find( {tutorEmail: tutorEmail, confirmed: true}, (err, appts) => {
            if (err) {
                console.log(err);
                res.json({});
            } else if (!appts) {
                console.log('no appointments');
                res.json({});
            } else {
                console.log('appointments exists');
                console.log(appts);
                res.send(appts);
            }
        });
	}
});

app.get('/getRatings', (req, res) => {
    console.log('hit getRatings endpoint');
    User.findOne( {email: req.query.email}, (err, user) => {
        if (err) {
            console.log('hit ERROR in getRatings');
            console.log(err);
            res.json({});
        } else if (!user) {
            console.log('did NOT find user');
            res.json({});
        } else {
            console.log('found user');
            console.log('users ratings', user.ratings);
            console.log('users name', user.name);
            console.log('users bio', user.bio);
            res.json({'ratings' : user.ratings})
        }
    });
});

app.get('/saveRating', (req, res) => {
    console.log('hit saveRating endpoint');
    console.log('users email', req.query.email);
    const rating = req.query.rating;
    console.log('rating' + rating);
    //const oldRatings = [];
    User.findOne( {email: req.query.email}, (err, user) => {
        if (err) {
            console.log(err);
        } else if (!user) {
            console.log('did NOT find user');
        } else {
            console.log("USER: ", user);
            console.log('found user');
            console.log('users ratings', user.ratings);
            console.log('users name', user.name);
            console.log('users bio', user.bio);
            ratings = user.ratings;
            //oldRatings.push(user.ratings);

            console.log("old Ratings: ", ratings);
            ratings.push(parseInt(rating));
            console.log("newRatings: ", ratings);
            var newValues = { $set: {
                ratings: ratings
            }};
            User.updateOne({email: req.query.email}, newValues).then((err) => {
                if (err) {
                    console.log('ratings update fail');
                    console.log(err);
                    res.json({'result' : 'fail'});
                } else {
                    console.log('ratings updated for ' + email);
                    res.json({'result' : 'success'});
                }
            });
        }
    });
    
});

app.listen(3000,  () => {
    console.log('Listening on port 3000');
});
