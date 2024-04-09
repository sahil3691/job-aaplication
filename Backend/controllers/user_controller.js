import asyncHandler from '../middlewares/asyncHandler.js';
import user from '../models/user.js';
import jwt from 'jsonwebtoken';
import Jobs from '../models/jobs.js';
          
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

const appliedjobs=asyncHandler(async(req,res)=>{
    const {userid}=req.body;
    // console.log(userid);
    try {
        const olduser = await user.findById(userid).populate({
            path: 'applied',
            model: 'Jobs'
        });
        // console.log(olduser);
        // console.log(olduser.applied);
        if (!olduser || !olduser.applied) {
            return [];
        }
        const appliedJobs = olduser.applied;
        // console.log(appliedJobs);
        res.json(appliedJobs);
    } catch (error) {
        return res.status(500).json({msg: error.message});
    }
});

const applyforjob=asyncHandler(async(req,res)=>{
    try {
        const { jobId ,userId} = req.body;
        const user = await user.findById(userId);
        if (!user) {
          return res.status(404).json({ message: 'User not found' });
        }
    
        const job = await Jobs.findById(jobId);
        if (!job) {
          return res.status(404).json({ message: 'Job not found' });
        }
    
        user.applied.push(jobId);
        await user.save();

        job.application.push(userId);
        await job.save();
    
        return res.status(200).json({ message: 'Job application successful' });
      } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal server error' });
      }
});
export {updateprofile,appliedjobs,applyforjob}; 