import java.util.*;

class ReversiBoardExt implements ReversiBoardInterfaceExt {
    private Disk[][] board;
    private Disk player;
    private boolean playerMoved = false;
    private Stack<Object[]> stackIn;
    private Stack<Object[]> stackOut;
    private boolean afterUndo = false;

    public ReversiBoardExt(){
        stackIn = new Stack<>();
        stackOut = new Stack<>();
    }


    @Override
    public void undo() throws IllegalOperationException {
        if(!this.stackIn.isEmpty()) {
            restoreState(true);
        }
        else
            throw new IllegalOperationException();
    }

    @Override
    public void redo() throws IllegalOperationException {
        if(!this.stackOut.isEmpty() && afterUndo) {
            restoreState(false);
        }
        else
            throw new IllegalOperationException();
    }

    private void restoreState(boolean in){
        Object[] stan;
        if(in){
            stan = this.stackIn.pop();
            afterUndo = true;
        }
        else {
            stan = this.stackOut.pop();
        }
        Disk[][] board = (Disk[][]) stan[0];
        Disk player = (Disk) stan[1];
        setGameState(makeCopy(board),player);
    }

    @Override
    public void setGameState(Disk[][] board, Disk nextPlayer) {
        this.board = board;
        this.player = nextPlayer;
        this.playerMoved = false;
    }

    @Override
    public boolean canWeContinueTheGame() {
        int availableMovesBLACK = getPositionTable(this.board,Disk.BLACK).size();
        int availableMovesWHITE = getPositionTable(this.board,Disk.WHITE).size();
        if(availableMovesBLACK == 0 && availableMovesWHITE ==0)
            return false;
        else
            return true;
    }

    @Override
    public Disk nextPlayer() throws CanNotContinue {
        if (this.playerMoved) {
            if (!canWeContinueTheGame()) {
                throw new CanNotContinue();
            } else {
                Disk opponent = (this.player == Disk.BLACK) ? Disk.WHITE : Disk.BLACK;
                int availableMoves = getPositionTable(this.board, opponent).size();
                if (availableMoves == 0) {
                    // nie zmieniaj gracza
                }
                else {
                    this.player = opponent;
                    this.playerMoved = false;
                }
            }
        }
        return this.player;
    }

    @Override
    public int move(Position pos) throws IllegalMove {
        int moveValue = 0;

        Map<Integer,List<Position>> result = getMoveValue(this.board, this.player, pos);
        moveValue += (Integer)result.keySet().toArray()[0];
        List<Position> gotPoints = new ArrayList<>(result.get(moveValue));
        if(moveValue == 0 || board[pos.getIndex1()][pos.getIndex2()] != null){
            this.playerMoved = false;
            throw new IllegalMove();
        }
        else {
            if(afterUndo){
                stackOut.clear();
                afterUndo = false;
            }
            Object[] prev = {makeCopy(this.board),this.player};
            this.stackIn.push(prev);
            this.board[pos.getIndex1()][pos.getIndex2()] = this.player;
            addBoardMarks(gotPoints);
            this.playerMoved = true;
            Object[] actual = {makeCopy(this.board),this.player};
            this.stackOut.add(0,actual);

            return moveValue;
        }
    }

    private Disk[][] makeCopy(Disk[][] board){
        Disk[][] copy = new Disk[board.length][board[0].length];

        for(int i=0; i<board.length; i++)
            for(int j=0; j<board[i].length; j++)
                copy[i][j]=board[i][j];

        return copy;
    }

    private void addBoardMarks(List<Position> points){
        for(Position p : points){
            this.board[p.getIndex1()][p.getIndex2()] = this.player;
        }
    }

    @Override
    public Disk[][] getBoard() {
        return board;
    }

