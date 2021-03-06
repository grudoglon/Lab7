package commands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import managers.CollectionManager;
import managers.ConsoleManager;
import models.Pen;

public class RemoveIdCommand extends AbstractCommand {

    public RemoveIdCommand(){
        cmdName = "remove";
        description = "удаляет элемент из коллекции по его id";
        argCount = 1;
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            throw new InvalidValueException(e.getMessage());
        }

        Object login = databaseController.login(credentials);
        String penID = databaseController.removePen(id, credentials);
        if (penID == null && credentials.username != null && credentials.password != null && login instanceof Credentials) {
            if(collectionManager.remove(id)) consoleManager.writeln("Element with id(" + id + ") - successfully deleted");
            else consoleManager.writeln("Element with id(" + id + ") - doesn't exist");
        } else {
            consoleManager.writeln("Have some problems: " + penID);
        }

        return null;
    }
}
