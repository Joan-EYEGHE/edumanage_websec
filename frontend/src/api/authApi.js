export const loginUser = async (loginData) => {
  console.log("Login data envoyée :", loginData);

  return new Promise((resolve) => {
    setTimeout(() => {
      resolve({
        accessToken: "fake-jwt-token-123456",
        tokenType: "Bearer",
        expiresIn: 3600,
        user: {
          id: "123e4567-e89b-12d3-a456-426614174000",
          nom: "Edumanage",
          prenom: "Secure",
          email: loginData.email,
          telephone: "770000000",
          actif: true,
          roleIds: [],
          roles: ["ADMIN"],
          createdAt: "2026-04-22T10:00:00",
        },
      });
    }, 1000);
  });
};