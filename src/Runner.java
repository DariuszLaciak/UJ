import java.util.function.BooleanSupplier;

/**
 * Created by Dariusz on 2016-02-02.
 */
public class Runner {
    private static final int TASK_NO = 9;

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
}

class TestZad8{
    public String textSQL = "TextSQL";
    public int integerSQL = 10;
    public boolean boolSQL = true;
    public float realSQL = 0.45F;
    private String test1 = "Test1";
    boolean test2 = false;
    public Runnable runTest = null;
    public String fal = "false";
}