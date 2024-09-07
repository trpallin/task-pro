import React, {useEffect} from 'react';
import styles from './ConfirmModal.module.css';
import Button from "./Button";

const ConfirmModal = ({ onConfirm, onCancel, message, warning = false }) => {
    useEffect(() => {
        const handleEscape = (event) => {
            if (event.key === 'Escape') {
                onCancel();
            }
        };
        document.addEventListener('keydown', handleEscape);

        return () => {
            document.removeEventListener('keydown', handleEscape);
        };
    }, [onCancel]);

    return (
        <div className={styles.modal}>
            <div className={styles.modalContent}>
                <p>{message}</p>
                <div className={styles.buttonGroup}>
                    {warning ? (
                        <Button onClick={onConfirm} variant="cancel">Delete</Button>
                    ) : (
                        <Button onClick={onConfirm} variant="confirm">Confirm</Button>
                    )}
                    <Button onClick={onCancel} variant="normal">Cancel</Button>
                </div>
            </div>
        </div>
    );
};

export default ConfirmModal;