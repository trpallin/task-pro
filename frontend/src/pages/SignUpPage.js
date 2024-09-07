import React, { useState } from "react";
import styles from "./SignUpPage.module.css";
import Button from "../components/Button";
import {useNavigate} from "react-router-dom";
import api from "../services/api";
import ConfirmModal from "../components/ConfirmModal";

const SignUpPage = () => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        password: '',
        confirmPassword: '',
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [showModal, setShowModal] = useState(false);

    const navigate = useNavigate();

    const handleLoginClick = () => {
        navigate('/login');
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setShowModal(true);
    };

    const confirmSubmit = () => {
        const { name, email, password, confirmPassword } = formData;
        setShowModal(false);

        if (password !== confirmPassword) {
            setErrorMessage('Passwords do not match!');
            return;
        }

        api.post('/auth/signup', { name, email, password })
            .then((response) => {
                setSuccessMessage('Sign up successful! Please log in.');
                setErrorMessage('');
            })
            .catch((error) => {
                setErrorMessage(error.response?.data?.message || 'Error occurred during sign up.');
            });
    }

    const cancelSubmit = () => {
        setShowModal(false);
    }

    return (
        <div className={styles.signupContainer}>
            <form onSubmit={handleSubmit} className={styles.signupForm}>
                <h2>Sign Up</h2>
                {errorMessage && <p className={styles.error}>{errorMessage}</p>}
                {successMessage && <p className={styles.success}>{successMessage}</p>}

                <div className={styles.formGroup}>
                    <label htmlFor="name">Name</label>
                    <input
                        type="name"
                        name="name"
                        value={formData.name}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className={styles.formGroup}>
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
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
                        required
                    />
                </div>

                <div className={styles.formGroup}>
                    <label htmlFor="confirmPassword">Confirm Password</label>
                    <input
                        type="password"
                        name="confirmPassword"
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className={styles.buttonContainer}>
                    <Button type="submit">Sign Up</Button>
                    <Button onClick={handleLoginClick}>Go to Login</Button>
                </div>
            </form>

            {showModal && (
                <ConfirmModal
                    message="Are you sure you want to sign up with this information?"
                    onConfirm={confirmSubmit}
                    onCancel={cancelSubmit}
                />
            )}
        </div>
    );
};

export default SignUpPage;