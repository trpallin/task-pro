import React from 'react';
import styles from './TaskItem.module.css';

const TaskItem = ({ task, onClick }) => {
    return (
        <li className={styles.taskItem} onClick={onClick}>
            <span className={styles.taskTitle}>{task.title}</span>
            <span className={styles.taskStatus}>{task.status}</span>
        </li>
    );
};

export default TaskItem;