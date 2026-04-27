import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getInscriptions = async (params = {}) => {
  const response = await api.get("/inscriptions/all", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const getInscriptionsList = async (params = {}) => {
  const response = await api.get("/inscriptions/all-list", { params });
  return extractPayload(response);
};

export const createInscription = async (inscriptionData) => {
  const response = await api.post("/inscriptions/create", inscriptionData);
  return response.data;
};

export const updateInscription = async (id, inscriptionData) => {
  const response = await api.put(`/inscriptions/${id}/update`, inscriptionData);
  return response.data;
};

export const deleteInscription = async (id) => {
  const response = await api.delete(`/inscriptions/${id}/delete`);
  return response.data;
};