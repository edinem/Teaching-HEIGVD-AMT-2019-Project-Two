package ch.heigvd.calendar.enums;
/**
 * Rôle dans l'application
 */
public enum Role {
    OWNER,
    EDITOR,
    VIEWER;

    public static boolean contains(String test) {

        for (Role c : Role.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
