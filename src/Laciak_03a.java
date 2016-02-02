//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//
//class ReversiBoard implements ReversiBoardInterface{
//
//	private Disk[][] board;
//	private Disk player;
//	private boolean playerMoved = false;
//
//	public ReversiBoard(){}
//
//	@Override
//	public void setGameState(Disk[][] board, Disk nextPlayer) {
//		this.board = board;
//		this.player = nextPlayer;
//		this.playerMoved = false;
//	}
//
//	@Override
//	public boolean canWeContinueTheGame() {
//		int availableMovesBLACK = getPositionTable(this.board,Disk.BLACK).size();
//		int availableMovesWHITE = getPositionTable(this.board,Disk.WHITE).size();
//		if(availableMovesBLACK == 0 && availableMovesWHITE ==0)
//			return false;
//		else
//			return true;
//	}
//
//	@Override
//	public Disk nextPlayer() throws CanNotContinue {
//		if (this.playerMoved) {
//			if (!canWeContinueTheGame()) {
//				throw new CanNotContinue();
//			} else {
//				Disk opponent = (this.player == Disk.BLACK) ? Disk.WHITE : Disk.BLACK;
//				int availableMoves = getPositionTable(this.board, opponent).size();
//				if (availableMoves == 0) {
//					// nie zmieniaj gracza
//				}
//				else {
////					Disk player = (this.player == Disk.BLACK) ? Disk.WHITE : Disk.BLACK;
//					this.player = opponent;
//					this.playerMoved = false;
//				}
//			}
//		}
//		return this.player;
//	}
//
//	@Override
//	public int move(Position pos) throws IllegalMove {
//		int moveValue = 0;
//
//		Map<Integer,List<Position>> result = getMoveValue(this.board, this.player, pos);
//		moveValue += (Integer)result.keySet().toArray()[0];
//		List<Position> gotPoints = new ArrayList<>(result.get(moveValue));
//		if(moveValue == 0 || board[pos.getIndex1()][pos.getIndex2()] != null){
//			this.playerMoved = false;
//			throw new IllegalMove();
//		}
//		else {
//			this.board[pos.getIndex1()][pos.getIndex2()] = this.player;
//			addBoardMarks(gotPoints);
//			this.playerMoved = true;
//			return moveValue;
//		}
//	}
//
//	private void addBoardMarks(List<Position> points){
//		for(Position p : points){
//			this.board[p.getIndex1()][p.getIndex2()] = this.player;
//		}
//	}
//
//	@Override
//	public Disk[][] getBoard() {
//		return board;
//	}
//
//	@Override
//	public int getResult(Disk player) {
//		int result = 0;
//		for(Disk[] y : this.board){
//			for(Disk x : y){
//				if(x== player)
//					result++;
//			}
//		}
//		return result;
//	}
//
//	public static void printBoard(Disk[][] board){
//		int flag = 0;
//		for (int x = board[0].length+1 - 1; x >= 0; x--) {
//			if(x == 8)
//				System.out.print("[ ]");
//			else
//				System.out.print("[" + x + "]");
//			for (int y = 0; y < board.length; y++) {
//				if (flag == 0)
//					System.out.print("[" + y + "]");
//				else {
//					if (board[y][x] == null) {
//						System.out.print("[ ]");
//					} else if (board[y][x] == Disk.WHITE) {
//						System.out.print("[O]");
//					} else {
//						System.out.print("[X]");
//					}
//				}
//			}
//			flag = 1;
//			System.out.println();
//		}
//	}
//
//
//	private Set<Position> getPositionTable(Disk[][] board, Disk player){
//		Map<Position,Integer> mapValues = new HashMap<>();
//		int val = 0;
//		for(Position p : getPotentialMoves(board)){
//			val = (Integer)getMoveValue(board,player,p).keySet().toArray()[0];
//			if(val > 0){
//				mapValues.put(p, val);
//			}
//		}
//
//		ValueComparator cmp = new ValueComparator(mapValues);
//		TreeMap<Position,Integer> sorted_map = new TreeMap<Position,Integer>(cmp);
//
//		sorted_map.putAll(mapValues);
//
//		return sorted_map.keySet();
//	}
//
//	private List<Position> getPotentialMoves(Disk[][] board){
//		List<Position> availMoves = new ArrayList<>();
//		Set <Integer> intRepr = new HashSet<>();
//		Set<Position> playerCells = new HashSet<>();
//
//		for(int x = board[0].length-1 ; x >=0 ; x--){
//			for(int y = 0 ; y < board.length ; y++){
//				if(board[y][x] != null){
//					playerCells.add(new Position(y, x));
//				}
//			}
//		}
//
//		for(Position p : playerCells){
//			for(Position p1 : getNeighbourFreeCells(board, p)){
//				String value = new String(""+p1.getIndex1()+p1.getIndex2());
//				if(!intRepr.contains(Integer.parseInt(value))){
//					intRepr.add(Integer.parseInt(value));
//					availMoves.add(p1);
//				}
//			}
//		}
//		return availMoves;
//	}
//
//	private Map<Integer,List<Position>> getMoveValue(Disk[][] board, Disk player, Position p){
//		int sumOfPoints = 0;
//		List<Position> positionsGot = new ArrayList<>();
//		Map<Integer,List<Position>> moveResultMap = new HashMap<>();
//
//		for(Direction d : Direction.values()){
//			try {
//				Map<Integer, List<Position>> moveResult = countDirection(d, board, p, player);
//				int key = (Integer) moveResult.keySet().toArray()[0];
//				sumOfPoints += key;
//				positionsGot.addAll(moveResult.get(key));
//			}
//			catch(ArrayIndexOutOfBoundsException e){
//			}
//
//		}
//
//		moveResultMap.put(sumOfPoints, positionsGot);
//
//		return moveResultMap;
//	}
//
//	private Map<Integer, List<Position>> countDirection(Direction direction, Disk[][] board, Position p, Disk player) throws ArrayIndexOutOfBoundsException{
//		int sumOfPoints = 0;
//		int stepX = direction.getX();
//		int stepY = direction.getY();
//		int steps = 0;
//		List<Position> pointsList = new ArrayList<>();
//		Map<Integer, List<Position>> movesMap = new HashMap<>();
//		while (board[p.getIndex1() + stepX][p.getIndex2() + stepY] != null
//				&& board[p.getIndex1() + stepX][p.getIndex2() + stepY] != player) {
//			pointsList.add(new Position(p.getIndex1() + stepX, p.getIndex2() + stepY));
//			if(stepX > 0)
//				stepX++;
//			if(stepY >0)
//				stepY++;
//			if(stepY <0)
//				stepY--;
//			if(stepX <0)
//				stepX--;
//			steps++;
//		}
//		if (board[p.getIndex1() + stepX][p.getIndex2() + stepY] == player) {
//			sumOfPoints += steps;
//		}
//		else {
//			pointsList.clear();
//		}
//		movesMap.put(sumOfPoints, pointsList);
//
//		return movesMap;
//	}
//
//	private Set<Position> getNeighbourFreeCells(Disk[][] board, Position p){
//		Set<Position> neighbours = new HashSet<>();
//
//		if(p.getIndex2() > 0){
//			if(p.getIndex1() > 0)
//				neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()-1));
//			neighbours.add(new Position(p.getIndex1(),p.getIndex2()-1));
//			if(p.getIndex1() < board[0].length - 1)
//				neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()-1));
//		}
//		if(p.getIndex1() > 0)
//			neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()));
//		// current position
//		if(p.getIndex1() < board[0].length - 1)
//			neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()));
//
//		if(p.getIndex2() < board.length - 1){
//			if(p.getIndex1() > 0)
//				neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()+1));
//			neighbours.add(new Position(p.getIndex1(),p.getIndex2()+1));
//			if(p.getIndex1() < board[0].length - 1)
//				neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()+1));
//		}
//
//		Set<Position> positionsToRemove = new HashSet<>();
//
//		for(Position n : neighbours){
//			if(board[n.getIndex1()][n.getIndex2()] != null){
//				positionsToRemove.add(n);
//			}
//		}
//		neighbours.removeAll(positionsToRemove);
//
//
//		return neighbours;
//	}
//
//	enum Direction {
//		VERT_UP(0,1), VERT_DOWN(0,-1), HOR_LEFT(-1,0), HOR_RIGHT(1,0), DIA_LEFT_UP(-1,1), DIA_LEFT_DOWN(-1,-1), DIA_RIGHT_UP(1,1), DIA_RIGHT_DOWN(1,-1);
//
//		private int x;
//		private int y;
//
//		Direction(int x, int y){
//			this.x = x;
//			this.y = y;
//		}
//
//		public int getX(){
//			return this.x;
//		}
//
//		public int getY(){
//			return this.y;
//		}
//
//	}
//
//	class ValueComparator implements Comparator<Position> {
//		Map<Position, Integer> base;
//
//		public ValueComparator(Map<Position, Integer> base) {
//			this.base = base;
//		}
//
//		public int compare(Position a, Position b) {
//			if (base.get(a) >= base.get(b)) {
//				return -1;
//			} else {
//				return 1;
//			}
//		}
//	}
//
//}
//
