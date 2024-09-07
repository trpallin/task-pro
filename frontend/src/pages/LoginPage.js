import React, {useState} from 'react';
import Button from '../components/Button';
import styles from './LoginPage.module.css'
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import {useRedirectToMainIfAuth} from "../hooks/authHooks";

const LoginPage = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
    });
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useRedirectToMainIfAuth();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSignUpClick = () => {
        navigate('/signup');
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        api.post('/auth/login', formData)
            .then(response => {
                const token = response.data.token;
                localStorage.setItem('token', token);
                navigate('/main');
            })
            .catch(error => {
                setErrorMessage(error.response?.data?.message || 'Error logging in');
            });
    };

    return (
        <div className={styles.loginContainer}>
            <form onSubmit={handleSubmit} className={styles.loginForm}>
                <h2>Login</h2>
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}

                <div className={styles.formGroup}>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="Email"
                        required
                    />
                </div>

                <div className={styles.formGroup}>
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="Password"
                        required
                    />
                </div>

                <div className={styles.buttonContainer}>
                    <Button type="submit">Login</Button>
                    <Button onClick={handleSignUpClick}>Sign Up</Button>
                </div>
            </form>
        </div>
    );
};

export default LoginPage;