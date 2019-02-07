import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class GameOfLife  extends Application
{
    final int cellwidth = 10;
    final int cellheight = 10; 
    final int boardsize = 500;
    Map <String, StackPane> gameboardMap = new HashMap<>(); 
    Pane root = new Pane();
    Scene scene = new Scene(root, boardsize, boardsize);
    Timeline gametimeline;
    int[][] gameboard = new int [boardsize/10][boardsize/10];
    public static void main(String[] args) 
    { 
        launch(args);
    }
    
    public void start(Stage primaryStage)
    {
        scene.getStylesheets().addAll("gameStyles.css");
        changePopulation();
        addCell();
        gametimeline = new Timeline(new KeyFrame(Duration.ZERO, (EventHandler) event -> iterateBoard()), new KeyFrame(Duration.millis(050)));
        gametimeline.setCycleCount(Timeline.INDEFINITE);
        primaryStage.setScene(scene);
        primaryStage.show();
        gametimeline.play();
    }
    
    public void addCell()
    {
        for (int x = 0; x < boardsize; x = x + cellwidth) 
        {
            for (int y = 0; y < boardsize; y = y + cellwidth)
            {
                StackPane cell = StackPaneBuilder.create().layoutX(x).layoutY(y).prefHeight(cellheight).prefWidth(cellwidth).styleClass("dead").build();
                root.getChildren().add(cell);
                gameboardMap.put(x + " " + y, cell);
            }
        }
    }

    public void iterateBoard() 
    {
        createNextCellCreation();
        for (int rowPosition = 0; rowPosition < BoardSize(); rowPosition++)
        {
            for (int columnPosition = 0; columnPosition < BoardSize(); columnPosition++) 
            {
                StackPane GamePane = gameboardMap.get(rowPosition * cellwidth + " " + columnPosition * cellheight);
                GamePane.getStyleClass().clear();
                if (FieldPosition(rowPosition, columnPosition) == 1)
                {
                    GamePane.getStyleClass().add("alive");
                }
                else 
                {
                    GamePane.getStyleClass().add("dead");
                }
            }
        }
    }
    
    int FieldPosition(int row, int column) 
    {
        return gameboard[row][column];
    }
    
    int BoardSize() 
    {
        return gameboard.length;
    }
    
    void changePopulation() //this method randomly gerates a cell 
    {
        Random random = new Random();
        double populationAmount = 0.8;
        for (int rowPosition = 0; rowPosition < gameboard.length; rowPosition++) 
        {
            for (int columnPosition = 0; columnPosition < gameboard.length; columnPosition++)
            {
                if (random.nextDouble() > populationAmount)
                {
                    gameboard[rowPosition][columnPosition] = 1;
                }
            }
        }
    }
    
    void createNextCellCreation() 
    {
        int[][] newGameBoard = new int[gameboard.length][gameboard.length];
        for (int rowPosition = 0; rowPosition < gameboard.length; rowPosition++) 
        {
            for (int columnPosition = 0; columnPosition< gameboard.length; columnPosition++) 
            {
                newGameBoard[rowPosition][columnPosition] = FieldPosition(rowPosition, columnPosition);
                checkCells(rowPosition, columnPosition, newGameBoard);
            }
        }
        gameboard = newGameBoard;
    }
    
    private void checkCells(int row, int column, int[][] newBoard) 
    {
        int[] rowNeighbours = {-1, 0, 1,  -1, 1, -1, 0, 1};
        int[] columnNeighbours = {1, 1, 1, 0, 0, -1, -1, -1};
        int cellvalue = gameboard[row][column];
        int neighbours = 0;
        for (int i = 0; i < 8; i++)
        {
            if(row + rowNeighbours[i] >=0 && column + columnNeighbours[i] >=0 && row + rowNeighbours[i] < gameboard.length && column + columnNeighbours [i] < gameboard.length)
            {
                neighbours = neighbours + FieldPosition(row + rowNeighbours [i], column + columnNeighbours [i]);
            }
        }

        if (cellvalue == 0 && neighbours == 3) 
        {
            newBoard[row][column] =1;
        }
        else if (cellvalue == 1 && neighbours < 2) 
        {
            newBoard[row][column] = 0;
        }
        else if (cellvalue == 1 && neighbours == 2 || neighbours == 3)
        {

        }
        else if (cellvalue == 1) 
        {
            newBoard[row][column] = 0;
        }
    }
}