export const serverUrl = process.env.NODE_ENV === "development"
? "http://localhost:8080/smarticleapi" // development api
: "https://smarticleasdc.herokuapp.com/smarticleapi"; // production api