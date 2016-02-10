package com.nicholassavilerobinson.SharpControl.commands;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public abstract class AbstractSharpControlCommand {

    protected String commandAlias;
    protected String command;
    protected Integer[] values;

    public AbstractSharpControlCommand(String commandAlias, String command) {
        this.commandAlias = commandAlias;
        this.command = command;
        values = null;
    }

    public boolean setParameter(int parameter) {
        if (!command.contains("*") && !command.contains("x"))
            return false;
        else if (values != null && !Arrays.asList(values).contains(parameter))
            return false;
        int fillCount = 0;
        if (command.contains("*"))
            fillCount = StringUtils.countMatches(command, "*");
        else if (command.contains("x"))
            fillCount = StringUtils.countMatches(command, "x");
        command = command.replaceAll("(\\*|x)+", StringUtils.rightPad(Integer.toString(parameter), fillCount));
        return true;
    }

    public boolean setValues(Integer[] values) {
        this.values = values;
        return true;
    }

    public String getCommandAlias() {
        return commandAlias;
    }

    public String getCommand() {
        return command;
    }

    public boolean isParametric() {
        return (command.contains("*") || command.contains("x"));
    }

}