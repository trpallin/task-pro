import React, { useState } from 'react';
import styles from './CreateTaskForm.module.css';
import Button from "./Button";
import ConfirmModal from "./ConfirmModal";

const CreateTaskForm = ({ onCreateTask }) => {
    const [taskData, setTaskData] = useState({
        title: '',
        description: '',
        status: 'PENDING',
        priority: 'LOW',
        dueDate: ''
    });

    const [showModal, setShowModal] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTaskData({
            ...taskData,
            [name]: value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        setShowModal(true);
    }

    const confirmSubmit = () => {
        onCreateTask(taskData);
        setShowModal(false);
        setTaskData({ title: '', description: '', status: '', priority: '', dueDate: '' });
    };

    const cancelSubmit = () => {
        setShowModal(false);
    }

    return (
        <div>
            <div className={styles.taskForm}>
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
                        <Button variant="confirm" onClick={handleSubmit}>Create Task</Button>
                    </div>
                </div>
            </div>

            {showModal && (
                <ConfirmModal
                    message="Are you sure you want to create this task?"
                    onConfirm={confirmSubmit}
                    onCancel={cancelSubmit}
                />
            )}
        </div>
    );
};

export default CreateTaskForm;