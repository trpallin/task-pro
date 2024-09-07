import React from 'react';
import styles from './TaskDetail.module.css';
import TaskList from "./TaskList";

const TaskDetail = ({ task }) => {
    return (
        <div className={styles.taskDetailContainer}>
            <div className={styles.taskDetailContent}>
                <h2>{task.title}</h2>
                <p><strong>Description:</strong> {task.description}</p>
                <p><strong>Status:</strong> {task.status}</p>
                <p><strong>Priority:</strong> {task.priority}</p>
                <p><strong>Due Date:</strong> {task.dueDate}</p>
            </div>

            <div className={styles.subtaskSection}>
                <h3>Subtasks</h3>
                {task.subtasks && task.subtasks.length > 0 ? (
                    <TaskList tasks={task.subtasks} />
                ) : (
                    <p>No subtasks available</p>
                )}
            </div>
        </div>
    );
};

export default TaskDetail;