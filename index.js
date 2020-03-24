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

// create home route
app.get('/', (req, res) => {
	res.render('home', { user: req.user });
});

app.listen(3000,  () => {
	console.log('Listening on port 3000');
});

// // set up BodyParser
// var bodyParser = require('body-parser');
// app.use(bodyParser.json());
// app.use(bodyParser.urlencoded({ extended: true }));

// //set up Passport
// var auth = require('./routes/api/auth');
// var passport = require('passport');
// var GoogleStrategy = require('passport-google-oauth20').Strategy;
// app.use(passport.initialize());
// app.use(passport.session());

// // import the User class from User.js
// var User = require('./User');

// // set up routes
// var routes = require('./routes/initialRoutes');

// // set up static folder
// // app.use(express.static(path.join('./public', public)));
// app.use('/public', express.static('public'));
// app.get('/', (req, res) => res.send('API Running'));
// //app.use('/', routes);
// //app.get('/', (req, res) => { res.redirect('./routes/initialRoutes.js');  });
// // app.use('/', (req, res) => { res.redirect('/public/personform.html'); } );



// /***************************************/

// // route for creating a new user
// // this is the action of the "create new person" form
// app.use('/signup', (req, res) => {
// 	// construct the Person from the form data which is in the request body
// 	var newUser = new User ({
// 		firstName: firstName,
// 		lastName: lastName,
// 		email: email,
// 		password: password
// 	});

// 	// save the person to the database
// 	newUser.save( (err) => { 
// 		if (err) {
// 		    res.type('html').status(200);
// 		    res.write('uh oh: ' + err);
// 		    console.log(err);
// 		    res.end();
// 		}
// 		else {
// 			// display the "successfull created" page using EJS
// 			// make EJS 
// 		    res.render('userCreated', {user : newUser});
// 		}
// 	} ); 
// });

// /*************************************************/

// //app.use('/public', express.static('public'));

// //app.use('/', (req, res) => { res.redirect('/public/personform.html'); } );


