package Client.Connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {
    Socket socket;
    private BufferedReader in;
    private PrintWriter out;


    public Connection(String serverAddress, int PORT){
        try {
            socket = new Socket(serverAddress, PORT);
            in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch(Exception ex){

        }
    }

    public void play(){
        //in.readLine();
        while(true) {
            out.println("TEST1");
            try {
                Thread.sleep(1000);
            }
            catch (Exception e){

            }
        }
    }
}
