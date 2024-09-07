import React from 'react';
import styles from './TaskItem.module.css';
import {useNavigate} from "react-router-dom";

const TaskItem = ({ task }) => {
    const navigate = useNavigate();
    const handleTaskClick = () => {
        navigate(`/task/${task.id}`);
    };
    return (
        <li className={styles.taskItem} onClick={handleTaskClick}>
            <span className={styles.taskTitle}>{task.title}</span>
            <span className={styles.taskStatus}>{task.status}</span>
        </li>
    );
};

export default TaskItem;