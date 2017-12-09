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

    public void start(){
        connectionThread.start();
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
                    //TODO: PARSE THE MESSAGE AND THEN DECIDE WHAT TO DO
                    message = message + "\n";
                    messages.appendText(message);
                }
            }
            catch (Exception ex){
                messages.appendText("CONNECTION CLOSED");
            }
        }

    }
}

/*
public class Connection {
    private String IP;
    private int PORT;

    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> onReceiveCallback;

    public Connection(String IP, int PORT, Consumer<Serializable> onReceiveCallback){
        this.onReceiveCallback = onReceiveCallback;
        this.IP = IP;
        this.PORT = PORT;
        connectionThread.setDaemon(true);
    }

    public void startConnection(){
        connectionThread.start();
    }

    public void send(String data) throws Exception{
        connectionThread.output.write(data);
    }

    public void closeConnection() throws Exception{
        connectionThread.socket.close();
    }


    private class ConnectionThread extends Thread{
        private Socket socket;
        PrintWriter output;

        @Override
        public void run(){
            try(
                Socket socket = new Socket(IP, PORT);
                PrintWriter out     = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in   = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                ){
                this.socket = socket;
                this.output = out;
                socket.setTcpNoDelay(true);

                while (true) {
                    String message = in.readLine();
                    onReceiveCallback.accept(message);
                }
            }
            catch (Exception ex){
                onReceiveCallback.accept("Connection closed");
            }
        }

    }
}
*/