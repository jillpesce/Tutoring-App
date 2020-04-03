var router = require('express').Router();

var authCheck = (req, res, next) => {
    if(!req.user) {
        // not logged in
        res.redirect('/auth/login');
    } else {
        // logged in
        next();
    }
};

//scores route
router.get('/', authCheck, (req, res) => {
    res.render('scores', { user: req.user });
});

module.exports = router;