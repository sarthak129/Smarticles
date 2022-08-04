import Link from "next/link";
import useUser from "hooks/useUser";
import { userService } from "service/user.service";

export default function Navbar() {
  const user = useUser();
  return (
    <div className="flex flex-row bg-black text-white w-full justify-between py-2 px-3 text-lg font-thin items-center lg:sticky">
      <div className="hover:underline decoration-gray-600">
        <Link href="/">Smarticle</Link>
      </div>
      {!user && (
        <div className="hover:underline decoration-gray-600">
          <Link href="/login">Login</Link>
        </div>
      )}
      {user && (
        <div className="group decoration-gray-600">
          <div className="dropdown inline-block relative">
            <button className="bg-gray-300 text-gray-700 py-1.5 px-4 rounded inline-flex items-center">
              <span className="mr-1">{user.username}</span>
              <svg
                className="fill-current h-4 w-4"
                xmlns="http://www.w3.org/2000/svg"
                viewBox="0 0 20 20"
              >
                <path d="M9.293 12.95l.707.707L15.657 8l-1.414-1.414L10 10.828 5.757 6.586 4.343 8z" />{" "}
              </svg>
            </button>
            <ul className="dropdown-menu absolute hidden group-hover:block text-gray-700 pt-1 w-full font-medium">
              <Link href="/profile" passHref>
                <li className="bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap cursor-pointer">
                  Profile
                </li>
              </Link>
              <Link href="/profile/post" passHref>
                <li className="bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap cursor-pointer">
                  Post Article
                </li>
              </Link>
              <Link href="/profile/articles" passHref>
                <li className="bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap cursor-pointer">
                  My Articles
                </li>
              </Link>
              <div onClick={() => userService.logout(user)}>
                <li className="bg-gray-200 hover:bg-gray-400 py-2 px-4 block whitespace-no-wrap cursor-pointer">
                  Logout
                </li>
              </div>
            </ul>
          </div>
        </div>
      )}
    </div>
  );
}
