/**
 * Student: Damilola Babalola
 * Course: ICS3U1
 * Date: 01/9/2019
 * Checkers with an 8 by 8 board.
 * Minimal version.
**/

import java.util.*;
import java.lang.*;

public class checkers8{
	// global scanner (wrapper functions here are a bad idea)
	static Scanner s = new Scanner(System.in); 

	static int row = 8; // horizontal/row length
	static int col = 8; // vertical/column length
	static int[][] board = new int[row][col];
		// 0 - empty space
		// 1 - player 1 piece (light)
		// 2 - player 2 piece (dark)
	static boolean debug = false; // true;

	static int pp1; // player one piece count
	static int pp2; // player two piece count
	static boolean isPlayerOne = true;
	static boolean GAMEOVER = false;
	//boolean[] settings = new boolean[]; 
		// 0 - enable undo option
		// 1 - mandatory capture moves (must make capture move whenever possible)
		// 2 - enable combo moves

	public static void main(String[] args){
		Random r = new Random();
		intro_message();
		load_game();
		game_loop();
		s.close();
	}

	// Ye Olde Wrapper Functions
	public static void error_exit(){ // REMOVE FROM FINAL SUBMISSION
		pretty_print("~ ERROR EXIT ~");

		for(int i=0; i<row; i++){
			for(int k=0; k<col; k++)
				System.out.print(board[i][k]+" ");
			System.out.println();
		}

		System.out.println();
		System.out.println("isPlayerOne = "+isPlayerOne);
		System.out.println("debug = "+debug);
		System.out.println("row = "+row);
		System.out.println("col = "+col);
		System.out.println("pp1 = "+pp1);
		System.out.println("pp2 = "+pp2);
		System.out.println("GAMEOVER = "+GAMEOVER);
		System.exit(0);
	}

	public static void pretty_print(String str){
		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println(str);
		System.out.println("--------------------------------------------------");
		System.out.println();
	}

	// all the get_* methods serve the purpose of sanitizing input
	public static String get_str(String prompt){
		System.out.print(prompt);
		String input = "";

		while(!s.hasNextLine()){
			System.out.println("ERROR: YOU DID NOT ENTER A STRING.");
			System.out.print("TRY AGAIN: ");
		}

		input = s.nextLine();
		return input;
	}

	public static int get_int(String prompt){
		System.out.print(prompt);
		int input = 0;

		while(!s.hasNextInt()){
			System.out.println("ERROR: YOU DID NOT ENTER AN INTEGER.");
			System.out.print("TRY AGAIN: ");
		}

		while(input < 0){
			System.out.println("ERROR: YOU MUST ENTER AN INTEGER GREATER THAN 0");
			System.out.print("TRY AGAIN: ");
			input = s.nextInt();
		}

		input = s.nextInt();
		return input;
	}
	public static boolean get_bool(String prompt){
		// FOR FUTURE USE
		// (to get user settings)
		System.out.print(prompt);
		boolean input;

		while(!s.hasNextBoolean()){
			System.out.println("ERROR: YOU DID NOT ENTER A BOOLEAN.");
			System.out.print("TRY AGAIN: ");
		}

		input = s.nextBoolean();
		return input;
	}

	public static void intro_message(){
		pretty_print("Hello! Welcome to Checkers.");
		// ascii banner
			// find ascii txt generator to make intro message (paste banner into intro_message func.)
	}

	public static void load_game(){
		System.out.println("~ THE RULES ~");

		System.out.println("Player 1's Pieces");
		System.out.println("    'r1' player one's regular pieces");
		System.out.println("    'k1' player one's king pieces");
		System.out.println();
		System.out.println("Player 2's Pieces");
		System.out.println("    'r2' player two's regular pieces");
		System.out.println("    'k2' player two's king pieces");

		System.out.println();

		// iterating across columns
		for(int i=1; i<col; i+=2){
			// fill in the odd rows
			board[0][i] = 1;
			board[2][i] = 1;

			board[6][i] = 2;
		}
		for(int i=0; i<col; i+=2){
			// fill in the even rows
			board[1][i] = 1;

			board[5][i] = 2;
			board[7][i] = 2;
		}
		// the rest of the board will be zero-initialized
		// splash screen
			// get user options
	}

	public static void draw_board(){
		// reset the count to 0
		pp1 = 0;
		pp2 = 0;

		System.out.println();
		System.out.println("|+---+----+----+----+----+----+----+---+|");
		for(int i=0; i<row; i++){
			for(int k=0; k<col; k++){
				System.out.print("|");
				if(board[i][k] == 0){ // empty space
					System.out.print("    ");
				} else if(board[i][k] == 1){ // player 1 piece
					pp1++;
					System.out.print(" r1 "); 
				} else if(board[i][k] == 2){ // player 2 piece
					pp2++;
					System.out.print(" r2 ");
				} else if(board[i][k] == 3){ // player 1 king piece
					pp1++;
					System.out.print(" k1 "); 
				} else if(board[i][k] == 4){ // player 2 king piece
					System.out.print(" k2 "); 
					pp2++;
				}
			}
			System.out.println("|");
			System.out.println("|+---+----+----+----+----+----+----+---+|");
		}

		if(debug){
			System.out.println("pp1 = "+pp1);
			System.out.println("pp2 = "+pp2);
		}
		System.out.println();
	}

