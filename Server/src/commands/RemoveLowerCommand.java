package commands;

import database.Credentials;
import database.DatabaseController;
import models.Pen;
import managers.CollectionManager;
import managers.ConsoleManager;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
        description = "удаляет из коллекции все элементы, меньшие, чем заданный";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getPen();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        if(credentials.username != null && credentials.password != null) {
            int initSize = collectionManager.getPenCollection().size();
            collectionManager.removeLower((Pen) inputData);
            int afterSize = collectionManager.getPenCollection().size();

            consoleManager.writeln("Было удалено " + (initSize - afterSize) + " элементов");

            inputData = null;
        }
        return null;
    }
}
