import Link from "next/link";

export default function Footer() {
	return (
		<>
			<hr />
			<div className="container">
				<footer className="py-5">
					<div className="flex justify-content-between row">
						<div className="col-6 col-md-2 mb-3">
							<h5>Mapa del sitio</h5>
							<ul className="nav flex-column">
								<li className="nav-item">
									<Link
										href="/"
										className="nav-link link-body-emphasis px-2 active"
										aria-current="page"
									>
										Inicio
									</Link>
								</li>
								<li className="nav-item">
									<Link
										href="/productos"
										className="nav-link link-body-emphasis px-2"
									>
										Productos
									</Link>
								</li>
								<li className="nav-item">
									<Link
										href="/contacto"
										className="nav-link link-body-emphasis px-2"
									>
										Contacto
									</Link>
								</li>
							</ul>
						</div>
						<div className="col-md-5 offset-md-1 ">
							<form className="mx-auto">
								<h5>Suscríbete a nuestro blog</h5>
								<p>
									Resumen mensual de lo nuevo y emocionante de nuestra parte.
								</p>
								<div className="d-flex flex-column flex-sm-row w-100 gap-2">
									<label htmlFor="newsletter1" className="visually-hidden">
										Correo electrónico
									</label>
									<input
										id="newsletter1"
										type="text"
										className="form-control"
										placeholder="Correo electrónico"
									/>
									<button className="btn btn-primary" type="button">
										Suscríbete
									</button>
								</div>
							</form>
						</div>
					</div>

					<div className="d-flex flex-column flex-sm-row justify-content-between py-4 my-4">
						<p>&copy; 2025 Todos los derechos reservados.</p>
						<ul className="list-unstyled d-flex">
							<li className="ms-3">
								<a className="link-body-emphasis" href="#">
									<svg className="bi" width="24" height="24">
										<use xlinkHref="#twitter" />
									</svg>
								</a>
							</li>
							<li className="ms-3">
								<a className="link-body-emphasis" href="#">
									<svg className="bi" width="24" height="24">
										<use xlinkHref="#instagram" />
									</svg>
								</a>
							</li>
							<li className="ms-3">
								<a className="link-body-emphasis" href="#">
									<svg className="bi" width="24" height="24">
										<use xlinkHref="#facebook" />
									</svg>
								</a>
							</li>
						</ul>
					</div>
				</footer>
			</div>
		</>
	);
}
