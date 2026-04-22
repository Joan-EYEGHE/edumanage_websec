import api from "./axios";

export const getInscriptions = async () => {
  const response = await api.get("/inscriptions");
  return response.data;
};