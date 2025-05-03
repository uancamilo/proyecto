const API_URL = "http://localhost:8080/auth";

export const login = async (email, password) => {
	try {
		const response = await fetch(`${API_URL}/login`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ email, password }),
		});
		if (!response.ok) throw new Error("Error en la autenticación");
		const data = await response.json();
		if (data.token) {
			localStorage.setItem("auth_token", data.token);
			return data.token;
		}
		return null;
	} catch (error) {
		console.error("Login error:", error);
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
