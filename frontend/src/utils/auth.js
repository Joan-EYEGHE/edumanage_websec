export const saveAuthData = (authResponse) => {
  localStorage.setItem("accessToken", authResponse.accessToken);

  if (authResponse.expiresIn) {
    localStorage.setItem("expiresIn", authResponse.expiresIn);
  }

  if (authResponse.user) {
    localStorage.setItem("user", JSON.stringify(authResponse.user));
  }
};

export const clearAuthData = () => {
  localStorage.removeItem("accessToken");
  localStorage.removeItem("expiresIn");
  localStorage.removeItem("user");
};

export const getStoredUser = () => {
  const user = localStorage.getItem("user");
  return user ? JSON.parse(user) : null;
};

export const getAccessToken = () => {
  return localStorage.getItem("accessToken");
};