	public static void game_loop(){
		String opt = "";
		while(!opt.equals("exit") && !GAMEOVER){ // the loop ends when it's gameover
			System.out.println();
			if(isPlayerOne){
				System.out.print("~ Player one, make your move ~");
			} else{
				System.out.print("~ Player two, make your move ~");
			}

			// start the chain of move related functions
			begin_mk_mv();
		}
	}

	public static void begin_mk_mv(){
		int lx0 = 0;
		int ly0 = 0;
		int lx1 = 0;
		int ly1 = 0;

		// loop until we get a valid move
		do{
			draw_board();
			lx0 = get_int("enter the row number to move from: ") - 1;
			ly0 = get_int("enter the column number to move from: ") - 1;
			lx1 = get_int("enter the row number to move to: ") - 1;
			ly1 = get_int("enter the column number to move to: ") - 1;
		}while(invalid_mv(lx0, ly0, lx1, ly1));

		// the move is not invalid—make it
		make_mv(lx0, ly0, lx1, ly1);
		isPlayerOne = isPlayerOne ? false : true; // switch to other player

		// check for player win
		check_for_win();
	}

	public static boolean invalid_mv(int x0, int y0, int x1, int y1){
		boolean ret = false; // assume good intentions at first

		if(x0 > col || y0 > col || x1 > col || y1 > col ){
			// we differentiate between passing through walls and
			// attempting a move that takes a piece off the board
			pretty_print("ERROR: MOVE CANNOT EXCEED THE SIZE OF THE BOARD");
			ret = true;
		} else if(x0 > row || y0 > col || x1 > row || y1 > col || x0 < 0 || y0 < 0 || x1 < 0 || y1 < 0){
			pretty_print("CANNOT PASS THROUGH WALLS");
			ret = true;
		} else if(board[x0][y0] == 0){
			pretty_print("ERROR: NO BOARD PIECE WAS FOUND AT THAT LOCATION");
			ret = true;
		} else if(isPlayerOne && x1 < x0){
			pretty_print("ERROR: CANNOT MOVE BACKWARDS");
			ret = true;
		} else if(!isPlayerOne && x1 > x0){
			pretty_print("ERROR: CANNOT MOVE BACKWARDS");
			ret = true;
		} else if(isPlayerOne && board[x0][y0] == 2){
			pretty_print("ERROR: PLAYER ONE (that means you!) CANNOT MOVE PLAYER TWO's PIECES");
			ret = true;
		} else if(!isPlayerOne && board[x0][y0] == 1){
			pretty_print("ERROR: PLAYER TWO (that means you!) CANNOT MOVE PLAYER ONE's PIECES");
			ret = true;
		} else if(!isValidDiagonalMove(x0, y0, x1, y1)){ // enforces diagonal-move-only
			pretty_print("ERROR: INVALID DIAGONAL MOVE");
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

	public static boolean isValidDiagonalMove(int x0, int y0, int x1, int y1){
		boolean ret = false; // false by default

		int dx = Math.abs(x0-x1); // horizontal displacement
		int dy = Math.abs(y0-y1); // vertical displacement
		// stored to avoid double calls
		boolean will_chk_piece_b = will_chk_piece(x0, y0, x1, y1, dx, dy); 

		if(dx == 1 && dy == 1){
			ret = true;
		}
		if(dx == 2 && dy == 2 && will_chk_piece_b){
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
			// dx and dy must be two => we must be jumping over a piece
			ret = false;
		} else if(board[ax0][ay0] == 0){
			// jumping over empty space doesn't count
			ret = false;
		} else if((ax0 > row || ax0 < col)
				&&(ay0 > row || ay0 < col)){
			// we should not try to access locations off the board
			ret = false;
		} else if(isPlayerOne && board[ax0][ay0] == 1){
			// player one can't capture their own pieces
			ret = false;
		} else if(!isPlayerOne && board[ax0][ay0] == 2){
			// player two can't capture their own pieces
			ret = false;
		}
		// will add cases as needed

		return ret;
	}

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
		pretty_print("SUCCESSFUL MOVE");
	}

	public static void chk_piece(int x0, int y0, int x1, int y1){	
		if(debug){
			System.out.println("chk_piece");
		}

		int ax0 = (x0+x1)/2; // the gap between x0 and x1 is their avg
		int ay0 = (y0+y1)/2; // the gap between y0 and y1 is their avg 
		board[ax0][ay0] = 0;
	}

	public static void check_for_win(){
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
// SEE GDOC FOR EXAMPLE OF PROGRAM OUTPUT

// add a way to exit
// kings & regulars
	// identify them
	// convert regulars to kings (NEW FUNCTION FOR THIS)
		// remember context: p1 & p2 moves to diff. ends of the board to make conversion
		// display message when that happens - pretty_print()
	// kings can move forwards/backwards diagonally by one or two (to make capture move)
//

// add game text / banners for not
	// "first blood" => for first player to capture a piece
	// "ALL HAIL THE KING" => when any player spawns a new king
	// "welp" => for player that loses large amount (n>3) of pieces in one swipe

// BETTER VISUALS FOR GAME BOARD

// game options
	// flying kings (can move arbitrary diagonal distances)
	// board size

// clear instructions for user
	// link to clear chess instructions on the web
	// consider inline instructions, NOT a blurb of txt

// better function to draw board (experiment in sublime txt—construct your own)
// implement combo-moves (prompt the user)

