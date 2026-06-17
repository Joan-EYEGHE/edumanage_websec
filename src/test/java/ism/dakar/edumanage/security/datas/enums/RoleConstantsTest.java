package ism.dakar.edumanage.security.datas.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleConstantsTest {

    @Test
    void chaqueConstanteCorrespondAUneValeurEnum() {
        assertEquals(Role.ADMINISTRATEUR.name(), Role.Constants.ADMINISTRATEUR);
        assertEquals(Role.GESTIONNAIRE.name(),   Role.Constants.GESTIONNAIRE);
        assertEquals(Role.FORMATEUR.name(),      Role.Constants.FORMATEUR);
        assertEquals(Role.APPRENANT.name(),      Role.Constants.APPRENANT);
    }
}
