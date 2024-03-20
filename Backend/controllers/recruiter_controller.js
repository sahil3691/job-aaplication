import asyncHandler from '../middlewares/asyncHandler.js';
import recruiter from '../models/recruiter.js';
import Jobs from '../models/jobs.js';

const updateprofile=asyncHandler(async(req,res)=>{
    const {
        token,
        username,
        email,
        password,
        firstName,
        lastName,
        currentPosi,
        about,
        company,
        contact,
        profilePicture
    } = req.body;

    try {
        
        if (!token || !username || !email || !password || !firstName || !lastName || !currentPosi || !about || !company || !contact) {
            return res.status(400).json({ msg: 'All fields are required' });
        }

        const recruiterId = jwt.verify(token, process.env.JWT_SECRET).id;
        const recruiterone = await recruiter.findById(recruiterId);
        if (!recruiterone) {
            return res.status(404).json({ msg: 'Recruiter not found' });
        }

        recruiterone.firstName = firstName;
        recruiterone.lastName = lastName;
        recruiterone.currentPosi = currentPosi;
        recruiterone.about = about;
        recruiterone.company = company;
        recruiterone.contact = contact;
        recruiterone.profilePicture = profilePicture;

        await recruiterone.save();
        return res.json({ msg: 'Profile updated successfully' });
    } catch (err) {
        console.error(err.message);
        return res.status(500).json({ msg: 'Internal Server Error' });
    }
})
const cretatejob=asyncHandler(async(req,res)=>{
    const { userid ,position,jobTitle,jobType,location,salary,vacancies,experience,desc,requirements} = req.body; 
    console.log(userid);
    try {
        const recruiterone = await recruiter.findById( userid); 

        if (!recruiterone) {
            return res.status(404).json({ error: 'Recruiter not found' });
        }

        const newJob = new Jobs({
            company: recruiterone._id,
            position:position,
            jobTitle:jobTitle,
            jobType:jobType,
            location:location,
            salary:salary,
            vacancies:vacancies,
            experience:experience,
            desc:desc,
            requirements:requirements
        });

        await newJob.save();
        recruiterone.jobsOffered.push(newJob._id);
        await recruiterone.save();

        res.status(201).json({ message: 'Job created successfully', job: newJob });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

const getalljobs=asyncHandler(async(req,res)=>{
    try {
        const jobPosts = await Jobs.find({});
        console.log(jobPosts);
        res.json({ jobPosts });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

const getrecruitermadejobs=asyncHandler(async(req,res)=>{
    const { recruiterId } = req.body;
    try {
        const jobPosts = await Jobs.find({recruiterId});
        res.json({ jobPosts });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

const getusersofparticularjob=asyncHandler(async(req,res)=>{
    const { jobId } = req.body;
    console.log(jobId);
    try {
        const jobPost = await Jobs.findById(jobId).populate({
            path: 'application',
            model: 'User'
        });
        console.log(jobPost);
        if (!jobPost || !jobPost.application) {
            return [];
        }
        if (!jobPost) {
            return res.status(404).json({ error: 'Job post not found' });
        }
        const applicants = jobPost.application;
        res.json({ applicants });
    } catch (error) {
        console.error(error);
        res.status(500).json({ error: 'Internal Server Error' });
    }
});

export {updateprofile,cretatejob,getalljobs,getrecruitermadejobs,getusersofparticularjob}; 
