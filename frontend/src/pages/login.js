import { useState } from "react";
import { useRouter } from "next/router";
import { login } from "../services/authService";

export default function Login() {
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [showModal, setShowModal] = useState(false);
	const [modalMessage, setModalMessage] = useState("Iniciando sesión...");
	const [isError, setIsError] = useState(false);
	const router = useRouter();

	const handleSubmit = async (e) => {
		e.preventDefault();
		try {
			const token = await login(email, password);
			console.log("Token:", token);
			console.log(typeof token);
			if (token && typeof token === "string" && token.startsWith("Bearer ")) {
				router.push("/productos");
			} else {
				setModalMessage("Error al iniciar sesión");
				setIsError(true);
				setShowModal(true);
			}
		} catch (error) {
			console.error("Error al iniciar sesión:", error);
		}
	};

	const handleCloseModal = () => {
		setShowModal(false);
	};

	return (
		<main className="d-flex align-items-center justify-content-center vh-100">
			<div className="form-signin w-25 m-auto">
				<form
					onSubmit={handleSubmit}
					className="p-4 border rounded shadow bg-white"
				>
					<h1 className="text-center mb-4">Ingreso de usuarios</h1>
					<div className="form-floating mb-3">
						<input
							className="form-control"
							placeholder="Correo electrónico"
							type="email"
							id="email"
							value={email}
							onChange={(e) => setEmail(e.target.value)}
							autoComplete="email"
							required
						/>
						<label htmlFor="email">Correo electrónico</label>
					</div>
					<div className="form-floating mb-3">
						<input
							className="form-control"
							placeholder="Contraseña"
							type="password"
							id="password"
							value={password}
							onChange={(e) => setPassword(e.target.value)}
							autoComplete="current-password"
							required
						/>
						<label htmlFor="password">Contraseña</label>
					</div>
					<div className="form-check text-start my-3">
						<input
							className="form-check-input"
							type="checkbox"
							value="remember-me"
							id="flexCheckDefault"
						/>
						<label className="form-check-label" htmlFor="flexCheckDefault">
							Recordar contraseña
						</label>
					</div>
					<button className="btn btn-primary w-100 py-2" type="submit">
						Ingresar
					</button>
				</form>
			</div>
			{/* Modal dinámico */}
			{showModal && (
				<>
					<div className="modal fade show d-block" tabIndex="-1">
						<div className="modal-dialog">
							<div className="modal-content">
								<div className="modal-header">
									<h1 className="modal-title fs-5">{modalMessage}</h1>
									{isError && (
										<button
											type="button"
											className="btn-close"
											onClick={handleCloseModal}
										></button>
									)}
								</div>
							</div>
						</div>
					</div>
					<div className="modal-backdrop fade show" aria-hidden="true"></div>
				</>
			)}
		</main>
	);
}
