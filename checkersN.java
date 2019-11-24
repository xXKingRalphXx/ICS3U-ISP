/**
 * Student: Damilola Babalola
 * Course: ICS3U1
 * Date: 01/22/2019
 * Checkers with a square board
 * of user determined length.
**/

import java.util.*;
import java.lang.*;

public class checkersN{
	// global scanner (wrapper functions here are a bad idea)
	static Scanner s = new Scanner(System.in); 

	static int N; // horizontal/N length
	static int[][] board; // 2-d array for the board pieces
		// 0 - empty space
		// 1 - player 1 regular piece
		// 2 - player 2 regular piece
		// 3 - player 1 king piece 
		// 4 - player 1 king piece 
	static boolean debug = true;

	// player one and two piece count
	static int pp1, pp2;

	// is player one the current player?
	static boolean isPlayerOne = true;

	// is it gameover?
	static boolean GAMEOVER = false;

	// state variables to track key game events
	static boolean firstCaptureWasExecuted = false;
	static boolean firstKingWasSpawned = false;

   /*
    * @parameters: n/a
    * @description: Does the following
	* 1. displays the intro message
	* 2. loads the game
	* 3. starts the game loop
	* 4. prints "GAMEOVER" when the game is over
	* 5. closes the global scanner and exits
    * @returns: n/a
    */
	public static void main(String[] args){
		Random r = new Random();
		intro_message();
		load_game();
		game_loop();

		pretty_print("GAMEOVER");

		s.close();
	}

   /*
	* @parameters: n/a
	* @description: dumps all variables to 
	* the console, then exits immediately.
	* @returns: n/a
	*/
	public static void error_exit(){ // REMOVE FROM FINAL SUBMISSION
		pretty_print("~ ERROR EXIT ~");

		System.out.println("isPlayerOne = "+isPlayerOne);
		System.out.println("debug = "+debug);
		System.out.println("N = "+N);
		System.out.println("pp1 = "+pp1);
		System.out.println("pp2 = "+pp2);
		System.out.println("GAMEOVER = "+GAMEOVER);
		System.out.println();

		for(int i=0; i<N; i++){
			for(int k=0; k<N; k++){
				System.out.print(board[i][k]+" ");
			}
			System.out.println();
		}
		System.out.println();

		System.exit(0);
	}

   /*
	* @parameters: one String
	* @description: Prints its string
	* parameter with pretty formatting.
	* @returns: n/a
	*/
	public static void pretty_print(String str){
		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println(str);
		System.out.println("--------------------------------------------------");
		System.out.println();
	}

   /*
    * @parameters: n/a
    * @description: Clears the console
	* screen by printing 100 newlines.
    * @returns: n/a
    */
	public static void clear(){
		for(int c=0; c<100; c++){
			System.out.println();
		}
	}

   /*
    * @parameters: one String
    * @description: Prints its string
	* parameter as a prompt, then
	* retrieves a string from the user.
	* Also serves the purpose of
	* sanitizing input.
    * @returns: the string entered by the user.
    */
	public static String get_str(String prompt){
		System.out.print(prompt);
		String input = "";

		while(!s.hasNextLine()){
			s.nextLine(); // consume invalid chars and avoid the nextLine bug
			System.out.println("ERROR: YOU DID NOT ENTER A STRING.");
			System.out.print("TRY AGAIN: ");
		}

		input = s.nextLine();
		return input;
	}

   /*
    * @parameters: one String
    * @description: Prints its string
	* parameter as a prompt, then
	* retrieves an integer from the user.
	* Also serves the purpose of
	* sanitizing input.
    * @returns: the integer entered by the user
    */
	public static int get_int(String prompt){
		System.out.print(prompt);
		int input = 0;

		while(!s.hasNextInt()){
			System.out.println("ERROR: YOU DID NOT ENTER AN INTEGER.");
			System.out.print("TRY AGAIN: ");

			s.nextLine(); // consume invalid chars and avoid the nextLine bug
		}

		while(input < 0){
			System.out.println("ERROR: YOU MUST ENTER AN INTEGER GREATER THAN 0");
			System.out.print("TRY AGAIN: ");

			s.nextLine(); // consume invalid chars and avoid the nextLine bug
			input = s.nextInt();
		}

		input = s.nextInt();
		return input;
	}

