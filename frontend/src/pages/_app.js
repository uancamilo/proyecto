import "bootstrap/dist/css/bootstrap.min.css";
import Navbar from "../components/Nabvar";
import Footer from "../components/Footer";

export default function MyApp({ Component, pageProps }) {
	return (
		<>
			<Navbar />
			<Component {...pageProps} />
			<Footer />
		</>
	);
}
