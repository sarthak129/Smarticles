import "../styles/globals.css";
import { userService } from "service/user.service";
import { useRouter } from "next/router";
import { useState, useEffect } from "react";
import { library } from "@fortawesome/fontawesome-svg-core";
import {
  faUser,
  faCalendar,
  faThumbsUp,
} from "@fortawesome/free-solid-svg-icons";
library.add(faUser);
library.add(faCalendar);
library.add(faThumbsUp);

function MyApp({ Component, pageProps }) {
  const router = useRouter();
  const [authorized, setAuthorized] = useState(false);
  function authCheck(url) {
    const publicPaths = ["/signup", "/login", "/", "/forgot", "/reset"];
    const path = url.split("?")[0];
    if (
      !userService.userValue &&
      !publicPaths.includes(path) &&
      !path.startsWith("/post") &&
      !path.startsWith("/verify")
    ) {
      setAuthorized(false);
      router.push({
        pathname: "/login",
      });
    } else {
      setAuthorized(true);
    }
  }
  useEffect(() => {
    authCheck(router.asPath);
    const hideContent = () => setAuthorized(false);
    router.events.on("routeChangeStart", hideContent);
    router.events.on("routeChangeComplete", authCheck);
    return () => {
      router.events.off("routeChangeStart", hideContent);
      router.events.off("routeChangeComplete", authCheck);
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);
  return authorized && <Component {...pageProps} />;
}

export default MyApp;
