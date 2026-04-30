import api from "./axios";
import {
  extractPayload,
  extractMetadata,
  extractMessage,
  extractStatus,
} from "../utils/apiResponse";

export const getAuditLogs = async (params = {}) => {
  const response = await api.get("/audit-logs/all", { params });
  
  return {
    payload: extractPayload(response),
    metadata: extractMetadata(response),
    message: extractMessage(response),
    status: extractStatus(response),
  };
};

export const getAuditLogsList = async (params = {}) => {
  const response = await api.get("/audit-logs/all-list", { params });
  return extractPayload(response);
};