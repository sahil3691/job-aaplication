import asyncHandler from '../middlewares/asyncHandler.js';
import {v2 as cloudinary} from 'cloudinary';
import user from '../models/user.js';
import jwt from 'jsonwebtoken';
          
// cloudinary.config({ 
//   cloud_name: process.env.CLOUD_NAME,
//   api_key: process.env.API_KEY,
//   api_secret: process.env.API_SECRET
// });

const updateprofile=asyncHandler(async(req,res)=>{
    const {token,firstName,lastName,about,mobileno,skills,experience,education,resume,imageurl,dob,address} = req.body;
    try {
        // const imageresult = await cloudinary.uploader.upload(imageurl,{
        // });
        // const resumeresult=await cloudinary.uploader.upload(resume,{
        // });
        // console.log(imageresult.secure_url);
        // console.log(resumeresult.secure_url);
        if (!token || !firstName || !lastName || !about || !mobileno || !skills || !experience || !education) {
            return res.status(400).json({msg: 'All fields are required'});
        }
        try {
            if(token){
                console.log("yaha tak aagaya");
                const userId = jwt.verify(token, process.env.JWT_SECRET).id;
                console.log("userId",userId);
                const candidate = await user.findById(userId);
                console.log("candidate", candidate);
                if (!candidate) {
                    return res.status(400).json({msg: 'user not found'});
                }
                candidate.firstName = firstName;
                candidate.lastName = lastName;
                candidate.profilePicture = imageurl;
                candidate.about = about;
                candidate.mobileno=mobileno;
                candidate.skills = skills;
                candidate.dob=dob;
                candidate.address=address;
                candidate.experience = experience;
                candidate.education = education;
                candidate.resume=resume;
                await candidate.save();
                console.log("candidate",candidate)
                res.json({msg: 'profile updated successfully'});
            }
            else{
                return res.status(400).json({msg: 'token not found'});
            }
        }
        catch (err) {
            return res.status(500).json({msg: err.message});
        }

    } catch (error) {
        console.log(error.message);
    }
});

const appliedjobs=(async(req,res)=>{
    const userid=req.body.olduserid;
    try {
        const olduser = await user.findById(userid).populate('applied');
        console.log(olduser);
        if (!olduser || !olduser.applied) {
            return [];
        }
        const appliedJobs = olduser.applied;
        console.log(appliedJobs);
        return appliedJobs;
    } catch (error) {
        return res.status(500).json({msg: error.message});
    }
});

export {updateprofile,appliedjobs}; 