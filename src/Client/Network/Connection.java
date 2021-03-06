package Client.Network;

import Client.View.View;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javafx.application.Platform;

public class Connection {
    //input output readers
    private BufferedReader in;
    private PrintWriter out;
    //reading thread
    public ConnectionThread connectionThread;

    private View current;

    private TextArea messages;

    //Establishes the connection
    public Connection(String serverAddress, int PORT){
        try {
            Socket socket = new Socket(serverAddress, PORT);
            in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            connectionThread = new ConnectionThread();
        }
        catch(Exception ignored){

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

    public void addMessageParser(TextArea messages){
        this.messages = messages;
    }

    public void setView(View view){
        current = view;
    }

    private class ConnectionThread extends Thread{
        @Override
        public void run(){
            try{
                while (true) {
                    String message = in.readLine();
                    if (message == null){
                        System.exit(1);
                        return;
                    }

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            if(current != null) {
                                current.parse(message);
                            }
                        }
                    });
                }
            }
            catch (Exception ex){
                System.out.println("Blad polaczenia");
                ex.printStackTrace();
            }
        }

    }
}
