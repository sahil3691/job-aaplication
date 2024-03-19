import jwt from 'jsonwebtoken';

const generateToken = (Id) => {
    console.log(Id);
    const token = jwt.sign(
        { id: Id },
        process.env.JWT_SECRET,
        { expiresIn: '1d' }
    );
    console.log(`${token}`);
    // res.status(200).send('JWT token sent successfully');
    return token;
}


export default generateToken;