   /*
    * @parameters: n/a
    * @description: Prints a large
	* ascii text banner that says,
	* "Welcome to Checkers".
    * @returns: n/a
    */
	public static void intro_message(){
		System.out.println();
		System.out.println();
		System.out.println(" _       __       __                                 __            ______ __                 __                    ");
		System.out.println("| |     / /___   / /_____ ____   ____ ___   ___     / /_ ____     / ____// /_   ___   _____ / /__ ___   _____ _____");
		System.out.println("| | /| / // _ \\ / // ___// __ \\ / __ `__ \\ / _ \\   / __// __ \\   / /    / __ \\ / _ \\ / ___// //_// _ \\ / ___// ___/");
		System.out.println("| |/ |/ //  __// // /__ / /_/ // / / / / //  __/  / /_ / /_/ /  / /___ / / / //  __// /__ / ,<  /  __// /   (__  ) ");
		System.out.println("|__/|__/ \\___//_/ \\___/ \\____//_/ /_/ /_/ \\___/   \\__/ \\____/   \\____//_/ /_/ \\___/ \\___//_/|_| \\___//_/   /____/  ");
		System.out.println();
		System.out.println();
 	}

   /*
    * @parameters: n/a
    * @description: Prints the rules 
	* of the game to the console.
    * @returns: n/a
    */
	public static void load_game(){
		pretty_print("THE RULES");

		System.out.println("Player 1's Pieces");
		System.out.println("    'r1' represents player one's regular pieces");
		System.out.println("    'k1' represents player one's king pieces");
		System.out.println("Player 2's Pieces");
		System.out.println("    'r2' represents player two's regular pieces");
		System.out.println("    'k2' represents player two's king pieces");
		System.out.println();

		System.out.println("Both regular and king pieces can move only one space at a time");
		System.out.println("Both regular and king pieces can move two spaces if executing a capture move");
		System.out.println("    That is, 'checking' another piece");
		System.out.println();
		System.out.println("Regulars CANNOT MOVE BACKWARDS — that ability is reserved for kings ALONE");
		System.out.println();

		System.out.println("An external link is provided below for more information");
		System.out.println("    https://en.wikipedia.org/wiki/Draughts");
		System.out.println();

		game_menu();
		if(!GAMEOVER){
			init_board();
		}
	}

   /*
    * @parameters: one String
    * @description: Prints its string
    * parameter with pretty formatting.
    * @returns: n/a
    */
	public static void init_board(){
		N = get_int("Enter the side length of the square board: ");

		while(N < 4){
			System.out.println("ERROR: BOARD LENGTH OR HEIGHT MUST BE GREATER THAN OR EQUAL TO 4");
			N = get_int("Enter the side length of the square board: ");
		}

		// initialize board with user determined dimensions
		board = new int[N][N];

		// even number side length
		if(N % 2 == 0){
			// top half of the board
			for(int i=0; i<N/2-1; i++){
				// alternate piece placement for odd and even rows
				if(i % 2 == 0){
					for(int k=1; k<N; k+=2){
						board[i][k] = 1;
					}
				} else{
					for(int k=0; k<N; k+=2){
						board[i][k] = 1;
					}
				}
			}

			// leave a gap of 2 spaces between player one and two

			// bottom half of the board
			for(int i=N-1; i>=N/2+1; i--){
				if(i % 2 == 1){
					for(int k=0; k<N; k+=2){
						board[i][k] = 2;
					}
				} else{
					for(int k=1; k<N; k+=2){
						board[i][k] = 2;
					}
				}
			}
		// odd number side length
		} else{
			// top half of the board
			for(int i=0; i<N/2-1; i++){
				// alternate piece placement for odd and even rows
				if(i % 2 == 1){
					for(int k=1; k<N; k+=2){
						board[i][k] = 1;
					}
				} else{
					for(int k=0; k<N; k+=2){
						board[i][k] = 1;
					}
				}
			}

			// leave a gap of 3 spaces between player one and two

			// bottom half of the board
			for(int i=N-1; i>N/2+1; i--){
				if(i % 2 == 0){
					for(int k=0; k<N; k+=2){
						board[i][k] = 2;
					}
				} else{
					for(int k=1; k<N; k+=2){
						board[i][k] = 2;
					}
				}
			}
		}
		// the rest of the board will be zero-initialized
	}

	/*
	 * @parameters: n/a
	 * @description: Manually updates the values
	 * of the integers, pp1, & pp2.
	 * @returns: n/a
	 */
	public static void update_piece_count(){
		// reset the count to 0
		pp1 = 0;
		pp2 = 0;

		for(int i=0; i<N; i++){
			for(int k=0; k<N; k++){
				if(board[i][k] == 1){ // player 1 piece
					pp1++;
				} else if(board[i][k] == 2){ // player 2 piece
					pp2++;
				} else if(board[i][k] == 3){ // player 1 king piece
					pp1++;
				} else if(board[i][k] == 4){ // player 2 king piece
					pp2++;
				}
			}
		}
	}

