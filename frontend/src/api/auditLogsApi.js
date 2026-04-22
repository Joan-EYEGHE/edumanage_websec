import api from "./axios";
import { extractPayload, extractMetadata, extractMessage, extractStatus } from "../utils/apiResponse";

export const getAuditLogs = async (params = {}) => {
  const response = await api.get("/audit-logs", { params });

  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};