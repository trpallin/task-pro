import React from 'react';
import {useNavigate} from 'react-router-dom';
import styles from "./Header.module.css";

const Header = ({ title, backButtonText, backButtonPath }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const handleGoBack = () => {
        if (backButtonPath) {
            navigate(backButtonPath);
        }
    };

    return (
        <header className={styles.header}>
            <div>
                <h1>{title}</h1>
            </div>
            <nav>
                {backButtonPath && (
                    <button onClick={handleGoBack} className={styles.backButton}>
                        {backButtonText || 'Back'}
                    </button>
                )}
                <button onClick={handleLogout} className={styles.logoutButton}>Logout</button>
            </nav>
        </header>
    );
};

export default Header;