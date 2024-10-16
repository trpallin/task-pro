import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import api from "../services/api";
import { useAuth } from "../hooks/authHooks";
import styles from "./ProjectDetailPage.module.css";
import Header from "../components/Header";
import Button from "../components/Button";
import ProjectForm from "../components/ProjectForm";
import ConfirmModal from "../components/ConfirmModal";
import TaskForm from "../components/TaskForm";
import ProjectDetail from "../components/ProjectDetail";

const ProjectDetailPage = () => {
    const { projectId } = useParams();
    const [projectDetail, setProjectDetail] = useState(null);
    const [showForm, setShowForm] = useState(false);
    const [editMode, setEditMode] = useState(false);
    const [showDeleteProjectModal, setShowDeleteProjectModal] = useState(false);
    const [updateProjectData, setUpdateProjectData] = useState(null);
    const defaultTaskData = {
        title: '',
        description: '',
        status: 'PENDING',
        priority: 'LOW',
        dueDate: ''
    };
    const [createTaskData, setCreateTaskData] = useState(defaultTaskData);

    useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        api.get(`/project/${projectId}`)
            .then((response) => {
                setProjectDetail(response.data);
            })
            .catch((error) => {
                console.error('Error fetching project details:', error);
            });
    }, [projectId]);

    if (!projectDetail) {
        return;
    }

    const handleUpdateProject = () => {
        api.put(`/project/${projectDetail.id}`, updateProjectData)
            .then((response) => {
                setProjectDetail(updateProjectData);
                setEditMode(false);
            })
            .catch((error) => {
                console.error("Error updating project:", error);
            });
    };

    const handleDeleteProject = () => {
        api.delete(`/project/${projectDetail.id}`)
            .then(() => {
                navigate('/main');
            })
            .catch((error) => {
                console.error("Error deleting project:", error);
            });
    };

    const handleUpdateProjectDataChange = (e) => {
        const { name, value } = e.target;
        setUpdateProjectData({
            ...updateProjectData,
            [name]: value
        });
    };

    const toggleForm = () => {
        setShowForm(!showForm);
    };

    const setShowFormTrue = () => {
        setShowForm(true);
    };

    const toggleEditMode = () => {
        if (!editMode) {
            setUpdateProjectData({ ...projectDetail });
        } else {
            setUpdateProjectData(null);
        }
        setEditMode(!editMode);
    };

    const toggleShowDeleteProjectModal = () => {
        setShowDeleteProjectModal(!showDeleteProjectModal);
    };

    const handleCreateTask = () => {
        const taskData = {
            ...createTaskData,
            projectId: projectId,
        };
        api.post('/task', taskData)
            .then((response) => {
                setShowForm(false);
                setCreateTaskData(defaultTaskData);
                setProjectDetail(prevDetail => ({
                    ...prevDetail,
                    tasks: [...prevDetail.tasks, response.data]
                }));
            })
            .catch((error) => {
                console.error("Error creating task:", error);
            });
    };

    const handleCreateTaskDataChange = (e) => {
        const { name, value } = e.target;
        setCreateTaskData({
            ...createTaskData,
            [name]: value
        });
    };

    return (
        <div className={styles.projectDetailPage}>
            <Header title="Project Detail" backButtonText="Back to the Main" backButtonPath="/main" />
            <div className={styles.content}>
                <div className={styles.buttonContainer}>
                    {!editMode && (
                        <Button onClick={toggleForm} variant="normal">
                            {showForm ? 'Cancel' : 'New Task'}
                        </Button>
                    )}
                    {!showForm && (
                        <Button onClick={toggleEditMode} variant="normal">
                            {editMode ? 'Cancel' : 'Edit Project'}
                        </Button>
                    )}
                    {!(editMode || showForm) && (
                        <Button onClick={toggleShowDeleteProjectModal} variant="cancel">Delete Project</Button>
                    )}
                </div>

                {showForm && (
                    <TaskForm
                        taskData={createTaskData}
                        onConfirm={handleCreateTask}
                        handleChange={handleCreateTaskDataChange}
                        buttonLabel="Create"
                        confirmMessage="Are you sure you want to create a subtask?"
                        title="Create New Subtask"
                    />
                )}

                {editMode ? (
                    <ProjectForm
                        projectData={updateProjectData}
                        onConfirm={handleUpdateProject}
                        handleChange={handleUpdateProjectDataChange}
                        buttonLabel="Edit"
                        confirmMessage="Are you sure you want to update this project?"
                        title="Edit Project"
                    />
                ) : (
                    <ProjectDetail project={projectDetail} onClickTask={setShowFormTrue} />
                )}
            </div>

            {showDeleteProjectModal && (
                <ConfirmModal
                    message="Are you sure you want to delete this project? All associated tasks will be deleted as well."
                    onConfirm={handleDeleteProject}
                    onCancel={toggleShowDeleteProjectModal}
                    warning={true}
                />
            )}
        </div>
    );
};

export default ProjectDetailPage;