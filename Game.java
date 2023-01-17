import java.io.BufferedReader;
import java.io.IOException;

public class Game {
    private static final Integer COLUMNS = 7;
    private static final Integer ROWS = 6;
	static char player = 'R';
	static char opponent = 'O';
    BufferedReader reader;
    BufferedReader client_reader;
    String version;
    ErrorHandler errorHandler;
    PackageSender packageSender;

    public Game(BufferedReader reader, BufferedReader client_reader, 
                String version, ErrorHandler errorHandler, PackageSender packageSender) {
        this.reader = reader;
        this.client_reader = client_reader;
        this.version = version;
        this.errorHandler = errorHandler;
        this.packageSender = packageSender;
    }

	public static void main(String[] args) {
		char[][] grid = new char[ROWS][COLUMNS];

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++){
                grid[row][col] = ' ';
            }
        }

		display(grid);
	}

    public int startGame(boolean hosting) {

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
                String input;
                try {
                    if (myTurn) {
                        System.out.println("Choose your column!");
                        input = client_reader.readLine();
                    } else {
                        System.out.println("Waiting for opponent to choose column!");
                        input = reader.readLine();
                    }
                    Package currentPackage = new Package(input);
                    if (currentPackage.code.equals("1")) {
                        column = Integer.parseInt(currentPackage.content);
                    } else if (currentPackage.code.equals("2")) {
                        errorHandler.handle(currentPackage.content);
                    }
                    
                } catch (NumberFormatException e) {
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                validPlay = validate(column, grid);

                if (!myTurn) {
                    if (!validPlay) {
                        packageSender.error("2");
                    }
                }

            } while (!validPlay);
            
            if (myTurn) {
                packageSender.move(column);
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
		
	private static void display(char[][] field) {
        System.out.println("\033[1;36m -------- CONNECT FOUR --------\033[0m");
        System.out.println("\033[1;36m ┌───┬───┬───┬───┬───┬───┬───┐ \033[0m");
        for (int i = 0; i < field.length; i++) {
            System.out.print("\033[1;36m │");
            for (int j = 0; j < field[0].length; j++) {
                if(field[i][j] == player) {
                    System.out.print(" \033[0;94mO \033[1;36m│");
                } else if(field[i][j] == opponent) {
                    System.out.print(" \033[0;91mX \033[1;36m│");
                } else {
                    System.out.print("   │");
                }
            }
			System.out.println();
            if(i!=5)
                System.out.println("\033[1;36m ├───┼───┼───┼───┼───┼───┼───┤ \033[0m");
        }
        System.out.println("\033[1;36m └───┴───┴───┴───┴───┴───┴───┘ \033[0m");
        System.out.println("\033[1;96m   0   1   2   3   4   5   6 \033[0m");
    }

    public static boolean validate(int column, char[][] grid){
		//valid column?
		if (column < 0 || column >= grid[0].length){
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
