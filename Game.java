import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Game {
    private static final Integer COLUMNS = 7;
    private static final Integer ROWS = 6;
    BufferedWriter writer;
    BufferedReader reader;
    BufferedReader client_reader;
    String version;

    public Game(BufferedWriter writer, BufferedReader reader, BufferedReader client_reader, String version) {
        this.writer = writer;
        this.reader = reader;
        this.client_reader = client_reader;
        this.version = version;
    }

    public int startGame(boolean hosting) {

        PackageSender packageSender = new PackageSender();

        char player = 'R';
        char opponent = 'O';

        char[][] grid = new char[ROWS][COLUMNS];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++){
                grid[row][col] = ' ';
            }
        }

        int turn = 1;
        boolean winner = false;
        boolean myTurn = hosting;
        char currPlayer = ' ';

        while (!winner && turn <= 42) {
            if (myTurn) {
                currPlayer = player;
            } else {
                currPlayer = opponent;
            }
            boolean validPlay = false;
            Integer column = null;
            do {
                display(grid);
                
                try {
                    if (myTurn) {
                        System.out.println("Choose your column!");
                        column = Integer.parseInt(client_reader.readLine());
                    } else {
                        System.out.println("Waiting for opponent to choose column!");
                        column = Integer.parseInt(reader.readLine().substring(2));
                    }
                } catch (NumberFormatException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                validPlay = validate(column, grid);

                if (!myTurn) {
                    if (!validPlay) {
                        packageSender.error(writer, "2|Can't select column " + column + "! Please try again.");
                    }
                }

            } while (!validPlay);
            
            if (myTurn) {
                packageSender.move(writer, column);
            }

            for (int row = grid.length-1; row >= 0; row--) {
				if(grid[row][column] == ' '){
					grid[row][column] = currPlayer;
					break;
				}
			}

            winner = isWinner(currPlayer, grid);

            if (myTurn) {
                myTurn = false;
            } else {
                myTurn = true;
            }

            turn++;
        }

        display(grid);
        
        if (winner){
			if (currPlayer == player){
				System.out.println("You won!");
			}else{
				System.out.println("You lost :( ...");
			}
		}else{
			System.out.println("Tie game");
		}
        return 1;

    }

    private static void display(char[][] grid){
		System.out.println(" 0 1 2 3 4 5 6");
		System.out.println("---------------");
		for (int row = 0; row < grid.length; row++){
			System.out.print("|");
			for (int col = 0; col < grid[0].length; col++){
				System.out.print(grid[row][col]);
				System.out.print("|");
			}
			System.out.println();
			System.out.println("---------------");
		}
		System.out.println(" 0 1 2 3 4 5 6");
		System.out.println();
	}

    public static boolean validate(int column, char[][] grid){
		//valid column?
		if (column < 0 || column > grid[0].length){
			return false;
		}
		
		//full column?
		if (grid[0][column] != ' '){
			return false;
		}
		
		return true;
	}

    public static boolean isWinner(char player, char[][] grid){
		//check for 4 across
		for(int row = 0; row<grid.length; row++){
			for (int col = 0;col < grid[0].length - 3;col++){
				if (grid[row][col] == player   && 
					grid[row][col+1] == player &&
					grid[row][col+2] == player &&
					grid[row][col+3] == player){
					return true;
				}
			}			
		}
		//check for 4 up and down
		for(int row = 0; row < grid.length - 3; row++){
			for(int col = 0; col < grid[0].length; col++){
				if (grid[row][col] == player   && 
					grid[row+1][col] == player &&
					grid[row+2][col] == player &&
					grid[row+3][col] == player){
					return true;
				}
			}
		}
		//check upward diagonal
		for(int row = 3; row < grid.length; row++){
			for(int col = 0; col < grid[0].length - 3; col++){
				if (grid[row][col] == player   && 
					grid[row-1][col+1] == player &&
					grid[row-2][col+2] == player &&
					grid[row-3][col+3] == player){
					return true;
				}
			}
		}
		//check downward diagonal
		for(int row = 0; row < grid.length - 3; row++){
			for(int col = 0; col < grid[0].length - 3; col++){
				if (grid[row][col] == player   && 
					grid[row+1][col+1] == player &&
					grid[row+2][col+2] == player &&
					grid[row+3][col+3] == player){
					return true;
				}
			}
		}
		return false;
	}

}
