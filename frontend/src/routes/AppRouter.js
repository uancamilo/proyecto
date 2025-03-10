import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "../pages/LoginPage";
import ProductosPage from "../pages/ProductosPage";
import ProtectedRoute from "./ProtectedRoute";

export default function AppRouter() {
	return (
		<Router>
			<Routes>
				<Route path="/login" element={<LoginPage />} />

				{/* Rutas protegidas */}
				<Route element={<ProtectedRoute />}>
					<Route path="/productos" element={<ProductosPage />} />
				</Route>

				<Route path="*" element={<LoginPage />} />
			</Routes>
		</Router>
	);
}
