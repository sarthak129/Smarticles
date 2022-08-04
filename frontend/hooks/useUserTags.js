import { tagsService } from "service/tags.service";
import { useEffect, useState } from "react";
import useUser from "./useUser";

export default function useUserTags() {
  const user = useUser();
  const [tags, setTags] = useState([]);
  useEffect(() => {
    async function get() {
      setTags(await tagsService.getUserTags(user?.token));
    }
    get();
  }, [user?.token]);
  return tags;
}
