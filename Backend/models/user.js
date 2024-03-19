import mongoose  from 'mongoose';
const Schema = mongoose.Schema;

const UserSchema = new Schema({
    username: { 
        type: String, 
        required: true, 
        unique: true
    },
    email: { 
        type: String, 
        required: true,
        unique: true
    },
    password: { 
        type: String, 
        required: true 
    },
    firstName: {
        type: String,
    },
    lastName: {
        type: String,
    },
    role: { 
        type: String, 
        default: 'candidate'
    },
    userResume: { 
        type: String, 
        default: 'https://www.freeiconspng.com/thumbs/resume-icon-png/resume-icon-png-15.png'
    },
    profilePicture: { 
        type: String, 
        default: 'https://static.vecteezy.com/system/resources/previews/005/129/844/non_2x/profile-user-icon-isolated-on-white-background-eps10-free-vector.jpg'
    },
    about: {
        type: String,
    },
    mobileno: {
        type: String,
    },
    address: {
        type: String,
    },
    skills: {
        type: [String],
    },
    experience: {
        type: String,
    },
    dob: {
        type: String,
    },
    education: {
        type: String,
    },
    applied: [{ type: Schema.Types.ObjectId, ref: "Jobs" }],
},
    {
        timestamps: true
    }
);

export default mongoose.model('User', UserSchema);