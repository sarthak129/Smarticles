import Link from "next/link";
import { useRouter } from "next/router";
import React, { useEffect } from "react";
import { userService } from "service/user.service";

const NotFound = () => {
  const router = useRouter();
  const { token } = router.query;
  useEffect(() => {
    async function verification() {
      await userService.getVerified(token);
    }
    verification();
  }, []);
  return (
    <div className="min-h-screen flex flex-col items-center justify-center bg-gray-100">
      <h1 className="text-5xl text-center mt-10 mb-8">
        Verification Successful
      </h1>
      <div className="justify-center items-center flex flex-row font-semibold">
        <Link href="/login" passHref>
          <span className="hover:underline cursor-pointer">Click to Login</span>
        </Link>
      </div>
    </div>
  );
};

export default NotFound;
