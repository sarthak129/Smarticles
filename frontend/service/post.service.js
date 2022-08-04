import { serverUrl } from "helpers/api";

/**
 * get all posts - returns public posts if no token provided,
 * otherwise return public + private posts for the currently logged in user
 * @param {string} token jwt token of currently logged in user
 * @param {array} tags array of objects of `id` and `tagName` of the tags to filter with
 * @param {array} authors array of integers representing ids of authors to filter with
 * @param {number} page page number to fetch
 * @param {string} sortBy sort by creationDate or
 * @returns array of posts
 */
const getAll = async (token, tags, authors, page, sortBy) => {
  let res;
  const params = {
    "page": page,
    "totalPage": 3,
    "sortBy": sortBy,
    "tagList": tags,
    "userIdList": authors
  }
  const filterParam = encodeURIComponent(JSON.stringify(params));
  if (token) {
    res = await (await fetch(`${serverUrl}/article/retrieveArticle?visibility=ALL&filterParam=${filterParam}`, {
      method: "GET",
      headers: {
        "jwt-token": `${token}`,
      },
    })).json();
  } else {
    res = await (await fetch(`${serverUrl}/article/retrieveArticle?visibility=1&filterParam=${filterParam}`, {
      method: "GET",
    })).json();
  }
  if (!Array.isArray(res["content"])) return [];
  return {
    content: res["content"],
    last: res["last"],
    first: res["first"]
  };
};

const post = async (post, token) => {
  const res = await (
    await fetch(`${serverUrl}/article/postarticle`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "jwt-token": `${token}`,
      },
      body: JSON.stringify(post),
    })
  ).json();
  if (res["statusCode"] !== 200) throw new Error("Error in posting");
  return res;
};

const getById = async (token, id) => {
  let res;
  try {
    res = await (
      await fetch(`${serverUrl}/article/getArticleById?id=${id}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${token}`,
        },
      })
    ).json();
  } catch (err) {
    throw new Error("Error in fetching");
  }
  // returns an array of 1 element
  return res[0];
};

const getByAuthor = async (token, page) => {
  const totalElementsInPage = 2;
  let res;
  try {
    res = await (
      await fetch(`${serverUrl}/article/getArticleByUser?page=${page}&totalPage=${totalElementsInPage}`, {
        method: "GET",
        headers: {
          "jwt-token": `${token}`,
        },
      })
    ).json();
  } catch (err) {
    throw new Error("Error in fetching");
  }
  // returns an array of posts (along with other metadata)
  return {
    last: res["last"],
    content: res["content"],
    first: res["first"]
  };
};

const postLike = async (token, id) => {
  let res;
  try {
    res = await (
      await fetch(`${serverUrl}/article/setLike`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${token}`,
        },
        body: JSON.stringify({ id: id }),
      })
    ).json();
  } catch (err) {
    throw new Error("Error in fetching");
  }
  return res;
};

export const postService = {
  post,
  getAll,
  getById,
  getByAuthor,
  postLike,
};
