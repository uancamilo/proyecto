import { useEffect, useState, useCallback } from "react";
import { useRouter } from "next/router";

export default function Productos() {
	const API_URL = "http://localhost:8080/productos";

	const [productos, setProductos] = useState([]);
	const [loading, setLoading] = useState(true);
	const [error, setError] = useState(null);

	const router = useRouter();

	const redirectToLogin = useCallback(() => {
		localStorage.removeItem("token");
		router.push("/login");
	}, [router]);

	const fetchProductos = useCallback(async () => {
		if (typeof window === "undefined") return;

		let token = localStorage.getItem("token");
		if (!token) {
			// Espera 500ms antes de intentar leer de nuevo (puedes ajustar el tiempo)
			await new Promise((resolve) => setTimeout(resolve, 500));
			token = localStorage.getItem("token");
			if (!token) return redirectToLogin();
		}

		try {
			const response = await fetch(API_URL, {
				method: "GET",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`,
				},
			});
			if (!response.ok) {
				throw new Error("No autorizado");
			}
			const data = await response.json();
			setProductos(data);
		} catch (err) {
			console.error("Error en la solicitud:", err);
			setError(err.message);
			redirectToLogin();
		} finally {
			setLoading(false);
		}
	}, [API_URL, redirectToLogin]);


	useEffect(() => {
		fetchProductos();
	}, [fetchProductos]);
	if (loading) return <p>Cargando...</p>;

	if (error) return <p>Error: {error}</p>;

	return (
		<>
			{productos.length > 0 ? (
				<>
					<h2>Productos</h2>
					<ul>
						{productos.map((p) => (
							<li key={p.id}>{p.nombre}</li>
						))}
					</ul>
				</>
			) : (
				<p>No hay productos disponibles.</p>
			)}
		</>
	);
}
