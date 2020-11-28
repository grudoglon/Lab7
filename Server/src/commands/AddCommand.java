package commands;

import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import models.Pen;
import managers.CollectionManager;
import managers.ConsoleManager;

import java.time.LocalDate;

public class AddCommand extends AbstractCommand {

    public AddCommand(){
        cmdName = "add";
        description = "добавляет новый элемент в коллекцию";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getPen();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        Object login = databaseController.login(credentials);
        String penID = databaseController.addPen((Pen) inputData, credentials);
        if (isNumeric(penID) && credentials.username != null && credentials.password != null && login instanceof Credentials) {
            ((Pen) inputData).setId(Integer.valueOf(penID));
            collectionManager.addElement((Pen) inputData);
            consoleManager.writeln("New pen added");
        }

        inputData = null;

        return null;
    }
}
