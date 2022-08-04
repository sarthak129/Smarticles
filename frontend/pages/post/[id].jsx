import React, { useState, useEffect } from "react";
import Main from "layouts/main";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faHeart } from "@fortawesome/free-solid-svg-icons";
import { faHeart as farHeart } from "@fortawesome/free-regular-svg-icons";
import { toast } from "react-toastify";
import { useRouter } from "next/router";
import moment from "moment";
import { postService } from "service/post.service";
import useUser from "hooks/useUser";
import { twitterService } from "service/twitter.service";
import { faTwitter } from "@fortawesome/free-brands-svg-icons";

library.add(faHeart, farHeart, faTwitter);
const PostDetails = () => {
  const router = useRouter();
  const user = useUser();
  const { id } = router.query;
  const onClickLike = () => {
    setLiked((prev) => !prev);
    if (liked === true) {
      toast("Disliked", {
        position: toast.POSITION.BOTTOM_LEFT,
        autoClose: 500,
      });
    } else if (liked == false) {
      toast("Liked", { position: toast.POSITION.BOTTOM_LEFT, autoClose: 500 });
    }
    return postService
      .postLike(user?.token, id)
      .then((data) => {
        if (data["statusCode"] !== 200) {
          throw new Error(JSON.stringify(data["message"]));
        }
      })
      .catch((e) => {
        console.log(e);
      });
  };
  const [liked, setLiked] = useState(false);
  const [tweets, setTweets] = useState([]);
  const [post, setPost] = useState({
    heading: "",
    content: "",
    createdOn: "",
    updatedOn: "",
    author: {
      firstname: "",
      lastname: "",
      username: "",
    },
    likes: [],
    tags: [],
  });
  useEffect(() => {
    const fetch = async () => {
      setTweets(await twitterService.getTweetDetails(user?.token, id));
      const post = await postService.getById(user?.token, id);
      const likes = post.like;
      const tags = post.tagId;
      const hasLiked = likes.find((like) => like.userName === user.username)
      if (hasLiked) setLiked(true);
      setPost({
        heading: post.heading,
        content: post.content,
        createdOn: post.creationDate,
        updatedOn: post.updationDate,
        author: {
          firstname: post.userId?.firstName,
          lastname: post.userId?.lastName,
          username: post.userId?.userName,
        },
        likes,
        tags,
      });
    };
    if (id) {
      fetch();
    }
  }, [id, user?.username, user?.token]);
  return (
    <Main>
      <div className="grid lg:grid-cols-2 lg:gap-48">
        <div className="mb-10">
          <div className="flex flex-row justify-between mb-10">
            <div className="lg:mr-10">
              <FontAwesomeIcon className="ml-1" icon="user" />
              <p className="inline text-gray-700 ml-2 font-medium text-lg">
                {post.author.firstname}&nbsp;{post.author.lastname}
              </p>
            </div>
            <div>
              <FontAwesomeIcon className="ml-1" icon="calendar" />
              <p className="inline text-gray-700 ml-2 font-medium text-lg">
                {moment(post.createdOn).format("MMM DD, YYYY")}
              </p>
            </div>
          </div>
          <div className="text-4xl my-4 text-center mb-10">{post.heading}</div>
          <article
            className="text-left mb-10"
            dangerouslySetInnerHTML={{ __html: post.content }}
          />
          <div className="mt-4 mb-5">
            {post.tags.map((tag, k) => {
              return (
                <span className="px-1.5 bg-white border-black border-2 rounded-lg mr-2" key={k}>
                  {tag.tagName}
                </span>
              );
            })}
          </div>
          {user && (
            <div>

              <FontAwesomeIcon
                className="my-3 text-2xl opacity-50 hover:opacity-100 hover:cursor-pointer"
                onClick={() => onClickLike()}
                icon={liked ? faHeart : farHeart}
              />
            </div>
          )}
        </div>
        <div className="">
          {tweets &&
            tweets.map((tweet, k) => {
              return (
                <div className="bg-gray-100 flex flex-col justify-center mb-5 lg:w-96 sm:w-52" key={k}>
                  <div className="relative py-1">
                    <div className="rounded border border-gray-300 px-6 py-1 my-1">
                      <div className="flex items-center">
                        <a
                          className="flex h-8 w-8"
                          href={tweet.tweetLink}
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          <img
                            src={tweet.userImageURL}
                            className="rounded-full"
                          />
                        </a>
                        <a
                          href={tweet.tweetLink}
                          target="_blank"
                          rel="noopener noreferrer"
                          className="flex flex-col ml-2"
                        >
                          <span
                            className="flex items-center font-bold text-gray-900 leading-5 text-sm"
                            title="{author.name}"
                          >
                            {tweet.authorName}
                            <svg
                              aria-label="Verified Account"
                              className="ml-1 text-blue-500 inline h-4 w-4"
                              viewBox="0 0 24 24"
                            >
                              <g fill="currentColor">
                                <path d="M22.5 12.5c0-1.58-.875-2.95-2.148-3.6.154-.435.238-.905.238-1.4 0-2.21-1.71-3.998-3.818-3.998-.47 0-.92.084-1.336.25C14.818 2.415 13.51 1.5 12 1.5s-2.816.917-3.437 2.25c-.415-.165-.866-.25-1.336-.25-2.11 0-3.818 1.79-3.818 4 0 .494.083.964.237 1.4-1.272.65-2.147 2.018-2.147 3.6 0 1.495.782 2.798 1.942 3.486-.02.17-.032.34-.032.514 0 2.21 1.708 4 3.818 4 .47 0 .92-.086 1.335-.25.62 1.334 1.926 2.25 3.437 2.25 1.512 0 2.818-.916 3.437-2.25.415.163.865.248 1.336.248 2.11 0 3.818-1.79 3.818-4 0-.174-.012-.344-.033-.513 1.158-.687 1.943-1.99 1.943-3.484zm-6.616-3.334l-4.334 6.5c-.145.217-.382.334-.625.334-.143 0-.288-.04-.416-.126l-.115-.094-2.415-2.415c-.293-.293-.293-.768 0-1.06s.768-.294 1.06 0l1.77 1.767 3.825-5.74c.23-.345.696-.436 1.04-.207.346.23.44.696.21 1.04z" />
                              </g>
                            </svg>
                          </span>
                        </a>
                        <FontAwesomeIcon
                          icon="fa-brands fa-twitter"
                          className="ml-3 lg:ml-auto text-blue-400 h-4 w-4"
                        />
                      </div>
                      <div className="mt-3 mb-3 leading-snug whitespace-pre-wrap text-sm text-gray-700">
                        {tweet.tweetText}
                      </div>
                      <a
                        className="text-gray-500 text-sm hover:underline"
                        href={tweet.tweetLink}
                        target="_blank"
                        rel="noopener noreferrer"
                      >
                        {" "}
                        <p>
                          {moment(tweet.creationDate).format("MMM DD, YYYY")}
                        </p>{" "}
                      </a>
                      <div className="flex text-gray-700 mt-3">
                        <a
                          className="flex items-center mr-3 text-gray-500 hover:text-green-600 transition hover:underline"
                          target="_blank"
                          rel="noopener noreferrer"
                        >
                          <svg
                            className="mr-1"
                            width="14"
                            height="14"
                            viewBox="0 0 24 24"
                          >
                            <path
                              className="fill-current"
                              d="M23.77 15.67c-.292-.293-.767-.293-1.06 0l-2.22 2.22V7.65c0-2.068-1.683-3.75-3.75-3.75h-5.85c-.414 0-.75.336-.75.75s.336.75.75.75h5.85c1.24 0 2.25 1.01 2.25 2.25v10.24l-2.22-2.22c-.293-.293-.768-.293-1.06 0s-.294.768 0 1.06l3.5 3.5c.145.147.337.22.53.22s.383-.072.53-.22l3.5-3.5c.294-.292.294-.767 0-1.06zm-10.66 3.28H7.26c-1.24 0-2.25-1.01-2.25-2.25V6.46l2.22 2.22c.148.147.34.22.532.22s.384-.073.53-.22c.293-.293.293-.768 0-1.06l-3.5-3.5c-.293-.294-.768-.294-1.06 0l-3.5 3.5c-.294.292-.294.767 0 1.06s.767.293 1.06 0l2.22-2.22V16.7c0 2.068 1.683 3.75 3.75 3.75h5.85c.414 0 .75-.336.75-.75s-.337-.75-.75-.75z"
                            />
                          </svg>
                          <span className="text-sm">{tweet.retweetCount}</span>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              );
            })}
        </div>
      </div>
    </Main>
  );
};

export default PostDetails;
