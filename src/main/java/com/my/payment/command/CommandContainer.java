package com.my.payment.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {
    private static final Logger LOG = LogManager.getLogger(CommandContainer.class);
    private static Map<String, Command> commands = new TreeMap<>();
    static {
        commands.put("login",new LoginCommand());
        commands.put("errorCommand",new ErrorCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("logout",new LogoutCommand());
        commands.put("getCards",new GetCardsCommand());
    }
    public static Command get(String commandName)
    {
        if (commandName == null || !commands.containsKey(commandName)) {
            LOG.warn("Command not found ==> "+commandName);
            return commands.get("errorCommand");
        }
        return commands.get(commandName);
    }
}
