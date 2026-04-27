import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getUsers = async (params = {}) => {
  const response = await api.get("/users/all", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const getUsersList = async (params = {}) => {
  const response = await api.get("/users/all-list", { params });
  return extractPayload(response);
};

export const createUser = async (userData) => {
  const response = await api.post("/users/create", userData);
  return response.data;
};

export const updateUser = async (id, userData) => {
  const response = await api.put(`/users/${id}/update`, userData);
  return response.data;
};

export const deleteUser = async (id) => {
  const response = await api.delete(`/users/${id}/delete`);
  return response.data;
};