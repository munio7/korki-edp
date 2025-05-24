package org.example.korkiedp.service;

import javafx.scene.control.CheckBox;
import org.example.korkiedp.dao.TutorDAO;
import org.example.korkiedp.model.Tutor;
import org.example.korkiedp.session.CurrentSession;
import org.example.korkiedp.util.ConfigProvider;
import org.example.korkiedp.util.PasswordUtil;

public class TutorAuthService {

    public static Tutor login(String username, String password, boolean rememberMe) {
        Tutor tutor = TutorDAO.findByLogin(username);
        if (tutor == null) {
            return tutor;
        }

        if(PasswordUtil.check(password,tutor.getPasswordHash())) {
            CurrentSession.setTutor(tutor);
            if (rememberMe) {
                ConfigProvider.set("remember.login",username);
                ConfigProvider.set("remember.password",password);
            }
            return tutor;
        }
        else
            return tutor;
    }
    public static void logout() {
        CurrentSession.clear();
        ConfigProvider.clear("remember.login");
        ConfigProvider.clear("remember.password");
    }

    public static Tutor register(Tutor tutor) {
        TutorDAO.save(tutor);
        return tutor;
    }
}
