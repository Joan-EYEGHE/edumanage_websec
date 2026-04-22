export const saveAuthData = (authResponse) => {
  localStorage.setItem("accessToken", authResponse.accessToken);
  localStorage.setItem("tokenType", authResponse.tokenType);
  localStorage.setItem("expiresIn", authResponse.expiresIn);

  if (authResponse.user) {
    localStorage.setItem("user", JSON.stringify(authResponse.user));
  }
};

export const clearAuthData = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("tokenType");
  localStorage.removeItem("expiresIn");
  localStorage.removeItem("user");
};

export const getStoredUser = () => {
  const user = localStorage.getItem("user");
  return user ? JSON.parse(user) : null;
};