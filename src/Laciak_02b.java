import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

interface OtelloHelperInterface {
	/**
	 * Typ wyliczeniowy reprezentujacy pionka do gry w Otello.
	 * 
	 */
	enum Disk {
		BLACK, WHITE;
	}

	/**
	 * Klasa reprezentuje pozycje w dwu-wymiarowej tablicy uzywanej do zapisu
	 * stanu do gry. Uzycie przechowywanych w obiektach indeksow jest zgodne z ich nazwa
	 * czyli para indeksow index1,index2 wskazuje na pozycje w tablicy zapisana
	 * nastepujaco: tablica[index1][index2]
	 */
	class Position {
		private int index1;
		private int index2;

		public Position(int i1, int i2) {
			index1 = i1;
			index2 = i2;
		}

		public int getIndex1() {
			return index1;
		}

		public int getIndex2() {
			return index2;
		}
	}

	/**
	 * Metoda wykonuje analize planszy do gry i zwraca posortowana tablice
	 * poprawnych ruchow dla gracza poslugujacego sie wskazanym kolorem piona.
	 * 
	 * @param board
	 *            aktualny stan gry - kwadratowa tablica 8x8, pozycje
	 *            nieobsadzone przez piony zawieraja null. Stan gry moze byc
	 *            dowolnie wymyslony - nie musi byc realizacja zadnej konkretnej
	 *            gry.
	 * 
	 * @param playerDisk
	 *            kolor piona, ktorym posluguje sie gracz zlecajacy wykonanie
	 *            analizy planszy
	 * @return posortowana malejaco wzgledem liczby zdobytych pionow przeciwnika
	 *         tablica poprawnych kontynuacji gry. W przypadku braku mozliwosci
	 *         ruchu - tablica o rozmiarze 0.
	 */
	Position[] analyze(Disk[][] board, Disk playerDisk);

}



class OtelloHelper implements OtelloHelperInterface{

	public OtelloHelper() {}
	
	@Override
	public Position[] analyze(Disk[][] board, Disk playerDisk) {
		Set<Position> setPosition = getPositionTable(board, playerDisk);
		Position[] positions = setPosition.toArray(new Position[setPosition.size()]);
		return positions;
	}
	
	private Set<Position> getPositionTable(Disk[][] board, Disk player){
		Map<Position,Integer> mapValues = new HashMap<>();
		int val = 0;
		for(Position p : getPotentialMoves(board)){
			val = getMoveValue(board,player,p);
			if(val > 0){
				mapValues.put(p, val);
			}
		}
		
		ValueComparator cmp = new ValueComparator(mapValues);
		TreeMap<Position,Integer> sorted_map = new TreeMap<Position,Integer>(cmp);
		
		sorted_map.putAll(mapValues);
		
		return sorted_map.keySet();
	}
	
	private List<Position> getPotentialMoves(Disk[][] board){
		List<Position> availMoves = new ArrayList<>();
		Set <Integer> intRepr = new HashSet<>();
		Set<Position> playerCells = new HashSet<>();
		
		for(int x = board[0].length-1 ; x >=0 ; x--){
			for(int y = 0 ; y < board.length ; y++){
				if(board[y][x] != null){
					playerCells.add(new Position(y, x));
				}
			}
		}
		
		for(Position p : playerCells){
			for(Position p1 : getNeighbourFreeCells(board, p)){
				String value = new String(""+p1.getIndex1()+p1.getIndex2());
				if(!intRepr.contains(Integer.parseInt(value))){
					intRepr.add(Integer.parseInt(value));
					availMoves.add(p1);
				}
			}
		}
		return availMoves;
	}
	
	private int getMoveValue(Disk[][] board, Disk player, Position p){
		int sumOfPoints = 0;
		try{
			for(Direction d : Direction.values()){
				sumOfPoints+= countDirection(d,board,p,player);
			}
		}
		catch(ArrayIndexOutOfBoundsException e){
			
		}
		
		return sumOfPoints;
	}
	
	private int countDirection(Direction direction, Disk[][] board, Position p, Disk player) throws ArrayIndexOutOfBoundsException{
		int sumOfPoints = 0;
		int stepX = direction.getX();
		int stepY = direction.getY();
		int steps = 0;
		
		while (board[p.getIndex1() + stepX][p.getIndex2() + stepY] != null
				&& board[p.getIndex1() + stepX][p.getIndex2() + stepY] != player) {
			if(stepX > 0)
				stepX++;
			if(stepY >0)
				stepY++;
			if(stepY <0)
				stepY--;
			if(stepX <0)
				stepX--;
			
			steps++;
		}
		if (board[p.getIndex1() + stepX][p.getIndex2() + stepY] == player) {
			sumOfPoints += steps;
		}
		
		return sumOfPoints;
	}
	
	private Set<Position> getNeighbourFreeCells(Disk[][] board, Position p){
		Set<Position> neighbours = new HashSet<>();
		
		if(p.getIndex2() > 0){
			if(p.getIndex1() > 0)
				neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()-1));
			neighbours.add(new Position(p.getIndex1(),p.getIndex2()-1));
			if(p.getIndex1() < board[0].length - 1)
				neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()-1));
		}
		if(p.getIndex1() > 0)
			neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()));
		// current position
		if(p.getIndex1() < board[0].length - 1)
			neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()));
		
		if(p.getIndex2() < board.length - 1){
			if(p.getIndex1() > 0)
				neighbours.add(new Position(p.getIndex1()-1,p.getIndex2()+1));
			neighbours.add(new Position(p.getIndex1(),p.getIndex2()+1));
			if(p.getIndex1() < board[0].length - 1)
				neighbours.add(new Position(p.getIndex1()+1,p.getIndex2()+1));
		}
		
		Set<Position> positionsToRemove = new HashSet<>();
		
		for(Position n : neighbours){
			if(board[n.getIndex1()][n.getIndex2()] != null){
				positionsToRemove.add(n);
			}
		}
		neighbours.removeAll(positionsToRemove);
		
		
		return neighbours;
	}
	
	public static void printBoard(Disk[][] board){
		int flag = 0;
		for (int x = board[0].length+1 - 1; x >= 0; x--) {
			if(x == 8)
				System.out.print("[ ]");
			else
				System.out.print("[" + x + "]");
			for (int y = 0; y < board.length; y++) {
				if (flag == 0)
					System.out.print("[" + y + "]");
				else {
					if (board[y][x] == null) {
						System.out.print("[ ]");
					} else if (board[y][x] == Disk.WHITE) {
						System.out.print("[O]");
					} else {
						System.out.print("[X]");
					}
				}
			}
			flag = 1;
			System.out.println();
		}
	}
	
	enum Direction {
		VERT_UP(0,1), VERT_DOWN(0,-1), HOR_LEFT(-1,0), HOR_RIGHT(1,0), DIA_LEFT_UP(-1,1), DIA_LEFT_DOWN(-1,-1), DIA_RIGHT_UP(1,1), DIA_RIGHT_DOWN(1,-1);
		
		private int x;
		private int y;
		
		Direction(int x, int y){
			this.x = x;
			this.y = y;
		}
		
		public int getX(){
			return this.x;
		}
		
		public int getY(){
			return this.y;
		}
		
	}
	
	class ValueComparator implements Comparator<Position> {
	    Map<Position, Integer> base;

	    public ValueComparator(Map<Position, Integer> base) {
	        this.base = base;
	    }

	    public int compare(Position a, Position b) {
	        if (base.get(a) >= base.get(b)) {
	            return -1;
	        } else {
	            return 1;
	        }
	    }
	}
}
