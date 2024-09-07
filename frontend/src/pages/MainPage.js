import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";
import styles from "./MainPage.module.css";
import api from "../services/api";
import CreateTaskForm from "../components/CreateTaskForm";
import Button from "../components/Button";
import {useAuth} from "../hooks/authHooks";
import TaskList from "../components/TaskList";

const MainPage = () => {
    const navigate = useNavigate();
    const [tasks, setTasks] = useState([]);
    const [showForm, setShowForm] = useState(false);

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

    const handleCreateTask = (newTask) => {
        api.post('/task', newTask)
            .then((response) => {
                setShowForm(false);
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
                    <CreateTaskForm onCreateTask={handleCreateTask} />
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