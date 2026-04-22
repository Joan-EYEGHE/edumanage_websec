import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const backendError = error?.response?.data;

    if (backendError?.error) {
      return Promise.reject({
        status: backendError.status || backendError.error?.status,
        message:
          backendError.message ||
          backendError.error?.message ||
          "Une erreur backend est survenue.",
        details: backendError.error,
      });
    }

    return Promise.reject({
      status: "EXCEPTION",
      message: error.message || "Erreur réseau ou serveur inaccessible.",
    });
  }
);

export default api;