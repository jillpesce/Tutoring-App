var mongoose = require('mongoose');
var Schema = mongoose.Schema; 

var userSchema = new Schema({
    firstName: String,
    lastName: String,
    googleId: String,
    email: {type: String, required: true, unique: true},
    picture: String
});

// export userSchema as a class called User
var User = mongoose.model('user', userSchema);

module.exports = User;


// module.exports.createUser = function(newUser, callback) {
//     bcrypt.genSalt(10, function(err, salt) {
//         bcrypt.hash(newUser.password, salt, function(err, hash) {
//             newUser.password = hash;
//             newUser.save(callback);
//         });
//     });
// }

// userSchema.methods.standardizeName = function() {
//     this.name = this.name.toLowerCase();
//     return this.name;
// }
