/**
 * Student: Damilola Babalola
 * Course: ICS3U1
 * Date: 01/9/2019
 * Checkers with a 4 by 4 board.
 * Minimal version.
**/

import java.util.*;
import java.lang.*;

public class checkers4{
	// global scanner (wrapper functions here are a bad idea)
	static Scanner s = new Scanner(System.in); 
	//boolean[] settings = new boolean[]; 
		// 0 - enable undo option
		// 1 - mandatory capture moves (must make capture move whenever possible)
		// 2 - enable combo moves
	// int bx = 4; // board size x (horizontal)
	// int by = 4; // board size y (vertical)

	static int[][] board = new int[4][4];
		// 0 - empty space
		// 1 - player 1 piece (light)
		// 2 - player 2 piece (dark)

	static boolean isPlayerOne = true;
	static boolean debug = true;

	public static void main(String[] args){
		Random r = new Random();
		intro_message();
		load_game();
		game_loop();
		s.close();
	}

	// Ye Olde Wrapper Functions
	public static void error_exit(){ // REMOVE FROM FINAL SUBMISSION
		System.out.println();
		System.out.println("~ ERROR EXIT ~");
		for(int i=0; i<4; i++){
			for(int k=0; k<4; k++)
				System.out.print(board[i][k]+" ");
			System.out.println();
		}
		System.out.println();
		System.out.println("isPlayerOne = "+isPlayerOne);
		System.out.println("debug = "+debug);
		System.exit(0);
	}
	public static void pretty_print(String str){
		System.out.println();
		System.out.println("--------------------------------------------------");
		System.out.println(str);
		System.out.println("--------------------------------------------------");
		System.out.println();
	}
	public static String get_str(String prompt){
		System.out.print(prompt);
		return s.nextLine();
	}
	public static int get_int(String prompt){
		System.out.print(prompt);
		return s.nextInt();
	}
	public static boolean get_bool(String prompt){
		// FOR FUTURE USE
		System.out.print(prompt);
		return s.nextBoolean();
	}
	public static void intro_message(){
		System.out.println();
		System.out.println("Hello! Welcome to Checkers.");
		System.out.println();
		// ascii banner
			// find ascii txt generator to make intro message (paste banner into intro_message func.)
	}
	public static void load_game(){
		System.out.println("~ The rules ~");
		System.out.println("‘l’ is the light side's (player 1) pieces");
		System.out.println("'d' is the dark side's (player 2) pieces");
		System.out.println("‘e’ indicates empty space on the board");
		for(int i=0; i<4; i++){ // initialize board
			board[0][i] = 1;
			board[1][i] = 0;
			board[2][i] = 0;
			board[3][i] = 2;
		}
		// splash screen
			// get user options
	}

	public static void game_loop(){
		String opt = "";
		while(!opt.equals("exit")){
			System.out.println();
			draw_board();
			System.out.println();
			if(isPlayerOne){
				System.out.print("~ Player one, make your move ~");
			} else{
				System.out.print("~ Player two, make your move ~");
			}
			System.out.println();

			// start the chain of move related functions
			begin_mk_mv();
		}
	}

	public static void draw_board(){
		for(int i=0; i<4; i++){
			for(int k=0; k<4; k++){
				if(board[i][k] == 0) // empty space
					System.out.print("e");
				if(board[i][k] == 1) // player 1 piece (light)
					System.out.print("l"); 
				if(board[i][k] == 2) // player 2 piece (dark)
					System.out.print("d");
			}
			System.out.println();
		}
	}

	public static void begin_mk_mv(){
		int lx0 = 0;
		int ly0 = 0;
		int lx1 = 0;
		int ly1 = 0;

		// loop until we get a valid move
		do{
			lx0 = get_int("enter the row number to move from: ") - 1;
			ly0 = get_int("enter the column number to move from: ") - 1;
			lx1 = get_int("enter the row number to move to: ") - 1;
			ly1 = get_int("enter the column number to move to: ") - 1;
		}while(invalid_mv(lx0, ly0, lx1, ly1));

		// the move is not invalid—make it
		make_mv(lx0, ly0, lx1, ly1);
		isPlayerOne = isPlayerOne ? false : true; // switch to other player
	}

	public static boolean invalid_mv(int x0, int y0, int x1, int y1){
		boolean ret = false; // assume good intentions at first

		// can't pass through walls will trigger Array OOB
		if(x0 > 3 || y0 > 3 || x1 > 3 || y1 > 3 
		|| x0 < 0 || y0 < 0 || x1 < 0 || y1 < 0){
			System.out.println("CANNOT PASS THROUGH WALLS");
			ret = true;
		} else if(board[x0][y0] == 0){
			System.out.println("ERROR: NO BOARD PIECE WAS FOUND AT THAT LOCATION");
			ret = false;
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

		System.out.println("ax0 = "+ax0);
		System.out.println("ay0 = "+ay0);

		if(dx != 2 || dy != 2){
			// dx and dy must be two => we must be jumping over a piece
			ret = false;
		} else if(board[ax0][ay0] == 0){
			// jumping over empty space doesn't count
			ret = false;
		} else if((ax0 > 3 || ax0 < 0)
				&&(ay0 > 3 || ay0 < 0)){
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
	}

	public static void chk_piece(int x0, int y0, int x1, int y1){	
		if(debug){
			System.out.println("chk_piece");
		}

		int ax0 = (x0+x1)/2; // the gap between x0 and x1 is their avg
		int ay0 = (y0+y1)/2; // the gap between y0 and y1 is their avg 
		board[ax0][ay0] = 0;
	}
}

// TODO
// SEE GDOC FOR EXAMPLE OF PROGRAM OUTPUT
// forbid backward moves
	// pretty_print("ERROR: CANNOT MOVE REGULAR PIECE BACKWARDS");
// display message to indicate successuful moves

// configure end of game conditions
	// make state variables for the number of each players' pieces
	// add check for end of game somewhere (which player wins?)

// code up an 8 x 8 board => new file (checkers8.java)
	// initialize board pieces (see online images)

// kings & regulars
	// identify them
	// convert regulars to kings (NEW FUNCTION FOR THIS)
		// remember context: p1 & p2 moves to diff. ends of the board to make conversion
	// kings can move forwards/backwards diagonally by one or two (to make capture move)

// add game text / banners for not
	// "first blood" => for first player to capture a piece
	// "ALL HAIL THE KING" => when any player spawns a new king
	// "welp" => for player that loses large amount (n>3) of pieces in one swipe

// game options
	// flying kings (can move arbitrary diagonal distances)
	// board size

// clear instructions for user
	// link to clear chess instructions on the web
	// consider inline instructions, NOT a blurb of txt

// better function to draw board (experiment in sublime txt—construct your own)
// implement combo-moves (prompt the user)


