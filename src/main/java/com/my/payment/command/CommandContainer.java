package com.my.payment.command;

import java.util.Map;
import java.util.TreeMap;

public class CommandContainer {
    private static Map<String, Command> commands = new TreeMap<>();
    static {
        commands.put("login",new LoginCommand());
        commands.put("errorCommand",new ErrorCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("logout",new LogoutCommand());
    }
    public static Command get(String commandName)
    {
        if (commandName == null || !commands.containsKey(commandName)) {
            System.out.println("No such command");
            return commands.get("errorCommand");
        }
        return commands.get(commandName);
    }
}
