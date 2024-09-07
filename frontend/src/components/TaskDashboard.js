import React from 'react';
import TaskItem from './TaskItem';
import styles from './TaskDashboard.module.css';

const TaskDashboard = ({ tasks, onTaskClick }) => {
    return (
        <div className={styles.dashboard}>
            <h2>Your Tasks</h2>
            <ul className={styles.taskList}>
                {tasks.map(task => (
                    <TaskItem key={task.id} task={task} onClick={() => onTaskClick(task.id)} />
                ))}
            </ul>
        </div>
    );
};

export default TaskDashboard;