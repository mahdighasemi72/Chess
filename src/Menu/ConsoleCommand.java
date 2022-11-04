package Menu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ConsoleCommand {
    REGISTER("(?i)register\\s+(.+)\\s+(.+)"),

    LOGIN("(?i)login\\s+(.+)\\s+(.+)"),
    REMOVE("(?i)remove\\s+(.+)\\s+(.+)"),
    LIST_USERS("(?i)list_users"),
    HELPREGISTERMENU("(?i)help"),
    EXIT("(?i)exit\\s*"),

    NEWGAME("(?i)new_game\\s+(.+)\\s+(-?\\d+)"),
    SCOREBOARD("scoreboard"),
    HELPMAINMENU("(?i)help"),
    LOGOUT("logout"),

    SELECT("select\\s+\\[(\\d)\\],\\[(\\d)\\]"),
    DESELECT("deselect"),
    MOVE("move\\s+\\[(\\d)\\],\\[(\\d)\\]");
    private final Pattern commandPattern;

    public Matcher getStringMatcher(String input){
        return commandPattern.matcher(input);
    }
    ConsoleCommand(String commandPatternString) {
        this.commandPattern = Pattern.compile(commandPatternString);
    }
}
