var mongoose = require('mongoose');
var Schema = mongoose.Schema; 

var timeslotScheme = new Schema({
    tutorEmail: String,
    tutorName: String, 
    date: String,
    courses: [],
});

// export userSchema as a class called User
var Timeslot = mongoose.model('timeslot', timeslotScheme);

module.exports = Timeslot;
