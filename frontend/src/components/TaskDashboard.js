import React from 'react';
import styles from './TaskDashboard.module.css';
import TaskList from "./TaskList";

const TaskDashboard = ({ tasks }) => {
    return (
        <div className={styles.dashboard}>
            <h2>Your Tasks</h2>
            <TaskList tasks={tasks}></TaskList>
        </div>
    );
};

export default TaskDashboard;