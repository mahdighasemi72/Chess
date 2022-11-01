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
    SCOREBOARD(""),
    HELPMAINMENU("(?i)help"),
    LOGOUT("(?!)logout");
    private final Pattern commandPattern;

    public Matcher getStringMatcher(String input){
        return commandPattern.matcher(input);
    }
    ConsoleCommand(String commandPatternString) {
        this.commandPattern = Pattern.compile(commandPatternString);
    }
}
