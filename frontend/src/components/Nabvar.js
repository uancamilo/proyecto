import Link from "next/link";

const Navbar = () => {
	return (
		<nav className="navbar">
			<div className="navbar-brand">
				<Link href="/">MiSitio</Link>
			</div>
			<ul className="navbar-menu">
				<li className="navbar-item">
					<Link href="/login">Login</Link>
				</li>
				<li className="navbar-item">
					<Link href="/productos">Productos</Link>
				</li>
				<li className="navbar-item">
					<Link href="/contacto">Contáctenos</Link>
				</li>
			</ul>
		</nav>
	);
};

export default Navbar;
