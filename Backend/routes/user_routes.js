import express from 'express';
import {appliedjobs, applyforjob, updateprofile } from '../controllers/user_controller.js';
import authenticateuser from '../middlewares/auth_middleware.js';
const router=express.Router();

router.post('/updateprofile',authenticateuser,updateprofile);
router.post('/appliedjobs',appliedjobs);
router.post('/applyforjob',applyforjob);

export default router;