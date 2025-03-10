const API_URL = "http://localhost:8080/auth";

export const login = async (email, password) => {
	try {
		const response = await fetch(`${API_URL}/login`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ email, password }),
		});

		if (!response.ok)
			throw new Error("Credenciales incorrectas" + response.status);

		// Verificar el tipo de respuesta del backend
		const contentType = response.headers.get("Content-Type");
		let token = "";

		if (contentType && contentType.includes("application/json")) {
			// Si la respuesta es JSON, extraer el token de la propiedad correcta
			const data = await response.json();
			token = data.token; // Asegúrate de que el backend envíe el token en este campo
		} else {
			// Si la respuesta es texto plano (ej. "Bearer <token>")
			const text = await response.text();
			token = text.startsWith("Bearer ") ? text.split(" ")[1] : text;
		}

		if (token) {
			localStorage.setItem("token", token);
		} else {
			console.log("El token es inválido:", token);
		}

		return token;
	} catch (error) {
		console.error("Error en la autenticación:", error);
		throw error;
	}
};

export const getToken = () => {
	if (typeof window !== "undefined") {
		return localStorage.getItem("token");
	}
	return null;
};

export const logout = () => {
	if (typeof window !== "undefined") {
		localStorage.removeItem("token");
		console.log("Token eliminado de localStorage");
	}
};
