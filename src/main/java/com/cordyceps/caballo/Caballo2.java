package com.cordyceps.caballo;

/**
 *
 * @author zkropotkine
 */
import java.util.*;

/**
 * 
 * @author zkropotkine
 */
public class Caballo2 {
    private final static int base = 8;
    private static int[][] grid;
    private static int totalMoves;
    private final static int[][] moves = {{ 1,-2}, { 2, -1}, { 2, 1}, { 1,  2},
                                          {-1, 2}, {-2,  1}, {-2,-1}, {-1, -2}};
    
    private final static int [][] blackPieces = {{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}};
    private static Map<String, Boolean> blackPiecesContainer = new HashMap<>();
    
    private final static int [][] whitePieces = {{0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}, {6, 7}};
    private static Map<String, Boolean> whitePiecesContainer = new HashMap<>();

    public static void main(String[] args) {
        // Set the random position as the inital position.
        int initialMove = 1;
        
        grid = new int[base][base];
        int[][] initialGrid = new int[base][base];
        
        totalMoves = (base) * (base) - blackPieces.length;
        
        for (int[] whitePiece : blackPieces) {
            int x = whitePiece[0];
            int y = whitePiece[1];
            
            
            initialGrid[x][y] = -1;
            blackPiecesContainer.put(x + "," + y, Boolean.TRUE);
        }
        
        for (int[] whitePiece : whitePieces) {
            int x = whitePiece[0];
            int y = whitePiece[1];
            
            initialGrid[x][y] = 1;
            whitePiecesContainer.put(x + "," + y, Boolean.TRUE);
        }
        
        printResultGrid(initialGrid);
        
        // Obtain random positions for the column & row
        int row = 2 + (int) (Math.random() * (base - 4));
        int col = 2 + (int) (Math.random() * (base - 4));
        
        grid[row][col] = initialMove;

        if (solve(row, col, ++initialMove)) {
            printResult();
        }    
        else {
            System.out.println("Sorry, we didn't found a route");
        }
 
    }
 
    private static boolean solve(int row, int column, int currentMove) {
        //printResult();
        if (currentMove > totalMoves) {
            return true;
        }
        
        List<int[]> neighbors = getNeighbors(row, column);
 
        if (neighbors.isEmpty() && currentMove != totalMoves) {
            return false;
        }    
 
        Collections.sort(neighbors, new Comparator<int[]>() {
            final int neighborPosition = 2;
            public int compare(int[] firstNeighborInfo, int[] secondNeighborInfo) {
                return firstNeighborInfo[neighborPosition] - secondNeighborInfo[neighborPosition];
            }
        });
 
        for (int[] neighbor : neighbors) {
            row = neighbor[0];
            column = neighbor[1];
            boolean found = false;
            String key = row + "," + column;
            
            
            if (blackPiecesContainer.containsKey(key)) {
                grid[row][column] = -1;
                //currentMove;
                continue;
            } else {
                
               if (whitePiecesContainer.containsKey(key)) {
                  whitePiecesContainer.remove(key);
                  
                  System.out.println("A white piece is found on " + key);
                  
                  if (whitePiecesContainer.isEmpty()) {
                      System.out.println("We found all the white pieces :)");
                      return true;
                  }
               }
                
               grid[row][column] = currentMove;
            }
            
            if (!orphanDetected(currentMove, row, column) && solve(row, column, currentMove + 1)) {
                return true;
            }    
            grid[row][column] = 0;
        }
 
        return false;
    }
    
    /**
     * 
     * @param row
     * @param column
     * @return 
     */
    private static List<int[]> getNeighbors(int row, int column) {
        List<int[]> neighbors = new ArrayList<>();
        final int y = 1;
        final int x = 0;
 
        for (int[] move : moves) {
            int newRow = row + move[y];
            int newColumn = column + move[x];
            
            if (checkValidPosition(newRow, newColumn) && grid[newRow][newColumn] == 0) {
                int num = countNeighbors(newRow, newColumn);
                neighbors.add(new int[]{newRow, newColumn, num});
            }
        }
        return neighbors;
    }
 
    /**
     * 
     * @param row
     * @param column
     * @return 
     */
    private static int countNeighbors(int row, int column) {
        int neighbors = 0;
        final int y = 1;
        final int x = 0;
        
        for (int[] move : moves) {
            int newRow = row + move[y];
            int newColumn = column + move[x];
            
            if (checkValidPosition(newRow, newColumn) && grid[newRow][newColumn] == 0) {
                neighbors++;
            }
        }
        
        return neighbors;
    }
    
    private static boolean checkValidPosition(int row, int column) {
        boolean result = false;
        
        if ((row >= 0 && row < base) && (column >= 0 && column < base)) {
            result = true;
        }
        
        return result;
    }
 
    private static boolean orphanDetected(int currentMove, int row, int column) {
        if (currentMove < totalMoves - 1) {
            for (int[] neighbor : getNeighbors(row, column)) {
                if (countNeighbors(neighbor[0], neighbor[1]) == 0) {
                    return true;
                }    
            }    
        }
        return false;
    }
 
    private static void printResult() {
        printResultGrid(grid);
    }
    
     private static void printResultGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int i : row) {
                System.out.printf("%2d ", i);
            }
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}
