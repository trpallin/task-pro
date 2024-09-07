import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";
import styles from "./MainPage.module.css";
import api from "../services/api";
import Button from "../components/Button";
import {useAuth} from "../hooks/authHooks";
import TaskList from "../components/TaskList";
import TaskForm from "../components/TaskForm";

const MainPage = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]);
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
        api.get('/task/tasks')
            .then((response) => {
                setTasks(response.data);
            })
            .catch((error) => {
                console.error("Error fetching tasks:", error);
            });
    }, [navigate]);

    const handleCreateTask = () => {
        api.post('/task', taskData)
            .then((response) => {
                setShowForm(false);
                setTaskData(defaultTaskData);
                setTasks(prevTasks => [...prevTasks, response.data]);
            })
            .catch((error) => {
                console.error("Error creating task:", error);
            });
    };

    const toggleForm = () => {
        setShowForm(!showForm);
    };

    return (
        <div className={styles.mainPage}>
            <Header title="Main Page" />
            <div className={styles.content}>
                <div className={styles.buttonContainer}>
                    <Button onClick={toggleForm} variant="normal">
                        {showForm ? 'Cancel' : 'Create New Task'}
                    </Button>
                </div>

                {showForm && (
                    <TaskForm
                        taskData={taskData}
                        onCreateTask={handleCreateTask}
                        handleChange={handleChange}
                        buttonLabel="Create Task"
                        confirmMessage="Are you sure you want to create task?"
                    />
                )}

                <div className={styles.dashboard}>
                    <h2>Your Tasks</h2>
                    <TaskList tasks={tasks}></TaskList>
                </div>
            </div>
        </div>
    );
};

export default MainPage;