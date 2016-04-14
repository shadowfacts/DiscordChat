package net.shadowfacts.discordchat;

import net.shadowfacts.discordchat.commands.Command;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by James Hollowell on 4/14/2016.
 */
public class DCCommands {

    private static DCCommands instance = null;
    private Map<String, Command> commands = new HashMap<>();

    private DCCommands() {
    }

    public static DCCommands getInstance() {
        if (instance == null) {
            instance = new DCCommands();
        }
        return instance;
    }

    public Command getCommand(String name) {
        return commands.get(name);
    }

    public void registerCommand(String name, Command instance) {
        commands.put(name, instance);
    }
}
