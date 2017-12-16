package Client.Map;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Map {
    //number of pawns per Player
    private int pawnNumber;
    //array with the data. Possibly should be converted to array of objects
    //we need to store starting positions and ending positions
    private int array[][];
    //the size of the array
    private int size = 0;

    //radius of the circle
    private final int FIELDSIZE = 35;
    //the size of space between fields
    private final int SPACINGSIZE = 5;

    //creates map array
    private void createMapArray(){
        //validate the number of pawns belongs to the sequence
        //1, 3, 6, 10, 15, 21 etc
        if(!checkPawnNumber()){
            updatePawnNumber();
        }
        //calculate the number of rows needed to store the pawns
        int rows = calculateRowsNumber();
        //prints number of rows, and pawnNumber
        System.out.println(rows + " " + pawnNumber);

        //update the size of array
            size = rows*4 + 1;

        //create array
        array = new int[size][];
        for(int i =0; i < size; i++){
            array[i] = new int[size];
        }

        //fill square part of map with 1's
        for(int i = rows; i < size - rows; i++){
            for(int j = rows; j < size - rows; j++){
                array[i][j] = 1;
            }
        }
        //creates the blue starting positions
        createStartingPositions(rows);
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
    //displays the map
    public void display(GraphicsContext gc){


        gc.setFill(Color.GREEN);
        gc.setLineWidth(5);

        for(int i = 0; i < size; i++){
            for(int j =0; j < size; j++){
                if(array[i][j] != 0){
                    if(array[i][j] == 2){
                        gc.setFill(Color.BLUE);
                    }

                    int dx = ((int)(Math.floor(size/2)) - j) * - (FIELDSIZE + SPACINGSIZE)/2;

                    gc.fillOval( (FIELDSIZE + SPACINGSIZE) * (i+1) + dx, (FIELDSIZE + SPACINGSIZE) * (j+1), FIELDSIZE, FIELDSIZE);

                    if(array[i][j] == 2){
                        gc.setFill(Color.GREEN);
                    }
                }
            }
        }
        draw2Dmap(gc);
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

    public Map(int pawnNumber){
        this.pawnNumber = pawnNumber;
        createMapArray();
    }


}
