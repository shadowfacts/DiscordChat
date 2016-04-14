package net.shadowfacts.discordchat.commands;

/**
 * Created by James Hollowell on 4/14/2016.
 */
public abstract class Command {

    public abstract void doCommand(String channel, String[] args);
}
