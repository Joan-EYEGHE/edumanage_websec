export const getReadableError = (error) => {
  const status = error?.status || "";
  const message = error?.message || "Une erreur est survenue.";

  const customMessages = {
    DUPLICATE_EMAIL: "Cet email existe déjà.",
    DUPLICATE_TELEPHONE: "Ce numéro existe déjà.",
    DUPLICATE_REFERENCE: "Cette référence existe déjà.",
    INVALID_CREDENTIALS: "Identifiants incorrects.",
    ACCESS_DENIED: "Accès refusé.",
    UNAUTHORIZED: "Session invalide ou expirée.",
    NOT_FOUND: "Ressource introuvable.",
    VALIDATION_EXCEPTION: "Veuillez vérifier les champs saisis.",
    BAD_REQUEST: "Requête invalide.",
  };

  return customMessages[status] || message;
};