	/*
	 * @parameters: n/a
	 * @description: Draws the game board.
	 * @returns: n/a
	 */
	public static void draw_board(){
		System.out.println();
		System.out.println();
		System.out.print("    |");
		for(int m=1; m<N+1; m++){
			// printing double-digit numbers
			// breaks the board display
			if(m<10){
				System.out.print("col"+m+"|");
			} else if(m == 10){
				System.out.print("... |");
			} else{
				System.out.print("    |");
			}
		}

		System.out.println();

		// 1st horizontal dividing line
		System.out.print("----|+---");
		for(int m=0; m<N-2; m++){
			System.out.print("+----");
		}
		System.out.println("+---+|");

		for(int i=0; i<N; i++){
			// printing double-digit numbers
			// breaks the board display
			if(i<9){
				System.out.print("row"+(i+1));
			} else if(i == 9){
				System.out.print("... ");
			} else{
				System.out.print("    ");
			}

			for(int k=0; k<N; k++){
				System.out.print("|");
				if(board[i][k] == 0){ // empty space
					System.out.print("    ");
				} else if(board[i][k] == 1){ // player 1 piece
					System.out.print(" r1 "); 
				} else if(board[i][k] == 2){ // player 2 piece
					System.out.print(" r2 ");
				} else if(board[i][k] == 3){ // player 1 king piece
					System.out.print(" k1 "); 
				} else if(board[i][k] == 4){ // player 2 king piece
					System.out.print(" k2 "); 
				}
			}
			System.out.println("|");

			// horizontal dividing line
			System.out.print("----|+---");
			for(int m=0; m<N-2; m++){
				System.out.print("+----");
			}
			System.out.println("+---+|");
		}
		update_piece_count();

		if(debug){
			System.out.println("pp1 = "+pp1);
			System.out.println("pp2 = "+pp2);
		}

		System.out.println();
	}

    /*
     * @parameters: n/a
     * @description: Displays the game
	 * menu to the user.
     * @returns: n/a
     */
	public static void game_menu(){
		String opt = "";
		pretty_print("GAME MENU");

		System.out.println("Enter 'continue' to continue (i.e. exit this game menu)");
		System.out.println("Enter 'exit' to end the game");
		System.out.println();

		opt = get_str("Enter your option: ");
		if(opt.equals("continue")){
			// fall through
		} else if(opt.equals("exit")){
			GAMEOVER = true;
		} else{
			// enter a recursive loop until the user enters something valid
			clear();
			game_menu();
		}
	}

    /*
     * @parameters: n/a
     * @description: Starts the game loop.
	 * Exits when the game is over.
     * @returns: n/a
     */
	public static void game_loop(){
		while(!GAMEOVER){ // the loop ends when it's gameover
			draw_board();

			// start the chain of move related functions
			begin_mk_mv();

			// switch to other player
			// prior function calls rely on this variable
			// it cannot be modified until afterwards
			isPlayerOne = isPlayerOne ? false : true; 
		}
	}

    /*
     * @parameters: n/a
     * @description: Accepts input from
	 * the user to move game pieces on
	 * the checkers board. Provides user
	 * with option to access game menu
	 * after each move. Exits when the 
	 * end game conditions are met, or 
	 * the user exits.
     * @returns: n/a
     */
	public static void begin_mk_mv(){
		int lx0 = 0;
		int ly0 = 0;
		int lx1 = 0;
		int ly1 = 0;

		System.out.println();
		if(isPlayerOne){
			System.out.print("~ Player one, make your move ~");
		} else{
			System.out.print("~ Player two, make your move ~");
		}
		System.out.println();

		// loop until we get a valid move
		do{
			lx0 = get_int("enter the row number to move from: ") - 1;
			ly0 = get_int("enter the column number to move from: ") - 1;
			lx1 = get_int("enter the row number to move to: ") - 1;
			ly1 = get_int("enter the column number to move to: ") - 1;
		}while(invalid_mv(lx0, ly0, lx1, ly1));

		// the move is not invalid—make it
		make_mv(lx0, ly0, lx1, ly1);
		update_piece_count();

		String opt = "";
		s.nextLine(); // rectify nextLine() bug

		// any user can access game menu after making a move
		while(!(opt.equals("y") || opt.equals("n"))){
			opt = get_str("Access Game Menu? (y/n): ");
		}

		if(opt.equals("y")){
			game_menu();
		}

		// check for player win
		check_for_win();
	}

