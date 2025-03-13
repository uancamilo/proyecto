import { useEffect, useState, useCallback } from "react";
import { useRouter } from "next/router";

// Definimos el componente Productos
export default function Productos() {
	// URL de la API para obtener los productos
	const API_URL = "http://localhost:8080/productos";

	// Estados del componente
	const [productos, setProductos] = useState([]); // Guarda la lista de productos obtenida de la API
	const [loading, setLoading] = useState(true); // Indica si los datos están cargando
	const [error, setError] = useState(null); // Almacena errores si ocurren

	// Hook de enrutamiento de Next.js para redireccionar a otras páginas
	const router = useRouter();

	/**
	 * Función para redirigir al usuario a la página de login si no está autenticado.
	 * Se usa `useCallback` para evitar que la función se recree en cada render.
	 */
	const redirectToLogin = useCallback(() => {
		localStorage.removeItem("token"); // Eliminamos el token del almacenamiento local
		router.push("/login"); // Redirigimos al login
	}, [router]); // Se vuelve a crear solo si `router` cambia

	/**
	 * Función que obtiene la lista de productos desde la API.
	 * Se usa `useCallback` para evitar que se redefina en cada render.
	 */
	const fetchProductos = useCallback(async () => {
		// Evita que el código se ejecute en el servidor (Next.js puede renderizar en el servidor)
		if (typeof window === "undefined") return;

		// Obtiene el token de autenticación almacenado en el navegador
		const token = localStorage.getItem("token");

		// Si no hay token, redirige al usuario a la página de login
		if (!token) return redirectToLogin();

		try {
			// Realiza la solicitud GET a la API
			const response = await fetch(API_URL, {
				method: "GET",
				headers: {
					"Content-Type": "application/json",
					Authorization: `Bearer ${token}`, // Incluye el token en la cabecera para autenticación
				},
			});

			// Si la respuesta no es exitosa (ej. token inválido), lanza un error y redirige al login
			if (!response.ok) {
				throw new Error("No autorizado");
			}

			// Convierte la respuesta a JSON
			const data = await response.json();

			// Almacena los productos en el estado
			setProductos(data);
		} catch (err) {
			console.error("Error en la solicitud:", err); // Muestra el error en la consola
			setError(err.message); // Guarda el mensaje de error en el estado
			redirectToLogin(); // Redirige al usuario al login si ocurre un error
		} finally {
			setLoading(false); // Indica que la carga ha finalizado
		}
	}, [API_URL, redirectToLogin]); // Se vuelve a crear solo si `API_URL` o `redirectToLogin` cambian

	/**
	 * Efecto que se ejecuta cuando el componente se monta.
	 * Llama a `fetchProductos` para obtener los datos.
	 */
	useEffect(() => {
		fetchProductos();
	}, [fetchProductos]); // Se ejecuta cuando `fetchProductos` cambia

	// Si los datos están cargando, muestra un mensaje de carga
	if (loading) return <p>Cargando...</p>;

	// Si hay un error, muestra el mensaje de error
	if (error) return <p>Error: {error}</p>;

	// Renderiza la lista de productos si existen
	return (
		<div>
			<h2>Productos</h2>
			{productos.length > 0 ? (
				<ul>
					{productos.map((p) => (
						<li key={p.id}>{p.nombre}</li> // Muestra cada producto en una lista
					))}
				</ul>
			) : (
				<p>No hay productos disponibles.</p> // Mensaje si la lista está vacía
			)}
		</div>
	);
}
