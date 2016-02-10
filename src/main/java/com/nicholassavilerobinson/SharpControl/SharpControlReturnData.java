package com.nicholassavilerobinson.SharpControl;

import com.nicholassavilerobinson.SharpControl.commands.AbstractSharpControlCommand;

public class SharpControlReturnData {

    public static final boolean BOOLEAN_NOT_SET = false;
    public static final int INTEGER_NOT_SET = -1;

    private AbstractSharpControlCommand command;
    private String response;
    private boolean success;

    private boolean returnBoolean = BOOLEAN_NOT_SET;
    private int returnInteger = INTEGER_NOT_SET;
    private String returnString;

    public SharpControlReturnData(AbstractSharpControlCommand command, String response) {
        this.command = command;
        this.response = response;
        success = (response != null && !response.equals("ERR"));
        if (success) {
            returnBoolean = response.equals("1");
            try {
                returnInteger = Integer.parseInt(response);
            } catch(NumberFormatException e) {
                assert true; // TODO: NOOP
            }
            returnString = response;
        }
    }

    public AbstractSharpControlCommand getCommand() {
        return command;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getResponse() {
        return response;
    }

    public boolean getReturnBoolean() {
        return returnBoolean;
    }

    public int getReturnInteger() {
        return returnInteger;
    }

    public String getReturnString() {
        return returnString;
    }

}
