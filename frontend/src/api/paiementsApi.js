import api from "./axios";

export const getPaiements = async () => {
  const response = await api.get("/paiements");
  return response.data;
};