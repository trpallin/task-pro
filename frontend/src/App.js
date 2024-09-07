import "./App.css";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import SignUpPage from "./pages/SignUpPage";
import MainPage from "./pages/MainPage";
import Logout from "./components/Logout";
import TaskDetailPage from "./pages/TaskDetailPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginPage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/logout" element={<Logout />} />
                <Route path="/signup" element={<SignUpPage />} />
                <Route path="/main" element={<MainPage />} />
                <Route path="/task/:id" element={<TaskDetailPage />} />
            </Routes>
        </Router>
    );
}

export default App;