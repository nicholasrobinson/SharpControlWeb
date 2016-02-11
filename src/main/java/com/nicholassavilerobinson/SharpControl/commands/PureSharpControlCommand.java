package com.nicholassavilerobinson.SharpControl.commands;

public class PureSharpControlCommand extends AbstractSharpControlCommand {

    public PureSharpControlCommand(String commandAlias, String command) {
        super(commandAlias, command);
    }

    public PureSharpControlCommand(String commandAlias, String command, Integer[] values) {
        super(commandAlias, command);
        this.setValues(values);
    }

}
