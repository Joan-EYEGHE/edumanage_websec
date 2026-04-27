export const extractPayload = (response) => {
  return response?.data?.payload ?? [];
};

export const extractMetadata = (response) => {
  const metadata = response?.data?.metadata ?? {};

  return {
    number: metadata.number ?? metadata.currentPage ?? 0,
    totalElements: metadata.totalElements ?? 0,
    size: metadata.size ?? 10,
    totalPages: metadata.totalPages ?? 0,
  };
};

export const extractMessage = (response) => {
  return response?.data?.message ?? "";
};

export const extractStatus = (response) => {
  return response?.data?.status ?? "";
};