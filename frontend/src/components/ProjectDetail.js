import styles from "./ProjectDetail.module.css";
import TaskList from "./TaskList";
import React from "react";
import Button from "./Button";

const ProjectDetail = ({ project, onClickTask }) => {
    return (
        <div className={styles.projectDetailContainer}>
            <div className={styles.projectDetailContent}>
                <h2>{project.name}</h2>
                <p><strong>create date:</strong> {project.createdAt}</p>
            </div>
            <div className={styles.taskSection}>
                <h3>Tasks</h3>
                {project.tasks && project.tasks.length > 0 ? (
                    <TaskList tasks={project.tasks} />
                ) : (
                    <div>
                        <p>No tasks</p>
                        <div className={styles.taskButtonContainer}>
                            <Button onClick={onClickTask} variant="confirm">
                                New Task
                            </Button>
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default ProjectDetail;