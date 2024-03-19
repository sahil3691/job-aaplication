import Jwt  from "jsonwebtoken";
import user from "../models/user.js";
import asyncHandler from "./asyncHandler.js";

const authenticateuser = asyncHandler(async (req, res, next) => {
    const headertoken = req.body;
    console.log(headertoken);
    if(headertoken){
        try{
            const decoded=Jwt.verify(headertoken,process.env.JWT_SECRET);
            req.user=await user.findById(decoded.id);
            next();
        }catch(error){
            res.status(401);
            throw new Error("Not authorized, token failed");
        }
    }
    else{
        res.status(401);
        throw new Error("Not authorized, no token found");
    }
});

export default authenticateuser;