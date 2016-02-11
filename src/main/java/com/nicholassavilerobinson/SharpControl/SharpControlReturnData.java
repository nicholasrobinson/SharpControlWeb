package com.nicholassavilerobinson.SharpControl;

import com.nicholassavilerobinson.SharpControl.commands.AbstractSharpControlCommand;

public class SharpControlReturnData {

    public static final boolean BOOLEAN_NOT_SET = false;
    public static final int INTEGER_NOT_SET = -1;

    private final AbstractSharpControlCommand command;
    private final String response;
    private final boolean success;

    private boolean returnBoolean = BOOLEAN_NOT_SET;
    private int returnInteger = INTEGER_NOT_SET;
    private String returnString;

    public SharpControlReturnData(AbstractSharpControlCommand command, String response) {
        this.command = command;
        this.response = response;
        this.success = (response != null && !response.equals("ERR"));
        if (this.success) {
            this.returnBoolean = response.equals("1");
            try {
                this.returnInteger = Integer.parseInt(response);
            } catch(NumberFormatException e) {
                assert true; // TODO: NOOP
            }
            this.returnString = response;
        }
    }

    public AbstractSharpControlCommand getCommand() {
        return this.command;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getResponse() {
        return this.response;
    }

    public boolean getReturnBoolean() {
        return this.returnBoolean;
    }

    public int getReturnInteger() {
        return this.returnInteger;
    }

    public String getReturnString() {
        return this.returnString;
    }

}
