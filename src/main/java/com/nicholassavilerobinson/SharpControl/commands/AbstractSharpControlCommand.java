package com.nicholassavilerobinson.SharpControl.commands;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public abstract class AbstractSharpControlCommand {

    protected final String commandAlias;
    protected final String command;
    protected Integer[] values;
    protected Integer parameter;
    protected final int fillCount;

    public AbstractSharpControlCommand(String commandAlias, String command) {
        this.commandAlias = commandAlias;
        this.command = command;
        this.values = null;
        this.parameter = null;
        this.fillCount = StringUtils.countMatches(command, "*") + StringUtils.countMatches(command, "x");
    }

    public boolean setParameter(int parameter) {
        if (!this.isParametric())
            return false;
        else if (this.values != null && !Arrays.asList(this.values).contains(parameter))
            return false;
        this.parameter = parameter;
        return true;
    }

    public boolean setValues(Integer[] values) {
        this.values = values;
        return true;
    }

    public String getCommandAlias() {
        return this.commandAlias;
    }

    public String getCommand() {
        if (this.isParametric() && this.isParameterSet()) {
            return this.command.replaceAll("(\\*|x)+", StringUtils.rightPad(Integer.toString(this.parameter), this.fillCount));
        } else {
            return this.command;
        }
    }

    public boolean isParametric() {
        return (this.command.contains("*") || this.command.contains("x"));
    }

    public boolean isParameterSet() {
        return this.parameter != null;
    }

}