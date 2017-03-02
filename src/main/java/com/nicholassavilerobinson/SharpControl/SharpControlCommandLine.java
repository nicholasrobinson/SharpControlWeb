package com.nicholassavilerobinson.SharpControl;

import java.util.Arrays;

public class SharpControlCommandLine {
    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Usage: ./gradlew run -DmainClassName=com.nicholassavilerobinson.SharpControl.SharpControlCommandLine -Dexec.args=\"<SERVER> <COMMAND>\"");
            return;
        }
        final String server = args[0];
        final String[] commandParts = args.length > 2 ? Arrays.copyOfRange(args, 1, 3) : new String[]{ args[1] };
        SharpControl sc = new SharpControl(server);
        try {
            sc.connect();
            System.out.println("Sending command: " + Arrays.toString(commandParts));
            SharpControlReturnData returnData = sc.sendCommand(commandParts);
            System.out.println("Received response: " + returnData.getReturnString());
            sc.disconnect();
        } catch (SharpControlException e) {
            System.out.println(e.getMessage());
            try {
                sc.disconnect();
            } catch (SharpControlException ignored) {
            }
        }
    }
}
