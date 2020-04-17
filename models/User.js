var mongoose = require('mongoose');
var Schema = mongoose.Schema; 

var userSchema = new Schema({
    name: String,
    email: {type: String, required: true, unique: true},
    picture: String, 
    school: String,
    major: String,
    gradYear: String,
    bio: String,
    scores: Array,
    isTutor: Boolean, 
    isNewUser: Boolean,
    courses: Array, 
    ratingsAsTutor: Array,
    ratingsAsTutee: Array,
    appointmentsAsTutor: Array,
    appointmentsAsTutee: Array
});

// export userSchema as a class called User
var User = mongoose.model('user', userSchema);

module.exports = User;
