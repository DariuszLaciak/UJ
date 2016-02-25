import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by Dariusz on 2016-02-02.
 */
public class Runner {
    private static final int TASK_NO = 10;

    public static void main(String[] args){
        switch(TASK_NO){
            case 7:
                zad7();
                break;
            case 8:
                zad8();
                break;
            case 9:
                zad9();
                break;
            case 10:
                zad10();
                break;
        }


    }

    private static void zad7(){
        Runnable pracaA_R = () -> {
            System.out.println("=====================");
            System.out.println("Praca A");
        };
        Runnable pracaB1True_R = () -> System.out.println("Praca B1 True");
        Runnable pracaB2True_R = () -> System.out.println("Praca B2 True");
        Runnable pracaB1False_R = () -> System.out.println("Praca B1 False");
        Runnable pracaB2False_R = () -> System.out.println("Praca B2 False");
        Runnable pracaC_R = () -> System.out.println("Praca C");
        final int[] iter = {0};
        BooleanSupplier warunekB_C = () -> {
            if(iter[0] % 2 == 0){
                return true;
            }
            else
                return false;
        };

        BooleanSupplier warunekD_C = () -> {
            iter[0]++;
            if( iter[0] < 5){
                return true;
            }
            else
                return false;
        };
        AnyAlgorithm.ExecutionList listaB12True = new AnyAlgorithm.ExecutionList();
        listaB12True.add(pracaB1True_R);
        listaB12True.add(pracaB2True_R);
        AnyAlgorithm.ExecutionList listaB12False = new AnyAlgorithm.ExecutionList();
        listaB12False.add(pracaB2False_R);
        listaB12False.add(pracaB1False_R);
        AnyAlgorithm.Fork forkB12 = new AnyAlgorithm.Fork();
        forkB12.set(warunekB_C);
        forkB12.setFalseBranch(listaB12False);
        forkB12.setTrueBranch(listaB12True);
        AnyAlgorithm.ExecutionList listaABC = new AnyAlgorithm.ExecutionList();
        listaABC.add(pracaA_R);
        listaABC.add(forkB12);
        listaABC.add(pracaC_R);
        AnyAlgorithm.Loop loop = new AnyAlgorithm.Loop();
        loop.set(warunekD_C,false);
        loop.set(listaABC);
        loop.start();
    }

    private static void zad8(){
        SQLiteHelper sql = new SQLiteHelper();
        TestZad8 test = new TestZad8();

        System.out.println("create: \n"+sql.createTable(test));
        System.out.println("insert: \n"+sql.insert(test));
    }

    private static void zad9(){
        ToDoListsExt tdle = new ToDoListsExt();
        try {
            tdle.createToDoList("nowa1");
            tdle.createToDoList("nowa2");
            tdle.addItemToList("przemiot","nowa1");
            tdle.addItemToList("przemiot2","nowa1");
            tdle.addItemToList("przemiot3","nowa2");
            tdle.connectListToList("nowa2", "nowa1");
            tdle.addItemToList("przemiot4","nowa1");
            tdle.checkItem(tdle.getUniqItemId("przemiot","nowa1"));
            tdle.checkItem(tdle.getUniqItemId("przemiot2","nowa1"));
            tdle.checkItem(tdle.getUniqItemId("przemiot4","nowa1"));
            tdle.checkItem(tdle.getUniqItemId("przemiot3","nowa2"));
            for(String item : tdle.getItems("nowa1")){
                System.out.println(item);
            }
            System.out.println("nowa1: "+tdle.allChecked("nowa1"));
            tdle.uncheckItem(tdle.getUniqItemId("przemiot3","nowa2"));
            System.out.println("nowa1: "+tdle.allChecked("nowa1"));
        } catch (ToDoListsInterface.AlreadyExistsException e) {
            System.err.println("Taka lista już istnieje");
        } catch (ToDoListsInterface.DoesNotExistException e) {
            System.err.println("Lista nie istnieje");
            e.printStackTrace();
        }
    }

    private static void zad10() {
        SQLiteHelperExt slhe = new SQLiteHelperExt();
        Connection con = null;
        Statement st;
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection("jdbc:sqlite:/tmp/sample.db");
            st = con.createStatement();
            st.executeUpdate("DROP TABLE IF EXISTS TestZad8;");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS TestZad8(fal TEXT, realSQL REAL, textSQL TEXT, integerSQL INTEGER, boolSQL INTEGER);");
            st.executeUpdate("INSERT INTO TestZad8 (fal, realSQL, textSQL, integerSQL, boolSQL) VALUES ('false', 0.45, 'TextSQL', 10, 1);");
            st.executeUpdate("INSERT INTO TestZad8 (fal, realSQL, textSQL, integerSQL, boolSQL) VALUES ('true', 1.45, '112TextSQL', 120, 0);");
            st.executeUpdate("INSERT INTO TestZad8 (fal, realSQL, textSQL, integerSQL, boolSQL) VALUES ('razdwa', 0, 'TextSQL1', 0, 1);");
            st.executeUpdate("INSERT INTO TestZad8 (fal, realSQL, textSQL, integerSQL, boolSQL) VALUES ('mniam', -44.45, 'costakiego', -10, 0);");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Object> klasa1 = slhe.select(con, "TestZad8");
        for(Object o : klasa1){
            TestZad8 test = (TestZad8)o;
            System.out.println("boolSQL: "+test.boolSQL+", integerSQL: "+test.integerSQL+", textSQL: "+test.textSQL+
            ", realSQL: "+test.realSQL+", fal: "+test.fal);
        }
    }
}

class TestZad8{
    public String textSQL = "TextSQL";
    public int integerSQL = 10;
    public Boolean boolSQL = true;
    public Float realSQL = 0.45F;
    private String test1 = "Test1";
    boolean test2 = false;
    public Runnable runTest = null;
    public String fal = "false";
}