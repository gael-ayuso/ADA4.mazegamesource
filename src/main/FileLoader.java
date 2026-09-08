package src.main;

import src.main.gui.elements.MapElements;

import java.io.*;
import javax.swing.*;

public class FileLoader {
    private int exitXCord = 0;
    private int exitYCord = 0;
    private char [][] GameMatrix;
    private int column;
    private int row;

    public boolean loadFile(String fileName) {
        try (BufferedReader in = new BufferedReader(new FileReader(fileName))) {
            String x;
            int lineNum = 0;
            while ((x = in.readLine()) != null) {
                MatrixLoader(x, lineNum);//pass the Matrix loader method the line and the line number for parsing.
                lineNum++;//we will use the line number later in this class
            }
            return true;
        }//end try
        catch (IOException e) {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Ooops IOException error, i did it again!" + e.getMessage());
            return false;
        }//end catch
    }//end load file method

    public void MatrixLoader(String fileTextLine, int lineNum) throws gameFileError {
        // exitCount=0;//we must reset our variables to zero for the next level.

        int sum = 0;
        char textVar;
        if (lineNum == 0)//it is the first line of the maze file, create The Matrix based on first line of the maze file
        {
            for (int i = 0; i < fileTextLine.length(); i++) {
                if (fileTextLine.charAt(i) == ' ')//find blank area on first line number
                    sum += 1;//how many blank spaces between the size of the matrix aka 4 6 or 5  7
            }
            int locationOfSpace = fileTextLine.indexOf(" ");//still handling that possible blank space in the matrix size in the file
            String c1 = fileTextLine.substring(0, locationOfSpace);//see above
            String r1 = fileTextLine.substring(locationOfSpace + sum);//see above
            column = Integer.parseInt(c1);
            row = Integer.parseInt(r1);
            GameMatrix = new char[row][column];//create new matrix based on the size from the file
        }//end if
        else
            for (int i = 0; i < fileTextLine.length(); i++)//it is not the first line of the maze file
            {
                textVar = fileTextLine.charAt(i); //grab the individual charaters from the string.
                if (textVar == '.')//change . to N, so we dont have any goofy file system problems
                    textVar = MapElements.EMPTY.getSymbol();
                if (textVar == MapElements.EXIT.getSymbol())//log the position of the exit for later use
                {

                    exitXCord = lineNum - 1;
                    exitYCord = i;
                    // textVar='W';
//                    textVar1 = "" + textVar;//turn the exit into a wall
                    textVar = MapElements.WALL.getSymbol();
                }
                GameMatrix[lineNum - 1][i] = textVar; //load the matrix with values, aka N,W, D, H, etc
            }//end for loop


    }//end matrixloader method

    public char[][] getGameMatrix() {
        if (GameMatrix == null) {
            return null;
        }
        int exitCount = 0;
        int i1 = 0;
        int j1 = 0;
        int playerCount = 0;
        for (int i = 0; i < GameMatrix.length; i++) {
            for (int j = 0; j < GameMatrix[i].length; j++) {
                if (GameMatrix[i][j] == MapElements.PLAYER.getSymbol()) {
                    playerCount += 1;
                } else if (GameMatrix[i][j] == MapElements.EXIT.getSymbol()) {
                    exitCount += 1;
                    i1 = i;
                    j1 = j;
                }
            }
        }//end double for loop
        if (playerCount > 1 || exitCount > 1) {
            throw new gameFileError();
        } else if (exitCount == 1) {
            GameMatrix[i1][j1] = MapElements.WALL.getSymbol();
        }

        return GameMatrix;
    }//end getGameMatrix method

    public int getMatrixSizeColumn()//return the matrixsize-column
    {
        return column;
    }

    public int getMatrixSizeRow()//return the matrix size-row
    {
        return row;

    }

    public int ExitXCord() //return the X cordinates for the Exit
    {
        return exitXCord;
    }

    public int ExitYCord()//return the Y cordinates for the Exit
    {
        return exitYCord;
    }

    public int dimondCount() {
        int totalDimonds = 0;
        for (int i = 0; i < GameMatrix.length; i++) {
            for (int j = 0; j < GameMatrix[i].length; j++) {
                if (GameMatrix[i][j] == MapElements.DIAMOND.getSymbol() || GameMatrix[i][j] == MapElements.HIDDEN_DIAMOND.getSymbol())
                    totalDimonds += 1;
            }
        }//end double for loop
        return totalDimonds;//return the total number of dimonds in the level
    }

    private class gameFileError extends RuntimeException //if a level is loaded with ether two players or two exits throw this
    {
        public gameFileError() {
            JFrame frame = new JFrame("Alert");
            JOptionPane.showMessageDialog(frame, "Your maze file ether had more than one player, or more than one exit.");
        }
    }//end inner class

}//end class