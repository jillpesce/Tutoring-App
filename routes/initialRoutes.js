var express = require('express');
var router = express.Router();

var user = require('../models/User');

router.get('/', function(req, res) {
    console.log("here splash");
    res.sendFile('../views/layouts/splash.html');
});

router.get('/signup', function(req, res) {
    console.log("here signup");
    res.sendFile('../views/layouts/signup.html');
});

router.get('/login', function(req, res) {
    console.log("here login");
    res.sendFile('../views/layouts/login.html');
});


router.post('/signup', function(req, res) {
    var firstName = req.body.firstName;
    var lastName = req.body.lastName;
    var email = req.body.email;
    var password = req.body.password;
    var confirmPassword = req.body.confirmPassword;

    req.checkBody('firstName', 'firstName is required.').notEmpty();
    req.checkBody('lastName', 'lastName is required.').notEmpty();
    req.checkBody('email', 'email is required.').notEmpty();
    req.checkBody('email', 'email is not valid').isEmail();
    req.checkbody('password', 'password is required').notEmpty();
    req.checkbody('confirmPassword', 'confirmPassword is required').notEmpty();
    req.checkbody('confirmPassword', 'passwords do not match').equals(req.body.password);

    var errors = req.validationErrors();

    if(error) {
        console.log("there are errors :(");
        // render soemthing saying there are errors
    } else {
        console.log("chillin");
        //  not sure
        var newUser = new User({
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password
        });

        User.createUser(newUser, function(){
            if (err) {
                throw err;
            } 
            console.log(user);
        }); 
    }
});


// route for creating a new person
// this is the action of the "create new person" form
app.use('/create', (req, res) => {
	// construct the Person from the form data which is in the request body
	var newPerson = new Person ({
		name: req.body.name,
		age: req.body.age,
	    });

	// save the person to the database
	newPerson.save( (err) => { 
		if (err) {
		    res.type('html').status(200);
		    res.write('uh oh: ' + err);
		    console.log(err);
		    res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    res.render('created', {person : newPerson});
		}
	    } ); 
    }
    );

module.exports = router;