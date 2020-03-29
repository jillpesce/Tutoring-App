var router = require('express').Router();

var authCheck = (req, res, next) => {
    if(!req.user) {
        // not logged in
        res.redirect('/auth/login');
    } else {
        // logged in
        console.log('user is logged in tryna get to profile');
        next();
    }
};

router.get('/', authCheck, (req, res) => {
    // res.send("you are logged in this is your profile: " + req.user.firstName);
    console.log('in profile');
    res.render('profile', { user: req.user });
});

module.exports = router;