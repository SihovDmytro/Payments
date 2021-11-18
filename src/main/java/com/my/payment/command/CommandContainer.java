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
        commands.put("getUserInfo",new GetUserInfoCommand());
        commands.put("getNewCardPage",new GetNewCardPageCommand());
        commands.put("addCard",new NewCardCommand());
        commands.put("getPayments",new GetPaymentsForCardCommand());
        commands.put("makePayment",new MakePaymentCommand());
        commands.put("commitPayment",new CommitPaymentCommand());
        commands.put("blockCard",new BlockCardCommand());

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
