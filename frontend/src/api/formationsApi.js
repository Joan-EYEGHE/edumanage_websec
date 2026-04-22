import api from "./axios";

export const getFormations = async () => {
  const response = await api.get("/formations");
  return response.data;
};