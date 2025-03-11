import Link from "next/link";

export default function Navbar() {

	return (
		<>
			{/* Navbar */}
			<nav className="py-2 bg-body-tertiary border-bottom">
				<div className="container d-flex flex-wrap">
					<ul className="nav me-auto">
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
					{/* <ul className="nav">
						<li className="nav-item">
							<Link href="/login" className="nav-link link-body-emphasis px-2">
								Login
							</Link>
						</li>
						<li className="nav-item">
							<Link href="#" className="nav-link link-body-emphasis px-2">
								Sign up
							</Link>
						</li>
					</ul> */}
				</div>
			</nav>

			{/* Header */}
			{/* <header className="py-3 mb-4 border-bottom">
				<div className="container d-flex flex-wrap justify-content-center">
					<Link
						href="/"
						className="d-flex align-items-center mb-3 mb-lg-0 me-lg-auto link-body-emphasis text-decoration-none"
					>
						<svg
							className="bi me-2"
							width="40"
							height="32"
							role="img"
							aria-label="Bootstrap"
						>
							<title>Bootstrap</title>
						</svg>
						<span className="fs-4">Double header</span>
					</Link>
					<form className="col-12 col-lg-auto mb-3 mb-lg-0" role="search">
						<input
							type="search"
							className="form-control"
							placeholder="Search..."
							aria-label="Search"
						/>
					</form>
				</div>
			</header> */}
		</>
	);
}
