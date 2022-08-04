import { BehaviorSubject } from "rxjs";
import router from "next/router";
import { serverUrl } from "helpers/api";

const userSubject = new BehaviorSubject(
  process.browser && JSON.parse(localStorage.getItem("user"))
);

export const userService = {
  user: userSubject.asObservable(),
  get userValue() {
    return userSubject.value;
  },
  login,
  logout,
  register,
  forgot,
  reset,
  saveTags,
  updateDetails,
  getDetails,
  getAuthors,
  getVerified
};

async function login(username, password) {
  const res = await (
    await fetch(`${serverUrl}/user/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ userName: username, pswd: password }),
    })
  ).json();
  if (res["statusCode"] !== 200) {
    return res;
    // console.log(res);
    // throw new Error ("There was an error");
  }
  const token = res["data"]["jwt-token"];
  userSubject.next({
    username,
    token,
  });
  localStorage.setItem(
    "user",
    JSON.stringify({
      username,
      token,
    })
  );
  return res;
}

async function register(user) {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/user/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          firstName: user.firstName,
          lastName: user.lastName,
          userName: user.userName,
          emailID: user.emailID,
          pswd: user.pswd,
        }),
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  return data;
}

async function forgot(user) {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/user/forgotPassword`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          emailID: user.emailID,
        }),
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  return data;
}

async function reset(user) {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/user/resetPassword`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "jwt-token": `${user.token}`,
        },
        body: JSON.stringify({
          pswd: user.pswd,
        }),
      })
    ).json();
  } catch (err) {
    throw new Error(err);
  }
  return data;
}

async function logout(user) {
  let data;
  localStorage.removeItem("user");
  userSubject.next(null);
  try {
    data = await (
      await fetch(`${serverUrl}/user/logout`, {
        method: "POST",
        headers: {
          "jwt-token": `${user.token}`,
        },
      })
    ).json();
    router.push("/login");
  } catch (err) {
    throw new Error(err);
  }
  return data;
}

async function getDetails (token) {
  let res;
  res = await (
    await fetch(`${serverUrl}/user/getUserProfile`, {
      method: "GET",
      headers: {
        "jwt-token": `${token}`,
      },
    })
  ).json();
  return res;
};

async function saveTags(tags, token) {
  const res = await (
    await fetch(`${serverUrl}/user/saveUserTagPref`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "jwt-token": `${token}`
      },
      body: JSON.stringify(tags),
    })
  ).json();
  return res;
}

async function updateDetails (user, token) {
  let data;
  data = await (
    await fetch(`${serverUrl}/user/updateUserProfile`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "jwt-token": `${token}`,
      },
      body: JSON.stringify(user),
    })
  ).json();
  if (data["statusCode"] !== 200) throw new Error("Error in updating details");
  return data;
}

async function getAuthors() {
  let data;
  try {
    data = await (
      await fetch(`${serverUrl}/user/getUserDetailsPostedArticle`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      })
    ).json();
    console.log('FROM getuserdetailspsotedarticles',data);
  } catch (err) {
    throw new Error(err);
  }
  if (data.length === 0 || !Array.isArray(data)) return [];
  data = data
    .map((author) => {
      return {
        label: author.firstName + " " + author.lastName,
        value: author.id
      }
    })
    .filter((author) => author.label !== null);
  console.log("user", data);
  return data;
}

async function getVerified(vtoken) {
  let data;
  console.log(vtoken);
  try {
    data = await (
      await fetch(`${serverUrl}/user/activateAccount?token=${vtoken}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
      })
    ).json();
  } catch (err) {
    // throw new Error(err);
  }
  console.log(data);
  return data;
}