import React from 'react';
import styles from "./TaskList.module.css";
import TaskItem from "./TaskItem";

const TaskList = ({ tasks }) => {
    return (
        <div>
            <ul className={styles.taskList}>
                {tasks.map(task => (
                    <TaskItem key={task.id} task={task} />
                ))}
            </ul>
        </div>
    )
}

export default TaskList;