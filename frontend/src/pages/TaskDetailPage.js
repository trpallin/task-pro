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
    const [editMode, setEditMode] = useState(false);
    const defaultTaskData = {
        title: '',
        description: '',
        status: 'PENDING',
        priority: 'LOW',
        dueDate: ''
    };
    const [createTaskData, setCreateTaskData] = useState(defaultTaskData);
    const [updateTaskData, setUpdateTaskData] = useState(null);

    const handleCreateTaskDataChange = (e) => {
        const { name, value } = e.target;
        setCreateTaskData({
            ...createTaskData,
            [name]: value
        });
    };

    const handleUpdateTaskDataChange = (e) => {
        const { name, value } = e.target;
        setUpdateTaskData({
            ...updateTaskData,
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

    const handleCreateSubTask = () => {
        const subTask = {
            ...createTaskData,
            parentTaskId: taskDetail.id
        };

        api.post('/task', subTask)
            .then((response) => {
                setShowForm(false);
                setCreateTaskData(defaultTaskData);
                setTaskDetail(prevDetail => ({
                    ...prevDetail,
                    subtasks: [...prevDetail.subtasks, response.data]
                }));
            })
            .catch((error) => {
                console.error("Error creating task:", error);
            });
    };

    const handleUpdateTask = () => {
        api.put(`/task/${taskDetail.id}`, updateTaskData)
            .then((response) => {
                setTaskDetail(response.data);
                setEditMode(false);
            })
            .catch((error) => {
                console.error("Error updating task:", error);
            });
    };

    const toggleForm = () => {
        setShowForm(!showForm);
    };

    const toggleEditMode = () => {
        if (!editMode) {
            setUpdateTaskData({ ...taskDetail });
        } else {
            setUpdateTaskData(null);
        }
        setEditMode(!editMode);
    }

    return (
        <div className={styles.taskDetailPage}>
            <Header title="Task Detail" backButtonText="Back to the Main" backButtonPath="/main" />
            <div className={styles.content}>
                <div className={styles.buttonContainer}>
                    {!editMode && (
                        <Button onClick={toggleForm} variant="normal">
                            {showForm ? 'Cancel' : 'New Subtask'}
                        </Button>
                    )}
                    {!showForm && (
                        <Button onClick={toggleEditMode} variant="normal">
                            {editMode ? 'Cancel' : 'Edit Task'}
                        </Button>
                    )}
                </div>

                {showForm && (
                    <TaskForm
                        taskData={createTaskData}
                        onConfirm={handleCreateSubTask}
                        handleChange={handleCreateTaskDataChange}
                        buttonLabel="Create"
                        confirmMessage="Are you sure you want to create a subtask?"
                        title="Create New Subtask"
                    />
                )}

                {editMode ? (
                    <TaskForm
                        taskData={updateTaskData}
                        onConfirm={handleUpdateTask}
                        handleChange={handleUpdateTaskDataChange}
                        buttonLabel="Edit"
                        confirmMessage="Are you sure you want to update this task?"
                        title="Edit Task"
                    />
                ) : (
                    <TaskDetail task={taskDetail} />
                )}
            </div>
        </div>
    );
};

export default TaskDetailPage;