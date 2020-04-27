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
    if (req.user) {
        const email = req.user.email;
        console.log("trying to find AWARDS FOR " + email);
        if (email) {
            var quality = "";
            var experience = "";
            var excellence = "";
            // Quality: bronze - average rating = 3-, silver - average rating = 3+, gold - average rating = 4+, platinum - average rating = 5
            const ratings = req.user.ratings;
            var sum = 0; 
            var numFives = 0;
            for (var i = 0; i < ratings.length; i++) {
                sum += ratings[i];
                console.log("ratings at" + i + ": " + ratings[i]);
                if (ratings[i] == 5) {
                    numFives++;
                }
            }
            const avgRating = sum / ratings.length;
            console.log("avgRating: " + avgRating);
            if (avgRating > 3 && avgRating < 4) {
                quality = "silver";
            } else if (avgRating < 5) {
                quality = "gold";
            } else if (avgRating == 5) {
                quality = "platinum";
            } else {
                quality = "bronze";
            }
            console.log("quality: " + quality);
            // Excellence: received 10 5 star reviews (good), received 15 5 star reviews (great), received 25 5 star reviews (fantastic)
            if (numFives < 10) {
                excellence = "none";
            } else if (numFives < 15) {
                excellence = "good";
            } else if (numFives < 25) {
                excellence = "great";
            } else if (numFives >= 25) {
                excellence = "fantastic";
            }
            console.log("numFives: " + numFives);
            console.log("excellence: " + excellence);
            // Experience: tutored 10 sessions, tutored 25 sessions, tutored 50 sessions, tutored 100 sessions
            async function findAppts() {
                var appts = [];
                var promise = new Promise(function(resolve, reject) {
                    Appointment.find( {tutorEmail: email, confirmed: true}, (err, tutorAppts) => {
                        console.log("TUTORappts.length: " + tutorAppts.length);
                        if (err) {
                            console.log(err);
                        } else if (!tutorAppts) {
                            console.log('no appointments');
                        } else if (tutorAppts.length == 0) {
                            console.log('no appointments');
                        } else {
                            console.log('appointments exists');
                            for (var i = 0; i < tutorAppts.length; i++) {
                                appts.push(tutorAppts[i]);
                            }
                        }
                    });
                    Appointment.find( {tuteeEmail: email, confirmed: true}, (err, tuteeAppts) => {
                        console.log("TUTEEappts.length: " + tuteeAppts.length);
                        if (err) {
                            console.log(err);
                        } else if (!tuteeAppts) {
                            console.log('no appointments');
                            resolve(appts.length);
                        } else if (tuteeAppts.length == 0) {
                            console.log('no appointments');
                            resolve(appts.length);
                        } else {
                            console.log('appointments exists');
                            for (var i = 0; i < tuteeAppts.length; i++) {
                                appts.push(tuteeAppts[i]);
                            }
                            console.log("RESOLVING");
                            resolve(appts.length);
                        }
                    });
                });
                var result = await promise;
                console.log("result: " + result);
                return result;
            }

            async function assignExperience() {
                console.log("IN ASYNC FUNC  wiaitng for appts.length");
                var apptsNum = await findAppts();
                console.log("IN ASYNC FUNC appts.length: " + apptsNum);
                if (apptsNum < 10) {
                    experience = "none";
                } else if (apptsNum < 25) {
                    experience = "10";
                } else if (apptsNum < 50) {
                    experience = "25";
                } else if (apptsNum < 100) {
                    experience = "50";
                } else {
                    experience = "100";
                }
                console.log("experience: " + experience);
            };
            async function callRender() {
                await assignExperience();
                res.render('awards', { user: req.user, quality: quality, experience: experience, excellence: excellence });
            }
            callRender();
        }
     } 
});

module.exports = router;