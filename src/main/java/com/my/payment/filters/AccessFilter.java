package com.my.payment.filters;

import com.my.payment.constants.Path;
import com.my.payment.db.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

public class AccessFilter implements Filter {
    private static final Logger LOG = LogManager.getLogger(AccessFilter.class);
    private Map<Role, List<String>> accessMap = new HashMap<Role, List<String>>();
    private List<String> commons = new ArrayList<String>();
    private List<String> outOfControl = new ArrayList<String>();
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        LOG.debug("Filter initialization starts");
        accessMap.put(Role.ADMIN, asList(fConfig.getInitParameter("admin")));
        accessMap.put(Role.USER, asList(fConfig.getInitParameter("user")));
        LOG.trace("Access map --> " + accessMap);
        commons = asList(fConfig.getInitParameter("common"));
        LOG.trace("Common commands --> " + commons);
        outOfControl = asList(fConfig.getInitParameter("out-of-control"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOG.debug("Filter starts");
        if (checkAccess(request)) {
            LOG.debug("Filter finished");
            chain.doFilter(request, response);
        } else {
            String errorMessage = "You do not have permission to access the requested resource";
            request.setAttribute("errorMessage", errorMessage);
            LOG.trace("Set the request attribute: errorMessage --> " + errorMessage);

            request.getRequestDispatcher(Path.ERROR_PAGE)
                    .forward(request, response);
        }
    }
    private boolean checkAccess(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String commandName = request.getParameter("command");
        if (commandName == null || commandName.isEmpty()) {
            return false;
        }
        if (outOfControl.contains(commandName)) {
            return true;
        }
        HttpSession session = httpRequest.getSession(false);
        if (session == null) {
            return false;
        }
        Role userRole = (Role)session.getAttribute("userRole");
        if (userRole == null) {
            return false;
        }
        return accessMap.get(userRole).contains(commandName) || commons.contains(commandName);
    }
    private List<String> asList(String str) {
        List<String> list = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
        return list;
    }

}