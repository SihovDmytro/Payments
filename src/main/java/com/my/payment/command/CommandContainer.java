package com.my.payment.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.TreeMap;

/**
 * Contains all command
 * @author Sihov Dmytro
 */
public class CommandContainer {
    private static final Logger LOG = LogManager.getLogger(CommandContainer.class);
    private static Map<String, Command> commands = new TreeMap<>();

    static {
        commands.put("login", new LoginCommand());
        commands.put("errorCommand", new ErrorCommand());
        commands.put("registration", new RegistrationCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("getCards", new GetCardsCommand());
        commands.put("addCard", new NewCardCommand());
        commands.put("createCard", new CreateCardCommand());
        commands.put("getPayments", new GetPaymentsForCardCommand());
        commands.put("makePayment", new MakePaymentCommand());
        commands.put("commitPayment", new CommitPaymentCommand());
        commands.put("changeCardStatus", new ChangeCardStatusCommand());
        commands.put("topUp", new TopUpCommand());
        commands.put("changeLocale", new ChangeLocaleCommand());
        commands.put("getUsers", new GetUsersCommand());
        commands.put("changeUserStatus", new ChangeUserStatusCommand());
        commands.put("cancelPayment", new CancelPaymentCommand());
        commands.put("changeCardName", new ChangeCardNameCommand());

    }

    public static Command get(String commandName) {
        if (commandName == null || !commands.containsKey(commandName)) {
            LOG.warn("Command not found ==> " + commandName);
            return commands.get("errorCommand");
        }
        return commands.get(commandName);
    }
}
