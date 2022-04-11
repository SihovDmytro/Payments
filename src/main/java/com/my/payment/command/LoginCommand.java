package com.my.payment.command;

import com.my.payment.constants.Path;
import com.my.payment.db.DBManager;
import com.my.payment.db.Role;
import com.my.payment.db.entity.User;
import com.my.payment.util.PasswordHash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

/**
 * Login command
 *
 * @author Sihov Dmytro
 */
public class LoginCommand implements Command {
    private static final Logger LOG = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        LOG.debug("LoginCommand starts");
        HttpSession session = request.getSession();
        ResourceBundle rb = (ResourceBundle) request.getServletContext().getAttribute("resBundle");
        LOG.trace("resBundle ==> " + rb);
        String forward = Path.ERROR_PAGE;
        String login = request.getParameter("login");
        LOG.trace("Request parameter login ==> " + login);
        String password = request.getParameter("pass");
        LOG.trace("Request parameter pass ==> " + password);
        DBManager dbManager = DBManager.getInstance();
        try {
            password = PasswordHash.hash(password);
        } catch (NoSuchAlgorithmException exception) {
            LOG.warn("Cannot hash password");
            session.setAttribute("wrongData", rb.getString("message.invalidCred"));
            return Path.LOGIN_PAGE;
        }
        if (!dbManager.try2Login(login, password)) {
            LOG.trace("Cannot login");
            session.setAttribute("wrongData", rb.getString("message.invalidCred"));
            return Path.LOGIN_PAGE;
        }

        LOG.trace("Login success");
        User user = dbManager.findUser(login);
        LOG.trace("Found in DB user ==> " + user);
        Role userRole = user.getRole();
        LOG.trace("userRole ==> " + userRole);

        session.setAttribute("currUser", user);
        session.setAttribute("userRole", userRole);
        return Path.USER_CABINET;
    }
}
