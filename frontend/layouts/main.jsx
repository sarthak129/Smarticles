import Navbar from "components/Navbar";
import Footer from "components/Footer";
import { NextSeo } from "next-seo";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
export default function MainCentered({ children, title }) {
  return (
    <>
      <NextSeo
        titleTemplate="%s | Smarticle"
        description="Smarticle"
        title={title}
      />
      <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
        <Navbar />
        <div className="flex-1 sm:px-32 px-12 py-12">
          {title && (
            <div className="font-bold text-2xl py-6 text-center">{title}</div>
          )}
          {children}
          <ToastContainer
            position="bottom-center"
            autoClose={5000}
            hideProgressBar={false}
            newestOnTop={false}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
          />
        </div>
        <Footer />
      </div>
    </>
  );
}
