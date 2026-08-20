export const ROLES = {
  ADMINISTRATEUR: "ADMINISTRATEUR",
  GESTIONNAIRE: "GESTIONNAIRE",
  FORMATEUR: "FORMATEUR",
  APPRENANT: "APPRENANT",
};

export const hasRole = (user, role) => {
  return Array.isArray(user?.roles) && user.roles.includes(role);
};

export const hasAnyRole = (user, roles = []) => {
  return Array.isArray(user?.roles) && roles.some((role) => user.roles.includes(role));
};