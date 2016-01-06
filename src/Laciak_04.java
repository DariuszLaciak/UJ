import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import java.util.Set;
import java.util.TreeMap;

class BetterOtelloHelper implements BetterOtelloHelperInterface {
    private Disk[][] firstBoard;

    public static void main(String[] args) {
        Disk[][] board = new Disk[8][8];
        Disk player = Disk.BLACK;

        int y = 0;
        for (Disk[] board1 : board) {
            int x = 0;
            for (Disk d : board1) {
                board[x][y] = Disk.WHITE;
                x++;
            }
            y++;
        }
        board[3][4] = Disk.BLACK;
        board[0][0] = null;
        board[0][7] = null;
        board[7][0] = null;
        board[7][7] = null;

//        board[3][3] = Disk.BLACK;
//        board[4][4] = Disk.BLACK;
//        board[3][4] = Disk.WHITE;
//        board[4][3] = Disk.WHITE;

        BetterOtelloHelper boh = new BetterOtelloHelper();
        BetterOtelloHelper.printBoard(board);
//		for (Position p : boh.analyze_old(board, player)) {
//			System.out.println(p.getIndex1() + " " + p.getIndex2());
//		}
        Map<Integer, Set<List<Position>>> res = boh.analyze(board, player);
//        for (Entry<Integer, Set<List<Position>>> entry : res.entrySet()) {
//            System.out.print("\n" + entry.getKey() + ": ");
//            for (List<Position> list : entry.getValue()) {
//                System.out.print("{");
//                for (Position p : list) {
//                    System.out.print("[" + p.getIndex1() + ", " + p.getIndex2() + "]");
//                }
//                System.out.print("} ");
//            }
//        }
    }

    @Override
    public Map<Integer, Set<List<Position>>> analyze(Disk[][] board, Disk playerDisk) {
        firstBoard = makeCopy(board);
        Map<Integer, Set<List<Position>>> result = new TreeMap<>();
        Position[] firstIteration = analyze_old(board,playerDisk);
        Map<Position,List<Position>> res;
        for(Position p : firstIteration){
            board = makeCopy(firstBoard);
            res = new HashMap<>();
            res.put(p,new ArrayList<>());
            res = checkTreeResult(p,board,playerDisk,res);

            System.out.println("["+p.getIndex1() + ", " + p.getIndex2()+"] =>");
            List<Position> tempList = new ArrayList<>();
            for(Entry<Position,List<Position>> entry: res.entrySet()){
                if(entry.getValue().size() > tempList.size()){
                    tempList = entry.getValue();
                }

            }
            for(Position po : tempList){
                System.out.print("["+po.getIndex1()+","+po.getIndex2()+"] ");
            }
            System.out.println();

        }

        return result;
    }

    private boolean arePointsInTreeAlready(List<Position> pointsToCheck, List<Position> list){
        boolean contains = false;
        for(Position p1 : pointsToCheck){
            if(list.contains(p1)){

            }
        }

        return (list.containsAll(pointsToCheck));
    }

    private Disk[][] makeCopy(Disk[][] board){
        Disk[][] copy = new Disk[board.length][board[0].length];

        for(int i=0; i<board.length; i++)
            for(int j=0; j<board[i].length; j++)
                copy[i][j]=board[i][j];

        return copy;
    }

    private Map<Position,List<Position>> checkTreeResult(Position p, Disk[][] board, Disk player,Map<Position,List<Position>> result){

        Disk[][] tempBoard = move(board,player,p);
        if(!canOpponentMove(tempBoard,player) && canPlayerMove(tempBoard,player)){
            Position[] newMoves = analyze_old(tempBoard,player);
            for(Position move : newMoves){
                result = addMoveToList(result,p,move);
            }
            for(Position move : newMoves){
                result = checkTreeResult(move,tempBoard,player,result);
            }
        }

        return result;
    }

    private Map<Position,List<Position>> addMoveToList(Map<Position,List<Position>> map, Position prevMove, Position newMove){
        List<Position> list = map.get(prevMove);
        if(list != null)
            map.get(prevMove).add(newMove);
        else{
            list = new ArrayList<>();
            list.add(newMove);
            map.put(prevMove,list);
        }
        return map;
    }

    public Position[] analyze_old(Disk[][] board, Disk playerDisk) {
        Set<Position> setPosition = getPositionTable(board, playerDisk);
        Position[] positions = setPosition.toArray(new Position[setPosition.size()]);
        return positions;
    }

