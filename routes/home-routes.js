var router = require('express').Router();
var User = require('../models/User');
var Appointment = require('../models/Appointment');

var authCheck = (req, res, next) => {
    if(!req.user) {
        // not logged in
        res.redirect('/auth/login');
    } else {
        next();
    }
};

router.get('/', authCheck, (req, res) => {
    if (req.user && req.user.isTutor) {
        const tutorEmail = req.user.email;
        console.log("trying to find confirmed tutor appointments with " + tutorEmail);
        if (tutorEmail) {
            Appointment.find( {tutorEmail: tutorEmail, confirmed: true}, (err, appts) => {
                if (err) {
                    console.log(err);
                    res.render('tutor-home', { user: req.user, appts : {} });
                } else if (!appts) {
                    console.log('no appointments');
                    res.render('tutor-home', { user: req.user, appts : {} });
                } else {
                    console.log('appointments exists');
                    console.log(appts);
                    res.render('tutor-home', { user: req.user, appts : appts });
                }
            });
        }
     } else if (req.user) {
            const tuteeEmail = req.user.email;
            console.log("trying to find confirmed tutee appointments with " + tuteeEmail);
            if (tuteeEmail) {
                Appointment.find( {tuteeEmail: tuteeEmail, confirmed: true}, (err, appts) => {
                    if (err) {
                        console.log(err);
                        res.render('tutee-home', { user: req.user, appts : {} });
                    } else if (!appts) {
                        console.log('no appointments');
                        res.render('tutee-home', { user: req.user, appts : {} });
                    } else {
                        console.log('appointments exists');
                        console.log(appts);
                        res.render('tutee-home', { user: req.user, appts : appts });
                    }
                });
            }   
        } else {
            res.render('tutee-home');
        }
});

module.exports = router;