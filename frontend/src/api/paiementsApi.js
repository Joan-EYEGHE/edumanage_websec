import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getPaiements = async (params = {}) => {
  const response = await api.get("/paiements/all", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const getPaiementsList = async (params = {}) => {
  const response = await api.get("/paiements/all-list", { params });
  return extractPayload(response);
};

export const createPaiement = async (paiementData) => {
  const response = await api.post("/paiements/create", paiementData);
  return response.data;
};

export const updatePaiement = async (id, paiementData) => {
  const response = await api.put(`/paiements/${id}/update`, paiementData);
  return response.data;
};

export const deletePaiement = async (id) => {
  const response = await api.delete(`/paiements/${id}/delete`);
  return response.data;
};