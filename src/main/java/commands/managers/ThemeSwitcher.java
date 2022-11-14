package commands.managers;

import commands.Command;

public class ThemeSwitcher {

    private Command command;

    public ThemeSwitcher(Command newCommand) {
        this.command = newCommand;
    }

    public void switchTheme() {
        command.execute();
    }

    public void setCommand(Command newCommand) {
        this.command = newCommand;
    }
}
