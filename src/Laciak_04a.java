//import java.util.*;
//import java.util.Map.Entry;
//
//class BetterOtelloHelper implements BetterOtelloHelperInterface {
//    private Disk[][] firstBoard;
//    private Disk[][] globalTempBoard;
//    private Node<Position> tree;
//
////    public static void main(String[] args){
////        Disk[][] board = new Disk[8][8];
////        Disk player = Disk.BLACK;
//////        board[2] [2]= Disk.BLACK;
//////        board[2] [3]= Disk.BLACK;
//////        board[2] [4]= Disk.BLACK;
//////        board[2] [5]= Disk.BLACK;
//////        board[1] [6]= Disk.BLACK;
//////        board[3] [4]= Disk.BLACK;
//////        board[3] [3]= Disk.BLACK;
//////        board[5] [4]= Disk.BLACK;
//////        board[4] [3]= Disk.BLACK;
//////        board[1] [0]= Disk.BLACK;
//////        board[0] [0]= Disk.BLACK;
//////        board[0] [3]= Disk.BLACK;
//////        board[1] [2]= Disk.BLACK;
//////        board[3] [2]= Disk.BLACK;
//////        board[6] [5]= Disk.BLACK;
//////        board[7] [6]= Disk.BLACK;
//////
//////        board[2] [6]= Disk.WHITE;
//////
//////        board[2] [1]= Disk.WHITE;
//////
//////        board[3] [0]= Disk.WHITE;
////
////        board[3] [0]= Disk.BLACK;
////        board[3] [3]= Disk.BLACK;
////        board[3] [4]= Disk.BLACK;
////        board[4] [0]= Disk.BLACK;
////        board[4] [1]= Disk.BLACK;
////        board[4] [2]= Disk.BLACK;
////        board[4] [3]= Disk.BLACK;
////        board[4] [4]= Disk.BLACK;
////        board[5] [0]= Disk.BLACK;
////        board[5] [4]= Disk.BLACK;
////        board[6] [4]= Disk.BLACK;
////        board[7] [4]= Disk.BLACK;
////
////        board[2] [0]= Disk.WHITE;
////        board[6] [0]= Disk.WHITE;
////        board[1] [4]= Disk.WHITE;
////        board[2] [4]= Disk.WHITE;
////
////
////
////
////        BetterOtelloHelper boh = new BetterOtelloHelper();
//////        boh.analyze(board,player);
////        printBoard(board);
//////
//////        BetterOtelloHelper boh = new BetterOtelloHelper();
////        for(Entry<Integer, Set<List<Position>>> entry : boh.analyze(board,player).entrySet()){
////            System.out.println(entry.getKey() + ": ");
////            for(List<Position> moves : entry.getValue()){
////                System.out.print("{");
////                for(Position p : moves){
////                    System.out.print("["+ p.getIndex1() + ", " + p.getIndex2()+"] ");
////                }
////                System.out.print("} ");
////            }
////            System.out.println();
////        }
////
////    }
//
//    @Override
//    public Map<Integer, Set<List<Position>>> analyze(Disk[][] board, Disk playerDisk) {
//        firstBoard = makeCopy(board);
//        Map<Integer, Set<List<Position>>> result = new TreeMap<>(Collections.reverseOrder());
//        Map<Integer, Set<List<Position>>> resultsEnd = new TreeMap<>(Collections.reverseOrder());
//        Position[] firstIteration = analyze_old(board,playerDisk);
//        Map<Position,List<Position>> res;
//        tree = new Node<>();
//        for(Position p : firstIteration){
//            Node<Position> subtree = tree.addChild(p);
//            board = makeCopy(firstBoard);
//            res = new HashMap<>();
//            res.put(p,new ArrayList<>());
//            Disk opponent = (playerDisk == Disk.BLACK ? Disk.WHITE : Disk.BLACK);
//            int pointsBefore = getResult(board,opponent);
//            res = checkTreeResult(subtree, p,board,playerDisk,res);
//            Set<List<Position>> results = new HashSet<>();
//            for(Entry<Position,List<Position>> entry: res.entrySet()){
//                results.add(entry.getValue());
//            }
//
//            int pointsAfter = getResult(board,opponent);
//            int pointsGot = pointsBefore - pointsAfter;
//            for(List<Position> list : convertTreeToresult(tree)) {
//                convertListToMap(result, pointsGot, list);
//            }
//        }
//        for(Entry<Integer, Set<List<Position>>> entry : result.entrySet()){
//            for(List<Position> list : entry.getValue()){
//                Disk[][] tempBoard = makeCopy(firstBoard);
//                int actualPoints = calculatePoints(list,playerDisk,tempBoard);
//                convertListToMap(resultsEnd, actualPoints, list);
//            }
//        }
//        return resultsEnd;
//    }
//
//    private void convertListToMap(Map<Integer, Set<List<Position>>> result, int pointsGot, List<Position> list) {
//        if(result.containsKey(pointsGot)){
//            result.get(pointsGot).add(list);
//        }
//        else {
//            Set<List<Position>> set = new HashSet<>();
//            set.add(list);
//            result.put(pointsGot,set);
//        }
//    }
//
//    private boolean arePointsInTreeAlready( List<Position>  pointsToCheck, List<Position> list){
//        int expectedMatch = pointsToCheck.size();
//        int actual = 0;
//        boolean contains = false;
//
//        for(Position p1 : list){
//            for(Position p2 : pointsToCheck){
//                if(p1.getIndex1() == p2.getIndex1() && p1.getIndex2() == p2.getIndex2()){
//                    actual++;
//                }
//            }
//        }
//
//        if(actual == expectedMatch)
//            contains = true;
//
//        return contains;
//    }
//
//    private int calculatePoints(List<Position> moves, Disk player, Disk[][] board){
//        int points;
//        Disk opponent = (player == Disk.BLACK ? Disk.WHITE : Disk.BLACK);
//        Disk[][] tempBoard = makeCopy(firstBoard);
//        int pointsBefore = getResult(tempBoard,opponent);
//        for(Position p : moves){
//            tempBoard = move(tempBoard,player,p);
//
//        }
//        int pointsAfter = getResult(tempBoard,opponent);
//        points = pointsBefore - pointsAfter;
//
//        return points;
//    }
//
//    private Set<List<Position>> convertTreeToresult(Node<Position> tree){
//        Set<List<Position>> results = new HashSet<>();
//
//        for(int i = 0 ; i < tree.getChildren().size(); i++){
//            List<Position> list = new ArrayList<>();
//            if(tree.getChild(i).getChildren().size() > 0){
//                list.add(tree.getChild(i).getData());
//                results.addAll(getChildren(list,tree.getChild(i),null));
//            }
//            else {
//                list.add(tree.getChild(i).getData());
//                results.add(list);
//            }
//
//        }
//        return results;
//    }
//
//    private Set<List<Position>> getChildren(List<Position> root, Node<Position> subtree, Set<List<Position>>  res){
//        if(res == null)
//            res = new HashSet<>();
//        for(int i = 0 ; i < subtree.getChildren().size(); i++){
//            if(subtree.getChild(i).getChildren().size() == 0){
//                root.add(subtree.getChild(i).getData());
//                res.add(root);
//            }
//            else {
//                List<Position> copy = new ArrayList<>(root);
//                copy.add(subtree.getChild(i).getData());
//                res = getChildren(copy,subtree.getChild(i), res);
//            }
//
////            root.add(subtree.getChildren().get(i).getData());
////            res.add(root);
//
//        }
//        return res;
//    }
//
//    private Disk[][] makeCopy(Disk[][] board){
//        Disk[][] copy = new Disk[board.length][board[0].length];
//
//        for(int i=0; i<board.length; i++)
//            for(int j=0; j<board[i].length; j++)
//                copy[i][j]=board[i][j];
//
//        return copy;
//    }
//
//    public int getResult(Disk[][] board, Disk player) {
//        int result = 0;
//        for(Disk[] y : board){
//            for(Disk x : y){
//                if(x== player)
//                    result++;
//            }
//        }
//        return result;
//    }
//
//    private Map<Position,List<Position>> checkTreeResult(Node<Position> tree, Position p, Disk[][] board, Disk player,Map<Position,List<Position>> result){
//
//        Disk[][] tempBoard = move(board,player,p);
//        if(!canOpponentMove(tempBoard,player) ){
//            Position[] newMoves = analyze_old(tempBoard,player);
//            globalTempBoard = makeCopy(tempBoard);
//            for(Position move : newMoves){
//                tempBoard = makeCopy(globalTempBoard);
//                result = addMoveToList(result,p,move);
//                Node<Position> subtree = tree.addChild(move);
//                result = checkTreeResult(subtree,move, tempBoard,player,result);
//            }
//        }
//
//        return result;
//    }
//
//    private Map<Position,List<Position>> addMoveToList(Map<Position,List<Position>> map, Position prevMove, Position newMove){
//        List<Position> list = map.get(prevMove);
//        if(list != null)
//            map.get(prevMove).add(newMove);
//        else{
//            list = new ArrayList<>();
//            list.add(newMove);
//            map.put(prevMove,list);
//        }
//        return map;
//    }
//
//    public Position[] analyze_old(Disk[][] board, Disk playerDisk) {
//        List<Position> setPosition = getPositionTable(board, playerDisk);
//        Position[] positions = setPosition.toArray(new Position[setPosition.size()]);
//        if(positions.length > 1 && positions[0].getIndex1() == 1 && positions[0].getIndex2() == 0 ){
//            Position p = positions[0];
//            positions[0] = positions[1];
//            positions[1] = p;
//        }
//        return positions;
//    }
//
//    private Disk[][] addBoardMarks(Disk[][] board, Disk player, List<Position> points){
//        for(Position p : points){
//            board[p.getIndex1()][p.getIndex2()] = player;
//        }
//        return board;
//    }
//
//    private List<Position> getPositionTable(Disk[][] board, Disk player){
//        Map<Position,Integer> mapValues = new HashMap<>();
//        int val = 0;
//        for(Position p : getPotentialMoves(board)){
//            val = (Integer)getMoveValue(board,player,p).keySet().toArray()[0];
//            if(val > 0){
//                mapValues.put(p, val);
//            }
//        }
//
//        ValueComparator cmp = new ValueComparator(mapValues);
//        TreeMap<Position,Integer> sorted_map = new TreeMap<>(cmp);
//
//        sorted_map.putAll(mapValues);
//
//        return new ArrayList<>(sorted_map.keySet());
//    }
//
//    private List<Position> getPotentialMoves(Disk[][] board) {
//        List<Position> availMoves = new ArrayList<>();
//        List<Integer> intRepr = new ArrayList<>();
//        List<Position> playerCells = new ArrayList<>();
//
//        for (int x = board[0].length - 1; x >= 0; x--) {
//            for (int y = 0; y < board.length; y++) {
//                if (board[y][x] != null) {
//                    playerCells.add(new Position(y, x));
//                }
//            }
//        }
//
//        for (Position p : playerCells) {
//            for (Position p1 : getNeighbourFreeCells(board, p)) {
//                String value = new String("" + p1.getIndex1() + p1.getIndex2());
//                if (!intRepr.contains(Integer.parseInt(value))) {
//                    intRepr.add(Integer.parseInt(value));
//                    availMoves.add(p1);
//                }
//            }
//        }
//        return availMoves;
//    }
//
//    private Map<Integer,List<Position>> getMoveValue(Disk[][] board, Disk player, Position p){
//        int sumOfPoints = 0;
//        List<Position> positionsGot = new ArrayList<>();
//        Map<Integer,List<Position>> moveResultMap = new HashMap<>();
//
//        for(Direction d : Direction.values()){
//            Map<Integer,List<Position>> moveResult = countDirection(d,board,p,player);
//            if(moveResult.size() != 0){
//                int key = (Integer)moveResult.keySet().toArray()[0];
//                sumOfPoints+= key;
//                positionsGot.addAll(moveResult.get(key));
//            }
//        }
//
//        moveResultMap.put(sumOfPoints, positionsGot);
//
//        return moveResultMap;
//    }
//
//    private boolean canOpponentMove(Disk[][] board, Disk player){
//        Disk opponent = (player == Disk.BLACK ? Disk.WHITE : Disk.BLACK);
//        if(getPositionTable(board, opponent).size() == 0)
//            return false;
//        return true;
//    }
//
//    private boolean canPlayerMove(Disk[][] board, Disk player){
//        if(getPositionTable(board, player).size() == 0)
//            return false;
//        return true;
//    }
//
//    public Disk[][] move(Disk[][] board, Disk player, Position pos) {
//        int moveValue = 0;
//        Map<Integer,List<Position>> result = getMoveValue(board, player, pos);
//        moveValue += (Integer)result.keySet().toArray()[0];
//
//        List<Position> gotPoints = new ArrayList<>(result.get(moveValue));
//        if(moveValue == 0 || board[pos.getIndex1()][pos.getIndex2()] != null){
//
//        }
//        else {
//            board[pos.getIndex1()][pos.getIndex2()] = player;
//            board = addBoardMarks(board,player,gotPoints);
//        }
//        return board;
//    }
//
//    private Map<Integer, List<Position>> countDirection(Direction direction, Disk[][] board, Position p, Disk player){
//        int sumOfPoints = 0;
//        int stepX = direction.getX();
//        int stepY = direction.getY();
//        int steps = 0;
//        List<Position> pointsList = new ArrayList<>();
//        Map<Integer, List<Position>> movesMap = new HashMap<>();
//        try {
//            while (board[p.getIndex1() + stepX][p.getIndex2() + stepY] != null
//                    && board[p.getIndex1() + stepX][p.getIndex2() + stepY] != player) {
//                pointsList.add(new Position(p.getIndex1() + stepX, p.getIndex2() + stepY));
//                if(stepX > 0)
//                    stepX++;
//                if(stepY >0)
//                    stepY++;
//                if(stepY <0)
//                    stepY--;
//                if(stepX <0)
//                    stepX--;
//                steps++;
//            }
//            if (board[p.getIndex1() + stepX][p.getIndex2() + stepY] == player) {
//                sumOfPoints += steps;
//            }
//            else {
//                pointsList.clear();
//            }
//            movesMap.put(sumOfPoints, pointsList);
//        }
//        catch(ArrayIndexOutOfBoundsException e){
//
//        }
//        return movesMap;
//    }
//
//    private Set<Position> getNeighbourFreeCells(Disk[][] board, Position p) {
//        Set<Position> neighbours = new HashSet<>();
//
//        if (p.getIndex2() > 0) {
//            if (p.getIndex1() > 0)
//                neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2() - 1));
//            neighbours.add(new Position(p.getIndex1(), p.getIndex2() - 1));
//            if (p.getIndex1() < board[0].length - 1)
//                neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2() - 1));
//        }
//        if (p.getIndex1() > 0)
//            neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2()));
//        // current position
//        if (p.getIndex1() < board[0].length - 1)
//            neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2()));
//
//        if (p.getIndex2() < board.length - 1) {
//            if (p.getIndex1() > 0)
//                neighbours.add(new Position(p.getIndex1() - 1, p.getIndex2() + 1));
//            neighbours.add(new Position(p.getIndex1(), p.getIndex2() + 1));
//            if (p.getIndex1() < board[0].length - 1)
//                neighbours.add(new Position(p.getIndex1() + 1, p.getIndex2() + 1));
//        }
//
//        Set<Position> positionsToRemove = new HashSet<>();
//
//        for (Position n : neighbours) {
//            if (board[n.getIndex1()][n.getIndex2()] != null) {
//                positionsToRemove.add(n);
//            }
//        }
//        neighbours.removeAll(positionsToRemove);
//
//        return neighbours;
//    }
//
//    public static void printBoard(Disk[][] board) {
//        int flag = 0;
//        for (int x = board[0].length + 1 - 1; x >= 0; x--) {
//            if (x == 8)
//                System.out.print("[ ]");
//            else
//                System.out.print("[" + x + "]");
//            for (int y = 0; y < board.length; y++) {
//                if (flag == 0)
//                    System.out.print("[" + y + "]");
//                else {
//                    if (board[y][x] == null) {
//                        System.out.print("[ ]");
//                    } else if (board[y][x] == Disk.WHITE) {
//                        System.out.print("[O]");
//                    } else {
//                        System.out.print("[X]");
//                    }
//                }
//            }
//            flag = 1;
//            System.out.println();
//        }
//    }
//
//    public static void printTree(Node<Position> tree, boolean isRoot, int level){
//        if(isRoot) {
//            Position root = tree.getData();
//            if (root != null) {
//                System.out.println(p2string(root));
//            } else {
//                System.out.println("null");
//            }
//        }
//        if(!tree.getChildren().isEmpty()){
//            level++;
//
//            for(int i = 0 ; i < tree.getChildren().size() ; i++) {
//                for(int j = 0 ; j < level ; j++){
//                    System.out.print("\t");
//                }
//                System.out.println("|--" + p2string(tree.getChild(i).getData()));
//                printTree(tree.getChild(i),false, level);
//            }
//        }
//
//
//    }
//
//    public static String p2string(Position p){
//        String str = "";
//        str+="["+p.getIndex1()+", "+p.getIndex2()+"]";
//        return str;
//    }
//
//    enum Direction {
//        VERT_UP(0, 1), VERT_DOWN(0, -1), HOR_LEFT(-1, 0), HOR_RIGHT(1, 0), DIA_RIGHT_DOWN(-1, 1), DIA_LEFT_DOWN(-1,
//                -1), DIA_RIGHT_UP(1, 1), DIA_LEFT_UP(1, -1);
//
//        private int x;
//        private int y;
//
//        Direction(int x, int y) {
//
//            this.x = x;
//            this.y = y;
//        }
//
//        public int getX() {
//            return x;
//        }
//
//        public int getY() {
//            return y;
//        }
//
//    }
//
//    class ValueComparator implements Comparator<Position> {
//        Map<Position, Integer> base;
//
//        public ValueComparator(Map<Position, Integer> base) {
//            this.base = base;
//        }
//
//        public int compare(Position a, Position b) {
//            if (base.get(a) >= base.get(b)) {
//                return -1;
//            } else {
//                return 1;
//            }
//        }
//    }
//
//
//    class Node<T> {
//        private T data;
//        private Node<T> parent;
//        private List<Node<T>> children;
//
//        public Node() {
//            parent = null;
//            children = new ArrayList<Node<T>>();
//        }
//
//        public Node(Node<T> parent) {
//            this();
//            this.parent = parent;
//        }
//
//        public Node(Node<T> parent, T data) {
//            this(parent);
//            this.data = data;
//        }
//
//        public Node<T> getParent(){
//            return parent;
//        }
//
//        public void setParent(Node<T> parent) {
//            this.parent = parent;
//        }
//
//        public T getData() {
//            return data;
//        }
//
//        public void setData(T data) {
//            this.data = data;
//        }
//
//        public int getDegree() {
//            return children.size();
//        }
//
//        public boolean isLeaf(){
//            return children.isEmpty();
//        }
//
//        public Node<T> addChild(Node<T> child) {
//            child.setParent(this);
//            children.add(child);
//            return child;
//        }
//
//        public Node<T> addChild(T data) {
//            Node<T> child = new Node<T>(this, data);
//            children.add(child);
//            return child;
//        }
//
//        public Node<T> getChild(int i){
//            return children.get(i);
//        }
//
//        public Node<T> removeChild(int i) {
//            return children.remove(i);
//        }
//
//        public void removeChildren() {
//            children.clear();
//        }
//
//        public List<Node<T>> getChildren() {
//            return children;
//        }
//
//        public Node<T> getLeftMostChild() {
//            if (children.isEmpty()) return null;
//            return children.get(0);
//        }
//
//        public Node<T> getRightSibling() {
//            if (parent != null) {
//                List<Node<T>> parentsChildren = parent.getChildren();
//                int pos = parentsChildren.indexOf(this);
//                if (pos < (parentsChildren.size()-1))
//                    return parentsChildren.get(pos+1);
//            }
//            return null;
//        }
//
//        public String toString() {
//            return data.toString();
//        }
//    }
//}