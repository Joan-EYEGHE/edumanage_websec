export const extractPayload = (response) => {
  return response?.data?.payload ?? [];
};

export const extractMetadata = (response) => {
  return (
    response?.data?.metadata ?? {
      number: 0,
      totalElements: 0,
      size: 0,
      totalPages: 0,
    }
  );
};

export const extractMessage = (response) => {
  return response?.data?.message ?? "";
};

export const extractStatus = (response) => {
  return response?.data?.status ?? "";
};