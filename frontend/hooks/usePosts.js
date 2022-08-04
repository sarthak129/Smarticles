import { postService } from "service/post.service";
import { useEffect, useState } from "react";

export default function usePosts({}) {
  const [posts, setPosts] = useState([]);
  useEffect(()=> {
    async function fetch () {
      setPosts(await postService.getAll());
    }
    fetch();
  }, []);
  return posts;
}
