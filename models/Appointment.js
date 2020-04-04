var mongoose = require('mongoose');
var Schema = mongoose.Schema; 

var AppointmentScheme = new Schema({
    tutorEmail: String,
    tutorName: String, 
    tuteeEmail: String,
    tuteeName: String, 
    date: String,
    confirmed: Boolean
});

// export userSchema as a class called User
var Appointment = mongoose.model('appointment', appointmentScheme);

module.exports = User;
