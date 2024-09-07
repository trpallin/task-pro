import React from "react";
import styles from "./TaskForm.module.css";
import Button from "./Button";
import ConfirmModal from "./ConfirmModal";
import {useState} from "react";

const TaskForm = ({ taskData, onCreateTask, handleChange, buttonLabel, confirmMessage, title }) => {
    const [showModal, setShowModal] = useState(false);
    const handleSubmit = () => {
        setShowModal(true);
    };
    const confirmSubmit = () => {
        onCreateTask();
        setShowModal(false);
    };
    const cancelSubmit = () => {
        setShowModal(false);
    };

    return (
        <>
            <div className={styles.taskForm}>
                <h2>{title}</h2>
                <div className={styles.formRow}>
                    <div className={styles.formGroup}>
                        <label htmlFor="title">Title</label>
                        <input
                            type="text"
                            name="title"
                            value={taskData.title}
                            onChange={handleChange}
                            required
                        />
                    </div>
                </div>

                <div className={styles.formRow}>
                    <div className={styles.formGroup}>
                        <label htmlFor="description">Description</label>
                        <textarea
                            name="description"
                            value={taskData.description}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                <div className={styles.formRow}>
                    <div className={styles.formGroup}>
                        <label htmlFor="status">Status</label>
                        <select
                            name="status"
                            value={taskData.status}
                            onChange={handleChange}
                        >
                            <option value="PENDING">Pending</option>
                            <option value="IN_PROGRESS">In Progress</option>
                            <option value="COMPLETED">Completed</option>
                        </select>
                    </div>

                    <div className={styles.formGroup}>
                        <label htmlFor="priority">Priority</label>
                        <select
                            name="priority"
                            value={taskData.priority}
                            onChange={handleChange}
                        >
                            <option value="LOW">Low</option>
                            <option value="MEDIUM">Medium</option>
                            <option value="HIGH">High</option>
                        </select>
                    </div>

                    <div className={styles.formGroup}>
                        <label htmlFor="dueDate">Due Date</label>
                        <input
                            type="date"
                            name="dueDate"
                            value={taskData.dueDate}
                            onChange={handleChange}
                        />
                    </div>
                </div>

                <div className={styles.formRowButton}>
                    <div className={styles.formGroupButton}>
                        <Button onClick={handleSubmit} variant="confirm">{buttonLabel}</Button>
                    </div>
                </div>
            </div>

            {showModal && (
                <ConfirmModal
                    message={confirmMessage}
                    onConfirm={confirmSubmit}
                    onCancel={cancelSubmit}
                />
            )}
        </>
    );
};

export default TaskForm;