    @Override
    public int getResult(Disk player) {
        int result = 0;
        for(Disk[] y : this.board){
            for(Disk x : y){
                if(x== player)
                    result++;
            }
        }
        return result;
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

    private Map<Integer,List<Position>> getMoveValue(Disk[][] board, Disk player, Position p){
        int sumOfPoints = 0;
        List<Position> positionsGot = new ArrayList<>();
        Map<Integer,List<Position>> moveResultMap = new HashMap<>();

        for(Direction d : Direction.values()){
            try {
                Map<Integer, List<Position>> moveResult = countDirection(d, board, p, player);
                int key = (Integer) moveResult.keySet().toArray()[0];
                sumOfPoints += key;
                positionsGot.addAll(moveResult.get(key));
            }
            catch(ArrayIndexOutOfBoundsException e){
            }

        }

        moveResultMap.put(sumOfPoints, positionsGot);

        return moveResultMap;
    }

    private Map<Integer, List<Position>> countDirection(Direction direction, Disk[][] board, Position p, Disk player) throws ArrayIndexOutOfBoundsException{
        int sumOfPoints = 0;
        int stepX = direction.getX();
        int stepY = direction.getY();
        int steps = 0;
        List<Position> pointsList = new ArrayList<>();
        Map<Integer, List<Position>> movesMap = new HashMap<>();
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

        return movesMap;
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


interface ReversiBoardInterface {
    /**
     * Typ wyliczeniowy reprezentujacy pionka do gry w Otello. Jest uzywany w
     * tym interfejsie rowniez do wskazania gracza.
     */
    enum Disk {
        BLACK, WHITE;
    }

    /**
     * Klasa reprezentuje pozycje w dwu-wymiarowej tablicy uzywanej do zapisu
     * stanu do gry. Uzycie przechowywanych w obiektach indeksow jest zgodne z
     * ich nazwa, czyli para indeksow index1,index2 wskazuje na pozycje w
     * tablicy zapisana nastepujaco: tablica[index1][index2]
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
     * Wyjatek informujacy o blednym posunieciu gracza.
     *
     */
    class IllegalMove extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = 9066912727591291840L;

    }

    /**
     * Wyjatek informujacy o tym, ze gra nie moze byc kontynuowana.
     */
    class CanNotContinue extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -6041202580217822041L;

    }

    /**
     * Metoda wykonywana przed kolejnymi. Ustawia aktualny stan gry i poprzez
     * kolor pionka wskazuje gracza, ktory ma wykonac ruch jako nastepny.
     * Podkreslam: gracz wskazywany jest za pomoca koloru jego pionkow.
     *
     * @param board
     *            aktualny stan planszy do gry
     * @param nextPlayer
     *            pionek gracza, ktory ma wykonac nastepny ruch
     */
    void setGameState(Disk[][] board, Disk nextPlayer);

    /**
     * Zwraca informacje, czy gra moze byc jeszcze kontynuowana.
     *
     * @return true - dalsza gra jest mozliwa, false - brak mozliwosci wykonania
     *         ruchu przez obu graczy
     */
    boolean canWeContinueTheGame();

    /**
     * Zwraca kolor pionka, ktorym ma zostac wykonany nastepny ruch.
     *
     * @return - zwrocony kolor pionka wskazuje gracza, ktory ma wykonac kolejny
     *         ruch.
     * @throws CanNotContinue
     *             wyjatek, ktory pojawia sie gry zaden z graczy nie moze
     *             kontynuowac gry
     */
    Disk nextPlayer() throws CanNotContinue;

    /**
     * Metoda pozwala wskazac nastepny ruch. Ruch wykonywany jest przez tego
     * gracza, ktory zostal wskazany przez wykonanie nextPlayer.
     *
     * @param pos
     *            polozenia na planszy, w ktorym ma zostac polozony pionek
     * @return liczba zdobytych pionkow przeciwnika
     * @throws IllegalMove
     *             wyjatek weryfikowalny informujacy o bledzie gracza. Ruch nie
     *             jest akceptowany. Gracz musi zaproponowac inne polozenie
     *             zgodne z regulami gry i stanem planszy.
     */
    int move(Position pos) throws IllegalMove;

    /**
     * Metoda zwraca aktualny stan rozgrywki.
     *
     * @return - dwuwymiarowa tablica 8x8 z zapisanym aktualnym stanem gry
     */
    Disk[][] getBoard();

    /**
     * Metoda zwraca aktualna liczbe pionkow nalezacych do gracza. Gracz
     * wskazywany jest poprzez podanie koloru pionkow, ktorymi sie posluguje.
     *
     * @param player
     *            kolor pionkow uzywanych przez gracza, ktory chce poznac swoj
     *            stan posiadania
     * @return liczba pionkow na planszy, ktore sa w kolorze pionkow gracza
     */
    int getResult(Disk player);

}

interface ReversiBoardInterfaceExt extends ReversiBoardInterface {

    class IllegalOperationException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = 5411670003525209093L;

    }

    /**
     * Realizuje operacje UNDO przywracajac stan planszy do wczesniejszego.
     * Gra cofa sie o <b>jeden, poprawny ruch jednego gracza</b>.
     *
     * @throws IllegalOperationException wyjatek zwracany gdy nie mozna sie
     * juz cofnac w historii gry - czyli gra dotarla do stanu, ktory zostal
     * ustawiony za pomoca wywolania metody setGameState
     */
    void undo() throws IllegalOperationException;

    /**
     * Realizuje operacje REDO przywracajac kolejny stan planszy. Stan
     * zmienia sie o jedno, poprawne posuniecie jednego gracza.
     *
     * @throws IllegalOperationException wyjatek zwracany gdy nie mozna sie
     * juz isc dalej w historii gry - czyli gra dotarla do ostatniego znanego stanu
     * rozgrywki.
     */
    void redo() throws IllegalOperationException;
}