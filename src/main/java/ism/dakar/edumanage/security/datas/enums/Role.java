package ism.dakar.edumanage.security.datas.enums;

public enum Role {
    ADMINISTRATEUR,
    GESTIONNAIRE,
    FORMATEUR,
    APPRENANT;

    /**
     * Constantes String compile-time pour @PreAuthorize (SpEL exige des littéraux, pas des
     * expressions runtime comme .name()).
     *
     * ⚠️ SYNCHRONISATION MANUELLE REQUISE : ces chaînes doivent rester identiques aux noms
     * des valeurs de l'enum ci-dessus. Si tu ajoutes ou renommes une valeur dans Role, tu
     * DOIS mettre à jour cette classe ET le test RoleConstantsTest — sinon la protection
     * @PreAuthorize sera silencieusement cassée (mauvais rôle autorisé ou refus à tort).
     * Le compilateur NE détectera PAS la désynchronisation.
     */
    public static final class Constants {
        public static final String ADMINISTRATEUR = "ADMINISTRATEUR";
        public static final String GESTIONNAIRE   = "GESTIONNAIRE";
        public static final String FORMATEUR      = "FORMATEUR";
        public static final String APPRENANT      = "APPRENANT";
        private Constants() {}
    }
}
