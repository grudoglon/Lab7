package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ClearCommad extends AbstractCommand {
    public ClearCommad(){
        cmdName = "clear";
        description = "очищает коллекцию";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {

        String retDelAll = databaseController.clearPen(credentials);
        if (retDelAll == null && credentials.username != null && credentials.password != null) {
            collectionManager.clear(credentials);
            consoleManager.writeln("All elements deleted");
        }else{
            consoleManager.writeln("Problem: " + retDelAll);
        }

        return null;
    }
}


