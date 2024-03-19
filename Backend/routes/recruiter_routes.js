import express from 'express';
import { cretatejob, getalljobs, getrecruitermadejobs, getusersofparticularjob, updateprofile } from '../controllers/recruiter_controller.js';
const router=express.Router();

router.post('/updateprofile',updateprofile);
router.post('/createjob',cretatejob);
router.get('/getalljobs',getalljobs);
router.post('/getrecruitermadejobs',getrecruitermadejobs);
router.post('/getusersofparticularjob',getusersofparticularjob);

export default router;