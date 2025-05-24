package org.example.korkiedp.session;

import org.example.korkiedp.model.Tutor;

public class CurrentSession {

    private static Tutor loggedInTutor;

    public static void setTutor(Tutor tutor) {
        loggedInTutor = tutor;
    }

    public static Tutor getTutor() {
        System.out.println(loggedInTutor);
        return loggedInTutor;
    }

    public static int getTutorId() {
        return loggedInTutor != null ? loggedInTutor.getId() : -1;
    }

    public static void clear() {
        loggedInTutor = null;
    }

    public static boolean isLoggedIn() {
        return loggedInTutor != null;
    }
}
