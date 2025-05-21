package org.example.korkiedp.service;

import org.example.korkiedp.dao.TutorDAO;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.util.PasswordUtil;

public class TutorAuthService {

    public Tutor login(String username, String password) {
        Tutor tutor = TutorDAO.findByLogin(username);
        if (tutor == null) {
            return null;
        }

        if(PasswordUtil.check(password,tutor.getPasswordHash()))
            return tutor;
        else
            return null;
    }
    public void logout(Tutor tutor) {}

    public Tutor register(Tutor tutor) {
        TutorDAO.save(tutor);
        return tutor;
    }
}
