import Link from "next/link";
import Main from "layouts/main";

const NotFound = () => {
  return (
    <Main>
      <h1 className="text-6xl text-center mt-10 mb-8">404 Error</h1>
      <h3 className="text-2xl text-center mb-2">Page Not Found</h3>
      <div className="justify-center items-center flex flex-row font-semibold">
        <Link href="/" passHref>
          <span className="hover:underline cursor-pointer">
            Back to Home Page
          </span>
        </Link>
      </div>
    </Main>
  );
};

export default NotFound;