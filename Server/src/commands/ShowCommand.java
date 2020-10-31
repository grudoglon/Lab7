package commands;

import database.Credentials;
import database.DatabaseController;
import managers.CollectionManager;
import managers.ConsoleManager;

public class ShowCommand extends AbstractCommand {

    public ShowCommand(){
        cmdName = "show";
        description = "выводит все элементы коллекции";
    }

    @Override
    public Object execute(ConsoleManager consoleManager, CollectionManager collectionManager, DatabaseController databaseController, Credentials credentials) {
       if(credentials.username != null && credentials.password != null) {
           consoleManager.writeln("Count: " + collectionManager.getPenCollection().size());
           StringBuilder sb = new StringBuilder();
           collectionManager.getPenCollection().forEach(s -> sb.append(s).append("\n"));

           consoleManager.writeln(sb.toString());
       }
        return null;
    }
}
