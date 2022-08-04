import { profileService } from "service/post.service";
import { useEffect, useState } from "react";

export default function useProfile({}) {
  const [details, setDetails] = useState([]);
  useEffect(() => {
    async function fetch() {
      setDetails(await profileService.getAll());
    }
    fetch();
  }, []);
  return details;
}
