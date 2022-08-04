import { userService } from "service/user.service";
import { useEffect, useState } from "react";

export default function useAllAuthors() {
  const [authors, setAuthors] = useState([]);
  useEffect(() => {
    async function get() {
      setAuthors(await userService.getAuthors());
    }
    get();
  }, []);
  return authors;
}
