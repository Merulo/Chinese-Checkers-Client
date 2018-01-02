package Client.Map;

import Client.Network.Connection;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

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

    private final Pane grid;

    private static boolean isSent = FALSE;
    private public static int move = 0;
    private static ArrayList<Integer> movesX = new ArrayList<>();
    private static ArrayList<Integer> movesY = new ArrayList<>();

    private int numberOfPlayers;
    private double [][]colorsOfPlayers = new double[6][3];
    private int rows;

    private Circle myColor = new Circle(30, 30, 20);

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
                        circles[rows -j + rows * 2][rows - i -1].setFill(new Color(r, g, b, 1));
                        circles[j + rows][i + rows * 3 + 1].setStroke(new Color(r, g, b, 1));
                        circles[j + rows][i + rows * 3 + 1].setStrokeWidth(2.0);
                        break;
                    }
                    case 1: {
                        circles[j + rows * 3 + 1][i + rows].setFill(new Color(r, g, b, 1));
                        circles[rows - j - 1][rows - i + rows * 2].setStroke(new Color(r, g, b, 1));
                        circles[rows - j - 1][rows - i + rows * 2].setStrokeWidth(2.0);
                        break;
                    }
                    case 2: {
                        circles[rows - j + rows * 2][rows - i + rows * 2].setFill(new Color(r, g, b, 1));
                        circles[j + rows][i + rows].setStroke(new Color(r, g, b, 1));
                        circles[j + rows][i + rows].setStrokeWidth(2.0);
                        break;
                    }
                    case 3: {
                        circles[j + rows][i + rows * 3 + 1].setFill(new Color(r, g, b, 1));
                        circles[rows - j + rows * 2][rows - i - 1].setStroke(new Color(r, g, b, 1));
                        circles[rows - j + rows * 2][rows - i - 1].setStrokeWidth(2.0);
                        break;
                    }
                    case 4: {
                        circles[rows - j - 1][rows - i + rows * 2].setFill(new Color(r, g, b, 1));
                        circles[j + rows * 3 + 1][i + rows].setStroke(new Color(r, g, b, 1));
                        circles[j + rows * 3 + 1][i + rows].setStrokeWidth(2.0);
                        break;
                    }
                    case 5: {
                        circles[j + rows][i + rows].setFill(new Color(r, g, b, 1));
                        circles[rows - j + rows * 2][rows - i + rows * 2].setStroke(new Color(r, g, b, 1));
                        circles[rows - j + rows * 2][rows - i + rows * 2].setStrokeWidth(2.0);
                        break;
                    }
                }
            }
        }
    }

    //displays the map
    public void display(double cR, double cG, double cB){

        grid.getChildren().add(myColor);
        myColor.setFill(new Color(cR, cG, cB, 1));

        createStartingPositions(rows);
        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                if(array[i][j] != 0){

                    int dx = ((int)(Math.floor(size/2)) - j) * - (FIELDSIZE + SPACINGSIZE)/2;

                    circles[i][j] = new Circle((FIELDSIZE + SPACINGSIZE) * (i+1) + dx, (FIELDSIZE + SPACINGSIZE) * (j+1), FIELDSIZE/2, Color.WHITE);
                    int finalI = i;
                    int finalJ = j;
                    circles[i][j].setOnMousePressed(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println(finalI +" "+ finalJ);
                            if(!isSent) {
                                try {
                                    int a = movesX.get(move-1);
                                    int b = movesY.get(move-1);

                                    if (((finalI - a) == -1 && (finalJ - b) == 1) || ((finalI - a) == 1 && (finalJ - b) == -1) || ((finalI - a) == 0 && (finalJ - b) == 1) || ((finalI - a) == 0 && (finalJ - b) == -1) || ((finalI - a) == 1 && (finalJ - b) == 0) || ((finalI - a) == -1 && (finalJ - b) == 0)){
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if(((finalI - a) == -2 && (finalJ - b) == 2) || ((finalI - a) == 2 && (finalJ - b) == -2) || ((finalI - a) == 0 && (finalJ - b) == 2) || ((finalI - a) == 0 && (finalJ - b) == -2) || ((finalI - a) == 2 && (finalJ - b) == 0) || ((finalI - a) == -2 && (finalJ - b) == 0)){
                                        movesX.add(a + ((finalI - a)/2));
                                        movesY.add(b + ((finalJ - b)/2));
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if(((finalI - a) == -3 && (finalJ - b) == 3)){
                                        movesX.add(a-1);
                                        movesY.add(b+1);
                                        move++;
                                        movesX.add(a-2);
                                        movesY.add(b+2);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if((finalI - a) == 3 && (finalJ - b) == -3){
                                        movesX.add(a+1);
                                        movesY.add(b-1);
                                        move++;
                                        movesX.add(a+2);
                                        movesY.add(b-2);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if((finalI - a) == 0 && (finalJ - b) == 3){
                                        movesX.add(a);
                                        movesY.add(b+1);
                                        move++;
                                        movesX.add(a);
                                        movesY.add(b+2);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if((finalI - a) == 0 && (finalJ - b) == -3){
                                        movesX.add(a);
                                        movesY.add(b-1);
                                        move++;
                                        movesX.add(a);
                                        movesY.add(b-2);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if((finalI - a) == 3 && (finalJ - b) == 0){
                                        movesX.add(a+1);
                                        movesY.add(b);
                                        move++;
                                        movesX.add(a+2);
                                        movesY.add(b);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                    else if(((finalI - a) == -3 && (finalJ - b) == 0)){
                                        movesX.add(a-1);
                                        movesY.add(b);
                                        move++;
                                        movesX.add(a-2);
                                        movesY.add(b);
                                        move++;
                                        movesX.add(finalI);
                                        movesY.add(finalJ);
                                        move++;
                                    }
                                }catch(Exception e){
                                    movesX.add(finalI);
                                    movesY.add(finalJ);
                                    move++;
                                }
                            }
                        }
                    });
                    grid.getChildren().add(circles[i][j]);
                }
            }
        }
        //draw2Dmap(gc);
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
        Connection connection = cn;
        this.grid = gr;
        this.numberOfPlayers = nop;
        this.colorsOfPlayers = cop;

        createMapArray();

    }

    public static int getMove(){
        return move;
    }

    public static int getX(int k){
        return (int) movesX.get(k);
    }

    public static int getY(int k){
        return (int) movesY.get(k);
    }

    public static void setSent(boolean t){
        isSent = t;
    }

    public static void clearMoves(){
        for(int i=0; i<move; i++){
            movesX.remove(0);
            movesY.remove(0);
            isSent = TRUE;
        }
        move = 0;
    }

    public void makeMove(int xa, int ya, int xb, int yb){
        circles[xb][yb].setFill(circles[xa][ya].getFill());
        circles[xa][ya].setFill(Color. WHITE);
    }

    public void underlineColor(boolean s){
        if(s){
            myColor.setStroke(Color.BLACK);
            myColor.setStrokeWidth(5);
        }
        else{
            myColor.setStroke(myColor.getFill());
        }
    }
}