package Client.Network;

import javafx.scene.control.TextArea;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Connection {
    //socket
    private Socket socket;
    //input output readers
    private BufferedReader in;
    private PrintWriter out;
    //reading thread
    private ConnectionThread connectionThread;

    //TODO: REPLACE WITH PROPER PARSER
    TextArea messages;

    //Establishes the connection
    public Connection(String serverAddress, int PORT){
        try {
            socket = new Socket(serverAddress, PORT);
            in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connectionThread = new ConnectionThread();
        }
        catch(Exception ex){

        }
    }

    public void send(String message) {
        out.println(message);
    }

    public boolean start(){
        if (connectionThread == null){
            return false;
        }
        connectionThread.start();
        return true;
    }

    //TODO: ADD PROPER PARSER
    public void addMessageParser(TextArea messages){
        this.messages = messages;
    }

    private class ConnectionThread extends Thread{
        @Override
        public void run(){
            try{
                while (true) {
                    String message = in.readLine();
                    if (message == null){
                        //TODO: EXIT APPLICATION, CONNECTION LOST
                        return;
                    }
                    //TODO: PARSE THE MESSAGE AND THEN DECIDE WHAT TO DO
                    //messages.appendText(message);
                    System.out.println(message);

                }
            }
            catch (Exception ex){
                //messages.appendText("CONNECTION CLOSED");
            }
        }

    }
}