    private Disk[][] addBoardMarks(Disk[][] board, Disk player, List<Position> points){
        for(Position p : points){
            board[p.getIndex1()][p.getIndex2()] = player;
        }
        return board;
    }

    private Set<Position> getPositionTable(Disk[][] board, Disk player){
        Map<Position,Integer> mapValues = new HashMap<>();
        int val = 0;
        for(Position p : getPotentialMoves(board)){
            val = (Integer)getMoveValue(board,player,p).keySet().toArray()[0];
            if(val > 0){
                mapValues.put(p, val);
            }
        }

        ValueComparator cmp = new ValueComparator(mapValues);
        TreeMap<Position,Integer> sorted_map = new TreeMap<Position,Integer>(cmp);

        sorted_map.putAll(mapValues);

        return sorted_map.keySet();
    }

    private List<Position> getPotentialMoves(Disk[][] board) {
        List<Position> availMoves = new ArrayList<>();
        Set<Integer> intRepr = new HashSet<>();
        Set<Position> playerCells = new HashSet<>();

        for (int x = board[0].length - 1; x >= 0; x--) {
            for (int y = 0; y < board.length; y++) {
                if (board[y][x] != null) {
                    playerCells.add(new Position(y, x));
                }
            }
        }

        for (Position p : playerCells) {
            for (Position p1 : getNeighbourFreeCells(board, p)) {
                String value = new String("" + p1.getIndex1() + p1.getIndex2());
                if (!intRepr.contains(Integer.parseInt(value))) {
                    intRepr.add(Integer.parseInt(value));
                    availMoves.add(p1);
                }
            }
        }
        return availMoves;
    }

    private Map<Integer,List<Position>> getMoveValue(Disk[][] board, Disk player, Position p){
        int sumOfPoints = 0;
        List<Position> positionsGot = new ArrayList<>();
        Map<Integer,List<Position>> moveResultMap = new HashMap<>();

        for(Direction d : Direction.values()){
            Map<Integer,List<Position>> moveResult = countDirection(d,board,p,player);
            if(moveResult.size() != 0){
                int key = (Integer)moveResult.keySet().toArray()[0];
                sumOfPoints+= key;
                positionsGot.addAll(moveResult.get(key));
            }
        }

        moveResultMap.put(sumOfPoints, positionsGot);

        return moveResultMap;
    }

    private boolean canOpponentMove(Disk[][] board, Disk player){
        Disk opponent = (player == Disk.BLACK ? Disk.WHITE : Disk.BLACK);
        if(getPositionTable(board, opponent).size() == 0)
            return false;
        return true;
    }

    private boolean canPlayerMove(Disk[][] board, Disk player){
        if(getPositionTable(board, player).size() == 0)
            return false;
        return true;
    }

    public Disk[][] move(Disk[][] board, Disk player, Position pos) {
        int moveValue = 0;
        Map<Integer,List<Position>> result = getMoveValue(board, player, pos);
        moveValue += (Integer)result.keySet().toArray()[0];

        List<Position> gotPoints = new ArrayList<>(result.get(moveValue));
        if(moveValue == 0 || board[pos.getIndex1()][pos.getIndex2()] != null){

        }
        else {
            board[pos.getIndex1()][pos.getIndex2()] = player;
            board = addBoardMarks(board,player,gotPoints);
        }
        return board;
    }