    /*
     * @parameters: four integers
     * @description: Checks to see
	 * if the move being attempted by
	 * the player is valid or not.
     * @returns: a boolean signifying
	 * whether or not the attempted 
	 * move is a valid or not.
     */
	public static boolean invalid_mv(int x0, int y0, int x1, int y1){
		boolean ret = false; // assume good intentions at first

		if(x0 > N || y0 > N || x1 > N || y1 > N ){
			// we differentiate between passing through walls and
			// attempting a move that takes a piece off the board
			pretty_print("ERROR: MOVE CANNOT EXCEED THE SIZE OF THE BOARD");
			ret = true;
		} else if(x0 > N || y0 > N || x1 > N || y1 > N || x0 < 0 || y0 < 0 || x1 < 0 || y1 < 0){
			pretty_print("CANNOT PASS THROUGH WALLS");
			ret = true;
		} else if(board[x0][y0] == 0){
			pretty_print("ERROR: NO BOARD PIECE WAS FOUND AT THAT LOCATION");
			ret = true;
		} else if(isPlayerOne && (board[x0][y0] == 2 || board[x0][y0] == 4)){
			pretty_print("ERROR: PLAYER ONE (that means you!) CANNOT MOVE PLAYER TWO's PIECES");
			ret = true;
		} else if(!isPlayerOne && (board[x0][y0] == 1 || board[x0][y0] == 3)){
			pretty_print("ERROR: PLAYER TWO (that means you!) CANNOT MOVE PLAYER ONE's PIECES");
			ret = true;
		} else if(board[x0][y0] != 3 && isPlayerOne && x1 < x0){
			pretty_print("ERROR: CANNOT MOVE BACKWARDS");
			ret = true;
		} else if(board[x0][y0] != 4 && !isPlayerOne && x1 > x0){
			pretty_print("ERROR: CANNOT MOVE BACKWARDS");
			ret = true;
		} else if(!isValidDiagonalMove(x0, y0, x1, y1)){
			pretty_print("ERROR: MOVE WAS INVALID or NOT DIAGONAL");
			ret = true;
		} else if(board[x1][y1] != 0){ // can't move onto occupied pos
			pretty_print("ERROR: CANNOT MOVE BOARD PIECE ONTO OCCUPIED POSITION");
			ret = true;
		} else if(debug){ // catch all cases while debugging
			pretty_print("FELL OFF THE SCOPE OF THE EARTH");
			// error_exit();
		}
		return ret;
	}

    /*
     * @parameters: four integers
     * @description: Checks to see
	 * whether or not the diagonal
	 * move attempted by the user 
	 * is valid or not.
     * @returns: a boolean signifying
	 * whether or not the attempted
	 * move is valid or not.
     */
	public static boolean isValidDiagonalMove(int x0, int y0, int x1, int y1){
		boolean ret = false; // false by default

		int dx = Math.abs(x0-x1); // horizontal displacement
		int dy = Math.abs(y0-y1); // vertical displacement
		// stored to avoid double calls
		boolean will_chk_piece_b = will_chk_piece(x0, y0, x1, y1, dx, dy); 

		if(dx == 1 && dy == 1){
			ret = true;
		} else if(dx == 2 && dy == 2 && will_chk_piece_b){
			ret = true;
		}

		if(debug){
			System.out.println("x0 = "+x0);
			System.out.println("y0 = "+y0);
			System.out.println("x1 = "+x1);
			System.out.println("y1 = "+y1);
			System.out.println("dx = "+dx);
			System.out.println("dy = "+dy);
			System.out.println("isValidDiagonalMove = "+ret);
			System.out.println("will_chk_piece = "+will_chk_piece_b);
		}
		return ret;
	}

