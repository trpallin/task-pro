import React, {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import Header from "../components/Header";
import styles from "./MainPage.module.css";
import api from "../services/api";
import Button from "../components/Button";
import {useAuth} from "../hooks/authHooks";
import ProjectForm from "../components/ProjectForm";

const MainPage = () => {
    const navigate = useNavigate();
    const [projects, setProjects] = useState([]);
    const [showForm, setShowForm] = useState(false);
    const [createProjectData, setCreateProjectData] = useState({ name: ''});

    const handleChange = (e) => {
        const { name, value } = e.target;
        setCreateProjectData({
            ...createProjectData,
            [name]: value
        });
    };

    useAuth();
    useEffect(() => {
        api.get('/project')
            .then((response) => {
                setProjects(response.data);
            })
            .catch((error) => {
                console.error("Error fetching tasks:", error);
            });
    }, [navigate]);

    const handleCreateProject = () => {
        api.post('/project', createProjectData)
            .then((response) => {
                setShowForm(false);
                setCreateProjectData({ name: '' });
                setProjects(prevProjects => [...prevProjects, response.data]);
            })
            .catch((error) => {
                console.error("Error creating task:", error);
            });
    };

    const handleProjectClick = (projectId) => {
        navigate(`/project/${projectId}`);
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
                        {showForm ? 'Cancel' : 'New Project'}
                    </Button>
                </div>

                {showForm && (
                    <ProjectForm
                        projectData={createProjectData}
                        onConfirm={handleCreateProject}
                        handleChange={handleChange}
                        buttonLabel="Create"
                        confirmMessage="Are you sure you want to create a project?"
                        title="Create Project"
                    />
                )}

                <div className={styles.dashboard}>
                    <h2>Your Projects</h2>
                    <ul className={styles.projectList}>
                        {projects.map(project => (
                            <li
                                key={project.id}
                                className={styles.projectItem}
                                onClick={() => handleProjectClick(project.id)}
                            >
                                {project.name}
                            </li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
};

export default MainPage;