import { useEffect, useState } from "react";
import { useRouter } from "next/router";

export default function Productos() {
	const API_URL = "http://localhost:8080/productos";
	const [productos, setProductos] = useState([]);
	const router = useRouter();

	useEffect(() => {
		const redirectToLogin = () => {
			localStorage.removeItem("token");
			router.push("/login");
		};

		const fetchProductos = async () => {
			if (typeof window === "undefined") return;

			const token = localStorage.getItem("token");
			if (!token) return redirectToLogin();

			try {
				const response = await fetch(API_URL, {
					method: "GET",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${token}`,
					},
				});

				if (!response.ok) return redirectToLogin();

				const data = await response.json();
				setProductos(data);
			} catch (error) {
				console.error("Error en la solicitud:", error);
				redirectToLogin();
			}
		};

		fetchProductos();
	}, [router]);

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
