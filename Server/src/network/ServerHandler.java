package network;

import commands.AbstractCommand;
import database.CollectionDBManager;
import database.Credentials;
import database.DatabaseController;
import exceptions.InvalidValueException;
import lombok.extern.slf4j.Slf4j;
import managers.CollectionManager;
import managers.ConsoleManager;
import network.packets.CommandExecutionPacket;
import network.packets.CommandPacket;
import utils.AppConstant;

import java.io.*;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.concurrent.*;

@Slf4j
public class ServerHandler {

    private class RequestReceiver extends Thread {
       @Override
            public void run() {
                while (true) {
                    try {
                        receiveData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            /**
             * Функция для получения данных
             */
            public void receiveData() throws IOException {
                SocketAddress addressFromClient = null;
                try {
                    final ByteBuffer buf = ByteBuffer.allocate(AppConstant.MESSAGE_BUFFER);
                    addressFromClient = socket.receiveDatagram(buf);
                    buf.flip();
                    final byte[] petitionBytes = new byte[buf.remaining()];
                    buf.get(petitionBytes);

                    if (petitionBytes.length > 0)
                        processRequest(petitionBytes, addressFromClient);

                    } catch (SocketTimeoutException ignored) {
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println("Weird errors, check log");
                    System.out.println("Weird errors processing the received data");
                    executeObj("Weird errors, check log. " + e.getMessage(), addressFromClient);
                }
            }


            private void processRequest(byte[] petitionBytes, SocketAddress addressFromClient) throws IOException, ClassNotFoundException {
                try (ObjectInputStream stream = new ObjectInputStream(new ByteArrayInputStream(petitionBytes))) {
                    final Object obj = stream.readObject();
                    System.out.println("received object: " + obj);
                    if (obj == null)
                        throw new ClassNotFoundException();
                    executeObj(obj, addressFromClient);
                }
            }
        }

    private final ServerSocket socket;
    private final RequestReceiver requestReceiver;
    private final ThreadPoolExecutor executor;
    private final CollectionManager collectionManager;
    private final DatabaseController databaseController;

    public ServerHandler(ServerSocket socket, CollectionManager collectionManager, DatabaseController databaseController){
        this.socket = socket;
        this.collectionManager = collectionManager;
        this.databaseController = databaseController;

        requestReceiver = new RequestReceiver();
        requestReceiver.setName("ServerReceiverThread");
        executor = (ThreadPoolExecutor)Executors.newFixedThreadPool(5);
    }

    public void receiveFromWherever() {
        requestReceiver.start();
    }


    private void executeObj(Object obj, SocketAddress addressFromClient) throws IOException {
        //Future<Object> resulted=
        executor.submit(() -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ConsoleManager consoleManager = new ConsoleManager(new InputStreamReader(System.in), new OutputStreamWriter(outputStream), false);
            Object responseExecution;
            if (obj instanceof String)
                responseExecution = obj;
            else {
                AbstractCommand command = ((CommandPacket) obj).getCommand();
                Credentials credentials = ((CommandPacket) obj).getCredentials();
                try {
                    outputStream.reset();
                    Object login = databaseController.login(credentials);
                    Object retObj = command.execute(consoleManager, collectionManager, databaseController, credentials);
                    if(retObj instanceof Credentials && login instanceof Credentials){
                        responseExecution = new CommandExecutionPacket(retObj);
                    }else if(retObj != null){
                        responseExecution = new CommandExecutionPacket(retObj);
                    }
                    else {
                        responseExecution = new CommandExecutionPacket(new String(outputStream.toByteArray()));
                    }
                } catch (InvalidValueException ex) {
                    responseExecution = ex.getMessage();
                    System.out.println(ex.getMessage());
                }
            }
            socket.sendResponse(responseExecution, addressFromClient);
            outputStream.close();
            return responseExecution;
        });

//        try {
//            System.out.println("Object gotten from executor: \n" + resulted.get().toString());
//        } catch (InterruptedException | ExecutionException e) {
//            System.out.println("Error getting result from executor");
//            System.out.println("Error getting result from executor: " + e.getMessage());
//        }
    }

    public void disconnect() {
        System.out.println("Disconnecting the server...");
        System.out.println("Disconnecting the server...");
        try {
            executor.shutdown();
            executor.awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println("Interrupted executor during shutdown");
            System.out.println("Interrupted during finishing the queued tasks");
        }
        socket.getSocket().disconnect();
        requestReceiver.interrupt();
    }
}
