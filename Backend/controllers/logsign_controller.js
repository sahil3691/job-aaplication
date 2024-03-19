import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import user from '../models/user.js';
import Recruiter from '../models/recruiter.js';
import generateToken from "../utils/createtoken.js";
import errorHandler from '../middlewares/errorhandler.js';

const signup = async (req, res,next) => {
    const {username, email, password,role} = req.body;
    console.log(req.body);
    if (!username || !email || !password || !role) {
        next(errorHandler(400, 'All fields are required'));
        return;
    }
    try {
        if(role!=="candidate" && role!=="recruiter"){
            next(errorHandler(400, 'Invalid role'));
            return;
        }
        
        if(role=="candidate"){
            const candidate = await user.findOne({email})
            if (candidate) {
                return res.status(400).json({msg: 'candidate already exists'});
            }
            const hashedPassword = bcrypt.hashSync(password, 10);
            const newCandidate = new user({
                username,
                email,
                password: hashedPassword, 
                role:role
            });

            await newCandidate.save();
            res.json({msg: 'candidate added successfully'});
        }
        else{
            const recruiter = await Recruiter.findOne({email});
            if (recruiter) {
                return res.status(400).json({msg: 'recruiter already exists'});
            }
            const hashedPassword = bcrypt.hashSync(password, 10);
            const newRecruiter = new Recruiter({
                username,
                email,
                password: hashedPassword,
                role:role 
            });

            await newRecruiter.save();
            res.json({msg: 'recruiter added successfully'});
        }
        
    } catch (error) {
        next(error);
        return;
    }
}


const login = async (req, res, next) => {
    const {email, password} = req.body;
    if (!email || !password) {
        next(errorHandler(400, 'All fields are required'));
    }
    try {
            const candidate = await user.findOne({email});
            if(candidate){
                const validPassword = bcrypt.compareSync(password, candidate.password);
                if (!validPassword) {
                    next(errorhandler(400, 'Invalid password'));
                    return;
                }
                const candidateid = candidate._id;
                console.log(candidateid);
                const token = generateToken(candidateid);
                console.log("token",token)
                res.json({msg: 'Login successful as Candidate', token:token});
                return;
            }

            const recruiter = await Recruiter.findOne({email});
            if (!recruiter) {
                next(errorHandler(400, 'Invalid credentials'));
                return;
            }
            else{
                const validPassword = bcrypt.compareSync(password, recruiter.password);
                if (!validPassword) {
                    next(errorHandler(400, 'Invalid passord'));
                    return;
                }
                const recruiterid = recruiter._id;
                console.log(recruiterid);
                const token = generateToken(recruiterid)
                console.log("token",token)
                res.json({msg: 'Login successful as Recruiter', token:token});
                return;
            }
    }
    catch (error) {
        next(errorHandler(500, 'Internal server error'));
        return;
    }
}

const google = async (req,res,next) => {
    const {username, email,password,role} = req.body;
    try{
        if(!username || !email || !password || !role){
            next(errorHandler(400, 'All fields are required'));
            return;
        }
        if(role=="candidate"){
            const candidate = await user.findOne({email});
            if(candidate){
                const token = generateToken(candidate._id);
                console.log(token);
                res.json({msg: 'Google Login successful as Candidate', token:token});
            }
            else{
                const hashedPassword = bcrypt.hashSync(password, 10);
                const newCandidate = new user({
                    username,
                    email,
                    password: hashedPassword,
                    role:role
                });
                newCandidate.save()
                const token = generateToken(newCandidate._id);
                res.json({msg: 'Candidate added successfully using google signin', token:token});
            }
        }
        else{
            const recruiter = await Recruiter.findOne({email});
            if(recruiter){
                const token = generateToken(recruiter._id);
                res.json({msg: 'Google Login successful as Recruiter ', token:token});
            }
            else{
                const generatedPassword = email+1234;
                const hashedPassword = bcrypt.hashSync(generatedPassword, 10);
                const newRecruiter = new Recruiter({
                    username,
                    email,
                    password: hashedPassword,
                    role:role
                });
                newRecruiter.save();
                const token = generateToken(newRecruiter._id);
                console.log(token);
                res.json({msg: 'Recruiter added successfully using google signin', token:token});
            }
        }
    }catch(error){
        next(errorHandler(500, 'Internal server error'));
        return;
    }
}

export { signup, login, google};