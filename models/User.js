var mongoose = require('mongoose');
var Schema = mongoose.Schema; 

var userSchema = new Schema({
    name: {type: String, required: true},
    email: {type: String, required: true, unique: true},
    school: {type: String, required: false},
    major: {type: String, required: false},
    gradYear: {type: String, required: false},
    bio: {type: String, required: false},
    picture: {type: String, required: false},
 });

// export userSchema as a class called User
var User = mongoose.model('user', userSchema);

module.exports = User;