    /*
     * @parameters: six integers
     * @description: Checks to see
	 * whether or not the move being
	 * attempted by the player will
	 * capture the opposing player's
	 * board piece.
     * @returns: a boolean signifying
	 * whether or not the move will 
	 * capture the other playe'rs 
	 * board piece.
     */
	public static boolean will_chk_piece(int x0, int y0, int x1, int y1, int dx, int dy){
		if(debug){
			System.out.println("will_chk_piece");
		}

		boolean ret = true; // true by default
		int ax0 = (x0+x1)/2; // the gap between x0 and x1 is their avg
		int ay0 = (y0+y1)/2; // the gap between y0 and y1 is their avg 

		if(debug){
			System.out.println("ax0 = "+ax0);
			System.out.println("ay0 = "+ay0);
		}

		if(dx != 2 || dy != 2){
			if(debug){
				System.out.println("dx && dy must be 2");
			}

			// dx and dy must be two => we must be jumping over a piece
			ret = false;
		} else if(board[ax0][ay0] == 0){
			if(debug){
				System.out.println("empty space");
			}

			// jumping over empty space doesn't count
			ret = false;
		} /* else if((ax0 > N || ax0 < N)
				&&(ay0 > N || ay0 < N)){
			if(debug)
				System.out.println("off board location");
			// we should not try to access locations off the board
			ret = false;
		} */ else if(isPlayerOne && (board[ax0][ay0] == 1 || board[ax0][ay0] == 3)){
			if(debug){
				System.out.println("p1 can't capture own pieces");
			}

			// player one can't capture their own pieces (regular or king)
			ret = false;
		} else if(!isPlayerOne && (board[ax0][ay0] == 2 || board[ax0][ay0] == 4)){
			if(debug){
				System.out.println("p2 can't capture own pieces");
			}

			// player two can't capture their own pieces (regular or king)
			ret = false;
		}

		return ret;
	}

    /*
     * @parameters: four integers
     * @description: Actually executes
	 * a move on the board according to 
	 * the conditions it fulfills. Checks
	 * for key game events and outputs a
	 * suitable message to the console.
     * @returns: n/a
     */
	public static void make_mv(int x0, int y0, int x1, int y1){
		if(debug){
			System.out.println("make_mv");
		}

		int dx = Math.abs(x0-x1); // horizontal displacement
		int dy = Math.abs(y0-y1); // vertical displacement

		// actually capture the piece before doing anything else (won't work otherwise)
		if(will_chk_piece(x0, y0, x1, y1, dx, dy)){
			chk_piece(x0, y0, x1, y1);
		}

		board[x1][y1] = board[x0][y0]; // mv piece
		board[x0][y0] = 0; // clear old pos

		// king conversion checks
			// is the board piece a regular one?
			// has the player's regular piece reached the king's N (the end of the board)?
		if((board[x1][y1] == 1
		||  board[x1][y1] == 2)
	    && (isPlayerOne && x1 == N-1)
		|| (!isPlayerOne && x1 == 0)){
			board[x1][y1]+=2; // increment by two to promote to a king

			// no king has been cNned yet
			if(!firstKingWasSpawned){
				pretty_print("ALL HAIL THE KING");
				// a king was cNned — we'll never enter this if statement again
				firstKingWasSpawned = true;
			}
		}

		pretty_print("SUCCESSFUL MOVE");
	}

    /*
     * @parameters: n/a
     * @description: Executes a capture
	 * that eliminates the opposing
	 * players game piece from the board.
     * @returns: n/a
     */
	public static void chk_piece(int x0, int y0, int x1, int y1){	
		if(debug){
			System.out.println("chk_piece");
		}

		int ax0 = (x0+x1)/2; // the gap between x0 and x1 is their avg
		int ay0 = (y0+y1)/2; // the gap between y0 and y1 is their avg 
		board[ax0][ay0] = 0;

		// one of the two players is the first to capture the other player's piece
		if(!firstCaptureWasExecuted){
			pretty_print("FIRST BLOOD");
			// a capture move was executed — we'll never enter this if statement again
			firstCaptureWasExecuted = true;
		}
	}

    /*
     * @parameters: n/a
     * @description: Checks to see if
	 * any of the end game conditions 
	 * have been met. 
     * @returns: n/a
     */
	// EDIT THE ABOVE TEXT
	public static void check_for_win(){
		update_piece_count();

		if(debug){
			System.out.println("check_for_win");
			System.out.println("pp1 = "+pp1);
			System.out.println("pp2 = "+pp2);
			System.out.println("GAMEOVER = "+GAMEOVER);
			System.out.println("isPlayerOne = "+isPlayerOne);
		} 

		if(pp1 == 0){
			GAMEOVER = true;
			pretty_print("PLAYER TWO HAS WON THE GAME");
		} else if(pp2 == 0){
			GAMEOVER = true;
			pretty_print("PLAYER ONE HAS WON THE GAME");
		}
	}
}

// TODO
// final submission
	// make new file
	// remove debug related code 
	// remove dead code

// ensure code is still working

// finish report

