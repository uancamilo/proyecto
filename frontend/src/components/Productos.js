import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getToken } from "../services/authService";

export default function ProductosPage() {
	const [productos, setProductos] = useState([]);
	const navigate = useNavigate();

	useEffect(() => {
		const fetchProductos = async () => {
			try {
				const token = getToken();
				const response = await fetch("http://localhost:8080/productos", {
					headers: { Authorization: `Bearer ${token}` },
				});

				if (!response.ok) {
					throw new Error("Token inválido");
				}

				const data = await response.json();
				setProductos(data);
			} catch (error) {
				console.error(error);
				navigate("/login");
			}
		};

		fetchProductos();
	}, [navigate]);

	return (
		<div>
			<h2>Productos</h2>
			<ul>
				{productos.map((p) => (
					<li key={p.id}>{p.nombre}</li>
				))}
			</ul>
		</div>
	);
}
