const API_URL = "http://localhost:8080/auth";

export const login = async (email, password) => {
	try {
		const response = await fetch(`${API_URL}/login`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ email, password }),
		});

		if (!response.ok) throw new Error("Credenciales incorrectas");

		const text = await response.text();

		// Si el backend devuelve "Bearer <token>", extraemos solo el token
		const token = text.startsWith("Bearer ") ? text.split(" ")[1] : text;

		if (token) {
			localStorage.setItem("token", token);
			return token;
		}

		throw new Error("No se recibió un token válido");
	} catch (error) {
		console.error("Error en la autenticación:", error);
		throw error;
	}
};

export const getToken = () => localStorage.getItem("token");

export const logout = () => localStorage.removeItem("token");
