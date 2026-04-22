import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getFormations = async (params = {}) => {
  const response = await api.get("/formations", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const createFormation = async (formationData) => {
  const response = await api.post("/formations", formationData);
  return response.data;
};

export const updateFormation = async (id, formationData) => {
  const response = await api.put(`/formations/${id}`, formationData);
  return response.data;
};