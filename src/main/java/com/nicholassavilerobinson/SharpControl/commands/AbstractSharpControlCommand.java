package com.nicholassavilerobinson.SharpControl.commands;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public abstract class AbstractSharpControlCommand {

    protected String commandAlias;
    protected String command;
    protected Integer[] values;
    protected Integer parameter;
    protected int fillCount;

    public AbstractSharpControlCommand(String commandAlias, String command) {
        this.commandAlias = commandAlias;
        this.command = command;
        values = null;
        parameter = null;
        fillCount = StringUtils.countMatches(command, "*") + StringUtils.countMatches(command, "x");
    }

    public boolean setParameter(int parameter) {
        if (!isParametric())
            return false;
        else if (values != null && !Arrays.asList(values).contains(parameter))
            return false;
        this.parameter = parameter;
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
        return command.replaceAll("(\\*|x)+", StringUtils.rightPad(Integer.toString(parameter), fillCount));
    }

    public boolean isParametric() {
        return (command.contains("*") || command.contains("x"));
    }

    public boolean isParameterSet() {
        return parameter != null;
    }

}