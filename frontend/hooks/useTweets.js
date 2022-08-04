import { twitterService } from "service/twitter.service";
import { useEffect, useState } from "react";

export default function useTweets({}) {
  const [tweets, setTweets] = useState([]);
  useEffect(() => {
    async function fetch() {
      setTweets(await twitterService.getTweetDetails());
    }
    fetch();
  }, []);
  return tweets;
}
