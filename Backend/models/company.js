import mongoose, { Schema } from "mongoose";
import validator from "validator";
import bcrypt from "bcryptjs";
import JWT from "jsonwebtoken";

const companySchema = new Schema({
  name: {
    type: String,
    required: [true, "Company Name is required"],
  },
  email: {
    type: String,
    required: [true, "Email is required"],
    unique: true,
    validate: validator.isEmail,
  },
  password: {
    type: String,
    required: [true, "Password is required"],
    minlength: [6, "Password must be at least"],
    select: true,
  },
  contact: { type: String },
  location: { type: String },
  about: { type: String },
  profileUrl: { type: String,default:"https://png.pngtree.com/png-vector/20210529/ourmid/pngtree-flat-style-city-construction-skyline-office-building-combination-png-image_3322655.jpg" },
  jobPosts: [{ 
        type: Schema.Types.ObjectId, 
        ref: "Recruiter" 
    }],
});


companySchema.pre("save", async function () {
  if (!this.isModified) return;
  const salt = await bcrypt.genSalt(10);
  this.password = await bcrypt.hash(this.password, salt);
});

const Companies = mongoose.model("Companies", companySchema);

export default Companies;