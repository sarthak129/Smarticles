import { userService } from "service/user.service";
import { useEffect, useState } from "react";

export default function useUser() {
  const [user, setUser] = useState({});
  useEffect(() => {
    const subscription = userService.user.subscribe(x => setUser(x));
    return () => subscription.unsubscribe();
  }, [])
  return user;
}