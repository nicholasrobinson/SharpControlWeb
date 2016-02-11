package com.nicholassavilerobinson.SharpControl;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.nicholassavilerobinson.SharpControl.commands.*;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.nicholassavilerobinson.SharpControl.Utils.getJsonObjectFromFile;

public class SharpControl {

    private final SharpTV tv;

    private static final boolean DEBUG = true;
    private static final String COMMANDS_FILE = "src/main/java/com/nicholassavilerobinson/SharpControl/commands.json";
    public static final LinkedHashMap<String, AbstractSharpControlCommand> commands = readCommandsFromJsonFile();

    private static LinkedHashMap<String, AbstractSharpControlCommand> readCommandsFromJsonFile() {
        JSONObject categoriesObject = getCommandsObjectFromFile();
        JSONObject categoryObject;
        Object commandObject;
        JSONArray commandValuesArray;
        String commandClass;
        String commandAlias;
        String command;
        Integer[] commandValues;
        Constructor classConstructor;
        Iterator<?> categoryCommands;
        Iterator<?> categoriesIterator = categoriesObject.keys();
        LinkedHashMap<String, AbstractSharpControlCommand> commands = new LinkedHashMap<>();
        if (DEBUG)
            System.out.println("========");
        while (categoriesIterator.hasNext()) {
            commandClass = (String) categoriesIterator.next();
            categoryObject = (JSONObject) categoriesObject.get(commandClass);
            categoryCommands = categoryObject.keys();
            while (categoryCommands.hasNext()) {
                commandAlias = (String) categoryCommands.next();
                commandObject = categoryObject.get(commandAlias);
                command = null;
                commandValues = null;
                if (commandObject instanceof String) {
                    command = (String) commandObject;
                }
                else if (commandObject instanceof JSONObject) {
                    command = ((JSONObject) commandObject).getString("command");
                    if (((JSONObject) commandObject).has("values")) {
                        commandValuesArray = ((JSONObject) commandObject).getJSONArray("values");
                        commandValues = new Integer[commandValuesArray.length()];
                        for (int i = 0; i < commandValuesArray.length(); i++)
                            commandValues[i] = commandValuesArray.getInt(i);
                    } else if (((JSONObject) commandObject).has("range")) {
                        commandValuesArray = ((JSONObject) commandObject).getJSONArray("range");
                        commandValues = new Integer[commandValuesArray.getInt(1) - commandValuesArray.getInt(0) + 1];
                        for (int i = 0; i < commandValues.length; i++)
                            commandValues[i] = commandValuesArray.getInt(0) + i;
                    } else {
                        System.out.println("Unexpected commands file format");
                        System.exit(1);
                    }
                } else {
                    System.out.println("Unexpected commands file format");
                    System.exit(1);
                }
                try {
                    if (commandValues != null) {
                        classConstructor = Class.forName(commandClass).getConstructor(String.class, String.class, Integer[].class);
                        commands.put(commandAlias, (AbstractSharpControlCommand) classConstructor.newInstance(commandAlias, command, commandValues));
                    }
                    else {
                        classConstructor = Class.forName(commandClass).getConstructor(String.class, String.class);
                        commands.put(commandAlias, (AbstractSharpControlCommand) classConstructor.newInstance(commandAlias, command));
                    }
                    if (DEBUG)
                        System.out.println(commandAlias + " \"" + command.replace("\r", "\\r") + "\"");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
        if (DEBUG)
            System.out.println("========");
        return commands;
    }

    private static JSONObject getCommandsObjectFromFile() {
        JSONObject obj = null;
        try {
            if (DEBUG)
                System.out.println("Reading commands from file: " + COMMANDS_FILE);
            obj = getJsonObjectFromFile(COMMANDS_FILE);
        } catch (IOException e) {
            System.out.println("Commands file not found: " + COMMANDS_FILE);
            System.exit(1);
        }
        return obj;
    }

    public SharpControl(String hostname) {
        this.tv = new SharpTV(hostname);
    }

    public boolean connect() throws SharpControlException {
        try {
            return this.tv.connect();
        } catch (SharpTVException e) {
            throw new SharpControlException("Unable to connect to TV");
        }
    }

    public boolean disconnect() throws SharpControlException {
        try {
            return this.tv.disconnect();
        } catch (SharpTVException e) {
            throw new SharpControlException("Unable to disconnect from TV");
        }
    }

    public SharpControlReturnData sendCommand(String ... commandParts) throws SharpControlException {
        if (commandParts.length > 0 && commandParts.length < 3) {
            AbstractSharpControlCommand command = this.getAbstractSharpControlCommand(commandParts[0]);
            if (commandParts.length == 2) {
                try {
                    if (!command.setParameter(Integer.valueOf(commandParts[1])))
                        throw new SharpControlException("Unsupported command parameter");
                } catch (NumberFormatException ignored) {
                    throw new SharpControlException("Unsupported command parameter");
                }
            }
            return this.sendCommand(command);
        } else {
            throw new SharpControlException("Unrecognized command");
        }
    }

    private AbstractSharpControlCommand getAbstractSharpControlCommand(String commandAlias) throws SharpControlException {
        if (!this.isCommandAliasValid(commandAlias))
            throw new SharpControlException("Unrecognized command");
        return SharpControl.commands.get(commandAlias);
    }

    private SharpControlReturnData sendCommand(AbstractSharpControlCommand command) throws SharpControlException {
        if (command.isParametric() && !command.isParameterSet())
            throw new SharpControlException("Missing command parameter");
        if (DEBUG)
            System.out.println("Sending: " + command.getCommandAlias() + " \"" + command.getCommand().replace("\r", "\\r") + "\"");
        this.connect();
        SharpControlReturnData returnData;
        try {
            this.tv.write(command.getCommand());
            returnData = new SharpControlReturnData(command, this.tv.read());
        } catch (SharpTVException e) {
            throw new SharpControlException("Unable to communicate with TV");
        }
        if (DEBUG) {
            System.out.println("Received: " + returnData.getResponse());
            if (command instanceof PureSharpControlCommand) {
                System.out.println(String.format("PureSharpControlCommand Success: %b", returnData.getSuccess()));
            } else if (command instanceof BooleanSharpControlCommand) {
                System.out.println(String.format("BooleanSharpControlCommand ReturnBoolean: %b", returnData.getReturnBoolean()));
            } else if (command instanceof StringSharpControlCommand) {
                System.out.println(String.format("StringSharpControlCommand ReturnString:%s", returnData.getReturnString()));
            } else if (command instanceof IntegerSharpControlCommand) {
                System.out.println(String.format("IntegerSharpControlCommand ReturnInteger:%d", returnData.getReturnInteger()));
            }
        }
        return returnData;
    }

    private boolean isCommandAliasValid(String command) {
        return new LinkedList<>(commands.keySet()).contains(command);
    }

}
