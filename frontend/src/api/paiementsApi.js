import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getPaiements = async (params = {}) => {
  const response = await api.get("/paiements", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const createPaiement = async (paiementData) => {
  const response = await api.post("/paiements", paiementData);
  return response.data;
};

export const updatePaiement = async (id, paiementData) => {
  const response = await api.put(`/paiements/${id}`, paiementData);
  return response.data;
};