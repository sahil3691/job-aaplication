import express from 'express';
import dotenv from 'dotenv';
import connectDB from './config/db.js';
import cors from 'cors';
import userroutes from './routes/user_routes.js';
import auth from "./routes/logsign_routes.js";
import recruiterroutes from "./routes/recruiter_routes.js";
const app=express();

// Middlewares
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({extended:true}));

dotenv.config();
const port=process.env.PORT || 8000;
const DATABASE_URL=process.env.MONGODB_URI;

// Database Connection
connectDB(DATABASE_URL);

app.use('/user',userroutes);
app.use('/recruiter',recruiterroutes);
app.use('/auth',auth);

// app.use('/user')
app.get('/',(req,res)=>{
    res.send("welcome to JobJunction");
});

app.listen(port,()=>{
    console.log(`Server is running on port ${port}`);
});
