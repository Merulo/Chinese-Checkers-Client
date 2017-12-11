package Client.Network;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;

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

    //Incoming messages parser
    public void parseMessage(String msg, Label[] lGames){
        String[] tmp = msg.split(";");

        for(int i=0; i<tmp.length; i++)
            System.out.println(tmp[i]);

        if(tmp[0].equals("GameData")){
            if(tmp.length==6){
                int game = Integer.parseInt(tmp[1]);

                String info = new String(tmp[1]);
                info = info.concat(" ");
                info = info.concat(tmp[3]+"/"+tmp[4]);
                lGames[game].setText(info);

                if(tmp[5].equals("Open"))
                    lGames[game].setTextFill(Color.GREEN);
                else if(tmp[5].equals("Playing"))
                    lGames[game].setTextFill(Color.RED);
                else if(tmp[5].equals("Ready to start"))
                    lGames[game].setTextFill(Color.YELLOW);
                else if(tmp[5].equals("Restarting"))
                    lGames[game].setTextFill(Color.BLUE);
            }
            else
                System.out.println("Too small amount of parameters in GameData");
        }
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
                    //System.out.println(message);
                    //Into hub:
                    parseMessage(message);

                }
            }
            catch (Exception ex){
                //messages.appendText("CONNECTION CLOSED");
            }
        }

    }
}
