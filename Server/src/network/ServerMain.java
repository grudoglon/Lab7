package network;

import database.CollectionDBManager;
import database.DBConfigure;
import database.DatabaseController;
import database.UserDBManager;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;
import utils.AppConstant;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ServerMain {

    public static void main(String[] args) {

        InetSocketAddress address = null;
        ServerSocket socket = null;
        try {
            if (args.length == 1) {
                address = new InetSocketAddress(Integer.parseInt(args[0]));
            }else{
                address = new InetSocketAddress(AppConstant.DEFAULT_PORT);
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            System.exit(-1);
        }

        try {
            socket = new ServerSocket(address);

            final DBConfigure dbConfigure = new DBConfigure();
            dbConfigure.connect();
            final CollectionDBManager collectionDBManager = new CollectionDBManager(dbConfigure.getDbConnection());
            final UserDBManager userDBManager = new UserDBManager(dbConfigure.getDbConnection());
            final DatabaseController controller = new DatabaseController(collectionDBManager, userDBManager);
            final CollectionManager collectionManager = new CollectionManager(controller.getCollectionFromDB());
            final ServerHandler serverHandler = new ServerHandler(socket, collectionManager, controller);


            if (socket.getSocket().isBound()) {
                System.out.println("Socket Successfully opened on " + address);
            }
            else {
                System.out.println("Strange behaviour trying to bind the server");
                System.exit(-1);
            }

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                serverHandler.disconnect();
                dbConfigure.disconnect();
            }));

            serverHandler.receiveFromWherever();

        } catch (SQLException | SocketException e) {
            e.printStackTrace();
        }
    }

}
