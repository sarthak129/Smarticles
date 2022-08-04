import { serverUrl } from "helpers/api";
import _ from "lodash";

async function getAll() {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/tag/retriveTags`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  if (data.length === 0 || !Array.isArray(data)) return [];
  data = data
    .map((tag) => {
      return {
        label: tag.tagName,
        value: tag.id
      }
    })
    .filter((tag) => tag.label !== null);
  return data;
}
/**
 * get tag names from an array of ids
 * @param {string} token jwt token of logged in user (can be fetched from `useUser`)
 * @param {number[]} ids array of tag ids
 * @returns
 */
async function getByIds(token, ids) {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/tag/retriveTags`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${token}`
        },
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  data = data.map((tag) => {
    return {
      label: tag.tagName,
      value: tag.id
    }
  })
  data = data.filter((tag) => !!(_.intersection([tag.value], ids).length > 0))
  return data;
}

async function createNew(newTags, token) {
  const createPromises = newTags.map((tag) => {
    return fetch(`${serverUrl}/tag/createArticleTag?tagName=${tag.value}`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "jwt-token": `${token}`
      },
    })
  })
  await Promise.all(createPromises);
  const allTagLabels = (await getAll(token)).map((t) => t.label);
  const allTags = await getAll(token);
  const justCreated = newTags.map((t) => t.value);
  const newSelectedTags = _.intersection(allTagLabels, justCreated)
    .map((c) => allTags
      .find((t) => t.label === c));
  return newSelectedTags;
}

async function getUserTags(token) {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/tag/retriveTags`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${token}`
        },
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  if (data.length === 0 || !Array.isArray(data)) return [];
  data = data
    .map((tag) => {
      return {
        label: tag.tagName,
        value: tag.id
      }
    })
    .filter((tag) => tag.label !== null);
  return data;
}

export const tagsService = {
  getAll,
  getByIds,
  createNew,
  getUserTags
};
