package commands;

import database.Credentials;
import database.DatabaseController;
import models.Pen;
import managers.CollectionManager;
import managers.ConsoleManager;

import java.util.ArrayList;

public class RemoveLowerCommand extends AbstractCommand {
    public RemoveLowerCommand(){
        cmdName = "remove_lower";
        description = "удаляет из коллекции все элементы, меньшие, чем заданный";
        needInput = true;
    }

    @Override
    public Object getInput(ConsoleManager consoleManager){
        return consoleManager.getRemoveLowerPen();
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
        if(needInput && inputData == null) inputData = this.getInput(consoleManager);
        if(credentials.username != null && credentials.password != null && databaseController.login(credentials) instanceof Credentials) {
            int initSize = collectionManager.getPenCollection().size();
            ArrayList<Integer> arr = collectionManager.removeLower((Pen) inputData);
            for(Integer id : arr){ databaseController.removePen(id, credentials); }
            int afterSize = collectionManager.getPenCollection().size();

            consoleManager.writeln("Было удалено " + (initSize - afterSize) + " элементов");

            inputData = null;
        }
        return null;
    }
}
