import styles from "./ProjectForm.module.css";
import Button from "./Button";
import React, { useState } from "react";
import ConfirmModal from "./ConfirmModal";

const ProjectForm = ({ projectData, onConfirm, handleChange, buttonLabel, confirmMessage, title }) => {
    const [showModal, setShowModal] = useState(false);
    const handleSubmit = () => {
        setShowModal(true);
    };
    const confirmSubmit = () => {
        onConfirm();
        setShowModal(false);
    };
    const cancelSubmit = () => {
        setShowModal(false);
    };
    return (
        <div className={styles.projectFormContainer}>
            <h2>{title}</h2>
            <div className={styles.formRow}>
                <div className={styles.formGroup}>
                    <input
                        type="text"
                        name="name"
                        value={projectData.name}
                        onChange={handleChange}
                        placeholder="Project Name"
                        required
                    />
                </div>
            </div>

            <div className={styles.formRowButton}>
                <div className={styles.formGroupButton}>
                    <Button onClick={handleSubmit} variant="confirm">{buttonLabel}</Button>
                </div>
            </div>

            {showModal && (
                <ConfirmModal
                    message={confirmMessage}
                    onConfirm={confirmSubmit}
                    onCancel={cancelSubmit}
                />
            )}
        </div>
    );
};

export default ProjectForm;