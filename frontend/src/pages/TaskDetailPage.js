import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import {useAuth} from "../hooks/authHooks";
import styles from "./TaskDetailPage.module.css"
import TaskDetail from "../components/TaskDetail";
import Header from "../components/Header";
import Button from "../components/Button";
import CreateTaskForm from "../components/CreateTaskForm";

const TaskDetailPage = () => {
    const { id } = useParams();
    const [taskDetail, setTaskDetail] = useState(null);
    const [showForm, setShowForm] = useState(false);

    useAuth();
    useEffect(() => {
        api.get(`/task/${id}`)
            .then((response) => {
                setTaskDetail(response.data);
            })
            .catch((error) => {
                console.error('Error fetching task details:', error);
            });
    }, [id]);

    if (!taskDetail) {
        return;
    }

    const handleCreateSubTask = (newTask) => {
        const subTask = {
            ...newTask,
            parentTaskId: taskDetail.id
        };

        api.post('/task', subTask)
            .then((response) => {
                setShowForm(false);
                setTaskDetail(prevDetail => ({
                    ...prevDetail,
                    subtasks: [...prevDetail.subtasks, response.data]
                }));
            })
            .catch((error) => {
                console.error("Error creating task:", error);
            });
    };

    const toggleForm = () => {
        setShowForm(!showForm);
    };

    return (
        <div className={styles.taskDetailPage}>
            <Header title="Task Detail" backButtonText="Back to the Main" backButtonPath="/main" />
            <div className={styles.content}>
                <div className={styles.buttonContainer}>
                    <Button onClick={toggleForm} variant="normal">
                        {showForm ? 'Cancel' : 'Create New Sub Task'}
                    </Button>
                </div>

                {showForm && (
                    <CreateTaskForm onCreateTask={handleCreateSubTask} />
                )}

                <TaskDetail task={taskDetail} />
            </div>
        </div>
    );
};

export default TaskDetailPage;