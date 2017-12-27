package Client.Map;

import Client.Network.Connection;
import Client.View.InGameView;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;


public class Map {
    //number of pawns per Player
    private int pawnNumber;
    //array with the data. Possibly should be converted to array of objects
    //we need to store starting positions and ending positions
    private int array[][];
    //the size of the array
    private int size = 0;
    private Circle circles[][];

    //radius of the circle
    private final int FIELDSIZE = 35;
    //the size of space between fields
    private final int SPACINGSIZE = 5;

    private final Connection connection;
    private final Pane grid;

    private static boolean isSent = FALSE;
    public static int move = 0;
    public static int [][]moves = new int[10][2];

    int numberOfPlayers;
    double [][]colorsOfPlayers = new double[6][3];
    int rows;

    //creates map array
    private void createMapArray(){
        //validate the number of pawns belongs to the sequence
        //1, 3, 6, 10, 15, 21 etc
        if(!checkPawnNumber()){
            updatePawnNumber();
        }
        //calculate the number of rows needed to store the pawns
        /*int */rows = calculateRowsNumber();
        //prints number of rows, and pawnNumber
        System.out.println(rows + " " + pawnNumber);

        //update the size of array
            size = rows*4 + 1;

        //create array
        array = new int[size][];
        circles = new Circle[size][];
        for(int i =0; i < size; i++){
            array[i] = new int[size];
            circles[i] = new Circle[size];
        }

        //fill square part of map with 1's
        for(int i = rows; i < size - rows; i++){
            for(int j = rows; j < size - rows; j++){
                array[i][j] = 1;
            }
        }
        //creates the blue starting positions
        //createStartingPositions(rows);
    }
    //creates the starting positions for players
    private void createStartingPositions(int rows){
        //the ⎾-like shaped
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows - i; j++) {
                array[i + rows][j + rows] = 2;
                array[i + rows * 3 + 1][j + rows] = 2;
                array[i + rows][j + rows *3 + 1] = 2;
            }
        }
        //the ⏌-like shaped
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < rows - i; j++) {
                array[rows - i + rows * 2][rows - j + rows * 2] = 2;
                array[rows - i - 1][rows - j + rows * 2] = 2;
                array[rows - i + rows * 2][rows - j - 1] = 2;

            }
        }
    }

    private void colorPositions(){

        int i = 6/numberOfPlayers;
        if(numberOfPlayers == 4){
            fillCorner(1, colorsOfPlayers[0][0], colorsOfPlayers[0][1], colorsOfPlayers[0][2]);
            fillCorner(2, colorsOfPlayers[1][0], colorsOfPlayers[1][1], colorsOfPlayers[1][2]);
            fillCorner(4, colorsOfPlayers[2][0], colorsOfPlayers[2][1], colorsOfPlayers[2][2]);
            fillCorner(5, colorsOfPlayers[3][0], colorsOfPlayers[3][1], colorsOfPlayers[3][2]);
        }
        else{
            int counter = 0;
            int x = 0;
            do{
                fillCorner(counter, colorsOfPlayers[x][0], colorsOfPlayers[x][1], colorsOfPlayers[x][2]);

                x++;
                counter += i;
            }while (counter < 6);
        }
    }

    private void fillCorner(int option, double r, double g, double b){
        int rows = calculateRowsNumber();

        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < rows - i; j++) {
                switch (option) {
                    case 0: {
                        /*map[rows - i - 1][rows - j + rows * 2].setPlayerOnField(player);
                        map[i + rows * 3 + 1][j + rows].setHomePlayer(player);*/
                        circles[rows - i - 1][rows - j + rows * 2].setFill(new Color(r, g, b, 1));
                        break;
                    }
                    case 1: {
                        /*map[i + rows][j + rows * 3 + 1].setPlayerOnField(player);
                        map[rows - i + rows * 2][rows - j - 1].setHomePlayer(player);*/
                        circles[i + rows][j + rows * 3 + 1].setFill(new Color(r, g, b, 1));
                        break;
                    }
                    case 2: {
                        /*map[rows - i + rows * 2][rows - j + rows * 2].setPlayerOnField(player);
                        map[i + rows][j + rows].setHomePlayer(player);*/
                        circles[rows - i + rows * 2][rows - j + rows * 2].setFill(new Color(r, g, b, 1));
                        break;
                    }
                    case 3: {
                        /*map[i + rows * 3 + 1][j + rows].setPlayerOnField(player);
                        map[rows - i - 1][rows - j + rows * 2].setHomePlayer(player);*/
                        circles[i + rows * 3 + 1][j + rows].setFill(new Color(r, g, b, 1));
                        break;
                    }
                    case 4: {
                        /*map[rows - i + rows * 2][rows - j - 1].setPlayerOnField(player);
                        map[i + rows][j + rows * 3 + 1].setHomePlayer(player);*/
                        circles[rows - i + rows * 2][rows - j - 1].setFill(new Color(r, g, b, 1));
                        break;
                    }
                    case 5: {
                        /*map[i + rows][j + rows].setPlayerOnField(player);
                        map[rows - i + rows * 2][rows - j + rows * 2].setHomePlayer(player);*/
                        circles[i + rows][j + rows].setFill(new Color(r, g, b, 1));
                        break;
                    }
                }
            }
        }
    }

    //displays the map
    public void display(GraphicsContext gc){


        gc.setFill(Color.GREEN);
        gc.setLineWidth(5);

        createStartingPositions(rows);
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                if(array[i][j] != 0){
                    if(array[i][j] == 2){
                        gc.setFill(Color.BLUE);
                    }

                    int dx = ((int)(Math.floor(size/2)) - j) * - (FIELDSIZE + SPACINGSIZE)/2;

                    //gc.fillOval( (FIELDSIZE + SPACINGSIZE) * (i+1) + dx, (FIELDSIZE + SPACINGSIZE) * (j+1), FIELDSIZE, FIELDSIZE);
                    circles[i][j] = new Circle((FIELDSIZE + SPACINGSIZE) * (i+1) + dx, (FIELDSIZE + SPACINGSIZE) * (j+1), FIELDSIZE/2, Color.WHITE);
                    int finalI = i;
                    int finalJ = j;
                    circles[i][j].setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println(finalI +" "+ finalJ);
                            if(!isSent){
                                moves[move][0] = finalI;
                                moves[move][1] = finalJ;
                                move++;
                            }
                        }
                    });
                    grid.getChildren().add(circles[i][j]);

                    if(array[i][j] == 2){
                        gc.setFill(Color.GREEN);
                    }
                }
            }
        }
        //draw2Dmap(gc);
        //createStartingPositions(rows);
        colorPositions();
    }
    //draws the array w/o shifting
    public void draw2Dmap(GraphicsContext gc){
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                if(array[i][j] != 0){

                    if(array[i][j] == 2){
                        gc.setFill(Color.BLUE);
                    }
                    gc.fillOval( (FIELDSIZE + SPACINGSIZE) * (i+1) + 500, (FIELDSIZE + SPACINGSIZE) * (j+1), FIELDSIZE, FIELDSIZE);
                    if(array[i][j] == 2){
                        gc.setFill(Color.GREEN);
                    }
                }
            }
        }
    }
    //calculates the rows needed to store the pawns
    private int calculateRowsNumber(){
        int pawns = pawnNumber;
        int row = 0;
        while (pawns > 0){
            row++;
            pawns = pawns - row;
        }
        return row;
    }
    //updates the pawn number so it is correct
    private void updatePawnNumber(){
        int pawns = pawnNumber;
        int row = 0;
        while (pawns > 0){
            row++;
            pawns = pawns - row;
        }
        pawnNumber = pawnNumber - pawns;
        pawnNumber = Math.max(1, pawnNumber);
    }
    //checks of the pawn number is correct
    private boolean checkPawnNumber(){
        int pawns = pawnNumber;
        int row = 0;
        while (pawns > 0){
            row++;
            pawns = pawns - row;
        }
        return (pawns == 0) && (pawnNumber > 0);
    }

    public Map(int pawnNumber, Connection cn, Pane gr, int nop, double cop[][]){
        this.pawnNumber = pawnNumber;
        this.connection = cn;
        this.grid = gr;
        this.numberOfPlayers = nop;
        this.colorsOfPlayers = cop;

        createMapArray();
    }

    public static int getMove(){
        return move;
    }

    public static int getX(int k){
        return moves[k][0];
    }

    public static int getY(int k){
        return moves[k][1];
    }

    public static void setSent(boolean t){
        isSent = t;
    }

    public static void clearMoves(){
        for(int i=0; i<move; i++){
            moves[i][0] = 0;
            moves[i][1] = 0;
            move = 0;
            isSent = TRUE;
        }
    }
}
