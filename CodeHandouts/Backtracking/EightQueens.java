import java.util.Scanner;

public class EightQueens {
	private char[][] board;
	private boolean[] safeRows;
	private boolean[] safeLeftDiags;
	private boolean[] safeRightDiags;
	private int sols;
	private Thread thread;
	private Monitor monitor;
	
	public EightQueens() {
		board = new char[8][8];
		safeRows = new boolean[8];
		safeLeftDiags = new boolean[15];
		safeRightDiags = new boolean[15];
		sols = 0;
		monitor = new Monitor();
		
		for(int i=0; i<8; i++) {
			safeRows[i] = true;
			for(int j=0; j<8; j++) {
				board[i][j] = '-';
			}
		}
		for(int i=0; i<15; i++) {
			safeLeftDiags [i] = safeRightDiags[i] = true;
		}
	}
	
	public void start() {
		thread = new Thread(new QueensRunnable());
		thread.start();
		Scanner scan = new Scanner(System.in);
		while(scan.hasNextLine()) {
			scan.nextLine();
			monitor.unpause();
		}
	}
	
	private boolean safe(int row, int col) {
		return safeRows[row] && safeLeftDiags[row+col] && safeRightDiags[row - col + 7];
	}
	
	private void printBoard() {
		for(int i=0; i<8; i++) {
			for(int j=0; j<8; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println("----------------------------" + sols);
	}
	
	private void solve(int col) {
		for(int row=0; row<8; row++) {
			if(safe(row, col)) {
				board[row][col] = 'Q';
				safeRows[row] = false;
				safeLeftDiags[row+col] = false;
				safeRightDiags[row - col + 7] = false;
				
				if(col == 7) {
					//got a solution
					sols++;
					printBoard();
					monitor.pause();
				} else {
					solve(col+1);
				}
				
				board[row][col] = '-';
				safeRows[row] = true;
				safeLeftDiags[row+col] = true;
				safeRightDiags[row - col + 7] = true;		
						
			}
		}
		
	}
	
	private class QueensRunnable implements Runnable {

		@Override
		public void run() {
			solve(0);
		}
		
	}
	
	private class Monitor {
		private synchronized void pause() {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private synchronized void  unpause() {
			notify();
		}
	}
	
	public static void main(String[] args) {
		EightQueens eq = new EightQueens();
		eq.start();
	}

}
