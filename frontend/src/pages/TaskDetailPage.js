import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";
import {useAuth} from "../hooks/authHooks";
import styles from "./TaskDetailPage.module.css"
import TaskDetail from "../components/TaskDetail";
import Header from "../components/Header";
import Button from "../components/Button";
import TaskForm from "../components/TaskForm";

const TaskDetailPage = () => {
    const { id } = useParams();
    const [taskDetail, setTaskDetail] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const defaultTaskData = {
        title: '',
        description: '',
        status: 'PENDING',
        priority: 'LOW',
        dueDate: ''
    };
    const [taskData, setTaskData] = useState(defaultTaskData);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setTaskData({
            ...taskData,
            [name]: value
        });
    };

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
                    <TaskForm
                        taskData={taskData}
                        onCreateTask={handleCreateSubTask}
                        handleChange={handleChange}
                        buttonLabel="Create Subtask"
                        confirmMessage="Are you sure you want to create subtask?"
                    />
                )}

                <TaskDetail task={taskDetail} />
            </div>
        </div>
    );
};

export default TaskDetailPage;