import { useEffect, useState } from "react";
import { useRouter } from "next/router";


export default function Productos() {
	const API_URL = "http://localhost:8080/productos";
	const [productos, setProductos] = useState([]);
	const router = useRouter();

	useEffect(() => {
		const fetchProductos = async () => {
			if (typeof window === "undefined") return;

			const token = localStorage.getItem("token");
			if (!token) {
				router.push("/login"); // Redirigir si no hay token
				return;
			}

			try {
				const response = await fetch(API_URL, {
					method: "GET",
					headers: {
						"Content-Type": "application/json",
						Authorization: `Bearer ${token}`,
					},
				});

				if (response.status === 401) {
					console.error("Token inválido o expirado");
					localStorage.removeItem("token");
					router.push("/login"); // Redirigir si el token es inválido
					return;
				}

				if (!response.ok) throw new Error("Error en la petición");

				const data = await response.json();
				setProductos(data);
			} catch (error) {
				console.error("Error en la solicitud:", error);
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
