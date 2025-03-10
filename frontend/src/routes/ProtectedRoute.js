import { Navigate, Outlet } from "react-router-dom";
import { getToken } from "../services/authService";

export default function ProtectedRoute() {
	const token = getToken();

	return token ? <Outlet /> : <Navigate to="/login" replace />;
}
