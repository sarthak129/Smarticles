import Main from "layouts/main";
import moment from "moment";
import Select from "react-select";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { faTwitter } from "@fortawesome/free-brands-svg-icons";
import Link from "next/link";
import { useEffect, useState } from "react";
import useUserTags from "hooks/useUserTags";
import useAllAuthors from "hooks/useAuthors";
import { postService } from "service/post.service";
import useUser from "hooks/useUser";
import CountUp from "react-countup";
import { twitterService } from "service/twitter.service";
import useTags from "hooks/useTags";

library.add(faUser);
library.add(faTwitter);
export default function Home() {
  const options = [
    { value: "Date", label: "By Date" },
    { value: "Likes", label: "By Likes" },
  ];
  const [tagcount, setTagcount] = useState([]);
  const authors = useAllAuthors();
  const preferredTags = useUserTags();
  const [posts, setPosts] = useState([]);
  const [page, setPage] = useState(0);
  const user = useUser();
  const [selectedTags, setSelectedTags] = useState([]);
  const tags = useTags();
  const [isLoading, setIsLoading] = useState(false);
  const [isFirst, setIsFirst] = useState(false);
  const [isLast, setIsLast] = useState(false);
  const [selectedAuthors, setSelectedAuthors] = useState([]);
  const [sortBy, setSortBy] = useState("creationDate");
  const handleTags = (tags) => setSelectedTags(tags);
  useEffect(() => {
    setSelectedTags(preferredTags);
  }, [preferredTags]);
  useEffect(() => {
    async function get() {
      setTagcount(await twitterService.getTweetCount(user?.token));
    }
    get();
  }, [user?.token]);
  useEffect(() => {
    async function get() {
      const token = user?.token ?? null;
      setIsLoading(true);
      const toFilterTags = selectedTags.map((t) => {
        return {
          tagName: t.label,
          id: t.value,
        };
      });
      const fetchPosts = await postService.getAll(
        token,
        toFilterTags,
        selectedAuthors,
        page,
        sortBy
      );
      setPosts(fetchPosts["content"]);
      setIsFirst(fetchPosts["first"]);
      setIsLast(fetchPosts["last"]);
      setIsLoading(false);
    }
    get();
  }, [user?.token, page, tags, selectedTags, selectedAuthors, sortBy]);
  const onClickNext = () => setPage((page) => page + 1);
  const onClickPrev = () => setPage((page) => page - 1);
  const handleAuthors = (obj) => setSelectedAuthors(obj.map(a => a.value));
  const handleSortBy = (obj) => {
    const k = obj.value;
    switch (k) {
      case "Date":
        setSortBy("creationDate");
        break;
      case "Likes":
        setSortBy("likeCount");
        break;
      default:
        setSortBy("creationDate");
    }
  };
  return (
    <Main title="Smarticle">
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 mt-10">
        <div className="lg:col-span-4 col-span-1">
          {user && (
            <div>
              {tagcount && (
                <div className="bg-gray-50 shadow-lg rounded-lg p-0 lg:p-8 pb-12 mb-8">
                  <h3 className="text-xl mb-5 font-semibold border-b pb-4">
                    <FontAwesomeIcon
                      icon="fa-brands fa-twitter"
                      className="ml-3 lg:ml-1 text-blue-400"
                    />{" "}
                    &nbsp; &nbsp;Tweet Counts
                  </h3>
                  <div>
                    {tagcount.map((tweet) => {
                      return (
                        <h1 className="font-semibold ml-3 lg:ml-1">
                          {tweet.tagName} &nbsp; &nbsp;{" "}
                          <CountUp
                            className="font-extrabold"
                            end={tweet.tweetCount}
                          />
                        </h1>
                      );
                    })}
                  </div>
                </div>
              )}
            </div>
          )}
          <div className="lg:sticky relative top-8">
            <div className="bg-gray-50 shadow-lg rounded-lg p-0 lg:p-8 pb-12 mb-8">
              <h3 className="text-xl mb-5 font-semibold border-b pb-4 ml-3 lg:ml-1">
                Filter By
              </h3>
              <div>
                <h1 className="mb-1 ml-3 lg:ml-1">Categories</h1>
                <Select
                  className="mb-5"
                  options={tags}
                  value={selectedTags}
                  isMulti
                  placeholder="Select Tags"
                  instanceId={"tags"}
                  onChange={handleTags}
                />
              </div>
              <div>
                <h1 className="mb-1 ml-3 lg:ml-1">Authors</h1>
                <Select
                  className="mb-5"
                  options={authors}
                  isMulti
                  placeholder="Select Authors"
                  instanceId={"authors"}
                  onChange={handleAuthors}
                />
              </div>
              <div>
                <h1 className="mb-1 ml-3 lg:ml-1">Sort</h1>
                <Select
                  options={options}
                  placeholder="Sort By"
                  instanceId={"tags"}
                  onChange={handleSortBy}
                />
              </div>
              {/* <div className="text-center">
                <button
                  className="text-base border-black border-2 rounded-md font-semibold hover:bg-black hover:text-white mt-10 w-20 h-10"
                  type="submit"
                >
                  Search
                </button>
              </div> */}
            </div>
          </div>
        </div>
        <div className="lg:col-span-8 col-span-1">
          {!isLoading &&
            posts &&
            posts.length > 0 &&
            posts.map((post) => (
              <div
                className="bg-white shadow-lg rounded-lg p-0 lg:p-8 pb-12 mb-8"
                key={post.id}
              >
                <div>
                  <h1 className="transition duration-700 text-center mb-5 cursor-pointer hover:text-gray-500 text-xl font-semibold">
                    <Link href={"/post/" + post.id}>{post.heading}</Link>
                  </h1>
                  <FontAwesomeIcon className="ml-3 lg:ml-1" icon="user" />
                  <p className="inline align-middle text-gray-700 ml-3 font-medium text-lg">
                    {post.userId?.firstName}&nbsp;{post.userId?.lastName}
                  </p>
                  <div className="font-medium text-gray-700 mb-5 ml-2 lg:ml-0">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      className="h-6 w-6 inline mr-2 text-pink-500"
                      fill="none"
                      viewBox="0 0 24 24"
                      stroke="currentColor"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        strokeWidth="2"
                        d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z"
                      />
                    </svg>
                    <span className="align-middle">
                      {moment(post.creationDate).format("MMM DD, YYYY")}
                    </span>
                  </div>
                  <div className="mb-16 ml-3 lg:ml-0">
                    <article
                      className="line-clamp-6"
                      dangerouslySetInnerHTML={{ __html: post.content }}
                    ></article>
                  </div>
                  <div>
                    <p className="ml-3 lg:ml-1 mb-7">
                      <FontAwesomeIcon className="ml-1" icon="thumbs-up" />{" "}
                      &nbsp; {post.like.length}
                    </p>
                    <Link href={"/post/" + post.id}>
                      <span className="ml-3 lg:ml-0 cursor-pointer transition duration-500 ease transform hover:-translate-y-1 border-black border-2 rounded-md font-normal hover:bg-black hover:text-white mt-4 p-2">
                        Continue Reading
                      </span>
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          {!isLoading && posts && posts.length === 0 && (
            <div>No more posts to display</div>
          )}
          {isLoading && <div>Loading...</div>}
          <div className="flex flex-row justify-between">
            <div
              onClick={!isFirst ? onClickPrev : () => {}}
              className={`
                ml-3
                lg:ml-0
                border-black
                border-2
                rounded-md
                font-normal
                mt-4
                p-2
                ${
                  isFirst
                    ? `
                    cursor-not-allowed
                    bg-gray-300
                  `
                    : `
                    cursor-pointer
                    transition
                    duration-500
                    ease transform
                    hover:-translate-y-1
                  hover:bg-black
                  hover:text-white
                    `
                }
              `}
            >
              prev
            </div>
            <div
              onClick={!isLast ? onClickNext : () => {}}
              className={`
                ml-3
                lg:ml-0
                border-black
                border-2
                rounded-md
                font-normal
                mt-4
                p-2
                ${
                  isLast
                    ? `
                    cursor-not-allowed
                    bg-gray-300
                  `
                    : `
                    cursor-pointer
                    transition
                    duration-500
                    ease transform
                    hover:-translate-y-1
                  hover:bg-black
                  hover:text-white
                    `
                }
              `}
            >
              next
            </div>
          </div>
        </div>
      </div>
    </Main>
  );
}
