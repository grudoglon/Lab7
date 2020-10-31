package commands;

import database.Credentials;
import database.DatabaseController;
import models.Pen;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;

public class UpdateIdCommand extends AbstractCommand {
    public UpdateIdCommand(){
        cmdName = "update";
        description = "обновляет значение элемента коллекции, id которого равен заданному";
        argCount = 1;
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getPen();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException("Format error");
        }

        String cityID = databaseController.updatePen(id, (Pen) inputData, credentials);
        if (cityID == null && credentials.username != null && credentials.password != null) {
            if(collectionManager.update((Pen) inputData, (long)id))
                consoleManager.writeln("Element with id(" + id + ") - edited");
            else
                consoleManager.writeln("Element with id(" + id + ") - doesn't");
        } else {
            consoleManager.writeln("Have some problems: " + cityID);
        }

        inputData = null;

        return null;
    }
}
