import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getFormations = async (params = {}) => {
  const response = await api.get("/formations/all", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const getFormationsList = async (params = {}) => {
  const response = await api.get("/formations/all-list", { params });
  return extractPayload(response);
};

export const createFormation = async (formationData) => {
  const response = await api.post("/formations/create", formationData);
  return response.data;
};

export const updateFormation = async (id, formationData) => {
  const response = await api.put(`/formations/${id}/update`, formationData);
  return response.data;
};

export const deleteFormation = async (id) => {
  const response = await api.delete(`/formations/${id}/delete`);
  return response.data;
};