    private Map<Integer, List<Position>> countDirection(Direction direction, Disk[][] board, Position p, Disk player){
        int sumOfPoints = 0;
        int stepX = direction.getX();
        int stepY = direction.getY();
        int steps = 0;
        List<Position> pointsList = new ArrayList<>();
        Map<Integer, List<Position>> movesMap = new HashMap<>();
        try {
            while (board[p.getIndex1() + stepX][p.getIndex2() + stepY] != null
                    && board[p.getIndex1() + stepX][p.getIndex2() + stepY] != player) {
                pointsList.add(new Position(p.getIndex1() + stepX, p.getIndex2() + stepY));
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
            else {
                pointsList.clear();
            }
            movesMap.put(sumOfPoints, pointsList);
        }
        catch(ArrayIndexOutOfBoundsException e){

        }
        return movesMap;
    }

    private Set<Position> getNeighbourFreeCells(Disk[][] board, Position p) {
        Set<Position> neighbours = new HashSet<>();

        if (p.getIndex2() > 0) {
            if (p.getIndex1() > 0)
                neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2() - 1));
            neighbours.add(new Position(p.getIndex1(), p.getIndex2() - 1));
            if (p.getIndex1() < board[0].length - 1)
                neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2() - 1));
        }
        if (p.getIndex1() > 0)
            neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2()));
        // current position
        if (p.getIndex1() < board[0].length - 1)
            neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2()));

        if (p.getIndex2() < board.length - 1) {
            if (p.getIndex1() > 0)
                neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2() + 1));
            neighbours.add(new Position(p.getIndex1(), p.getIndex2() + 1));
            if (p.getIndex1() < board[0].length - 1)
                neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2() + 1));
        }

        Set<Position> positionsToRemove = new HashSet<>();

        for (Position n : neighbours) {
            if (board[n.getIndex1()][n.getIndex2()] != null) {
                positionsToRemove.add(n);
            }
        }
        neighbours.removeAll(positionsToRemove);

        return neighbours;
    }

    public static void printBoard(Disk[][] board) {
        int flag = 0;
        for (int x = board[0].length + 1 - 1; x >= 0; x--) {
            if (x == 8)
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
        VERT_UP(0, 1), VERT_DOWN(0, -1), HOR_LEFT(-1, 0), HOR_RIGHT(1, 0), DIA_RIGHT_DOWN(-1, 1), DIA_LEFT_DOWN(-1,
                -1), DIA_RIGHT_UP(1, 1), DIA_LEFT_UP(1, -1);

        private int x;
        private int y;

        Direction(int x, int y) {

            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
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

    class TreeNode<T> implements Iterable<TreeNode<T>> {

        T data;
        TreeNode<T> parent;
        List<TreeNode<T>> children;

        public TreeNode(T data) {
            this.data = data;
            this.children = new LinkedList<TreeNode<T>>();
        }

        public TreeNode<T> addChild(T child) {
            TreeNode<T> childNode = new TreeNode<T>(child);
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }


        @Override
        public Iterator<TreeNode<T>> iterator() {
            Iterator<TreeNode<T>> iterator = new Iterator<TreeNode<T>>(
            ) {
                @Override
                public boolean hasNext() {
                    if(parent.children.size() == 0)
                        return false;
                    else
                        return true;
                }

                @Override
                public TreeNode<T> next() {
                    return parent.children.get(0);
                }
            };
            return iterator;
        }

        // other features ...

    }

}

interface BetterOtelloHelperInterface {
    /**
     * Typ wyliczeniowy reprezentujacy pionka do gry w Otello.
     *
     */
    enum Disk {
        BLACK, WHITE;
    }

    /**
     * Klasa reprezentuje pozycje w dwu-wymiarowej tablicy uzywanej do zapisu
     * stanu do gry. Uzycie przechowywanych w obiektach indeksow jest zgodne z
     * ich nazwa czyli para indeksow index1,index2 wskazuje na pozycje w tablicy
     * zapisana nastepujaco: tablica[index1][index2]
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
     *         mapa poprawnych kontynuacji gry. Kluczem mapy jest liczba
     *         zdobywanych na przeciwniku pionow. W przypadku braku mozliwosci
     *         ruchu - mapa o rozmiarze 0. <br>
     *         Kontynuacje gry sa zbiorem, w ktorym umieszczone sa listy
     *         alternatywnych posuniec prowadzacych do tej samej zdobyczy
     *         punktowej. Najczesciej listy beda zawierac tylko jedno polozenie
     *         piona, ale moze sie teraz zdarzyc, ze swym posunieciem gracz
     *         zablokuje mozliwosc odpowiedzi przeciwnika i wtedy moze zdobyc
     *         kolejne punkty stawiajac na planszy kolejnego wlasnego piona.
     *         Metoda analizuje wiec nie tylko jeden ruch gracza, ale musi
     *         sprawdzic, czy posuniecie nie prowadzi do zablokowania ruchu
     *         przeciwnika i mozliwosci zdobycia wiekszej liczby pionow. W
     *         takiej sytuacji lista staje sie wieloelementowa i zawiera
     *         polozenia kolejnych posuniec.
     */
    Map<Integer, Set<List<Position>>> analyze(Disk[][] board, Disk playerDisk);

}