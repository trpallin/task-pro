import React from 'react';
import Button from "../components/Button";
import {useNavigate} from "react-router-dom";

const MainPage = () => {
    const navigate = useNavigate();
    const handleLogout = () => {
        navigate('/logout');
    };

    return (
        <div>
            <h1>Welcome to the Main Page</h1>
            <Button onClick={handleLogout}>Logout</Button>
        </div>
    );
};

export default MainPage;