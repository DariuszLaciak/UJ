import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by Dariusz on 2016-02-01.
 */
class AnyAlgorithm implements AnyAlgorithmInterface {
    private ExecutionListInterface list;

    public static void main(String[] args){
        Runnable pracaA_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("=====================");
                System.out.println("Praca A");
            }
        };
        Runnable pracaB1True_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("Praca B1 True");
            }
        };
        Runnable pracaB2True_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("Praca B2 True");
            }
        };
        Runnable pracaB1False_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("Praca B1 False");
            }
        };
        Runnable pracaB2False_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("Praca B2 False");
            }
        };
        Runnable pracaC_R = new Runnable() {
            @Override
            public void run() {
                System.out.println("Praca C");
            }
        };
        final int[] iter = {0};
        BooleanSupplier warunekB_C = new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                if(iter[0] % 2 == 0){
                    return true;
                }
                else
                    return false;
            }
        };

        BooleanSupplier warunekD_C = new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                iter[0]++;
                if( iter[0] < 5){
                    return true;
                }
                else
                    return false;
            }
        };
        AnyAlgorithm aa = new AnyAlgorithm();
        ExecutionList listaB12True = aa.createList();
        listaB12True.add(pracaB1True_R);
        listaB12True.add(pracaB2True_R);
        ExecutionList listaB12False = aa.createList();
        listaB12False.add(pracaB2False_R);
        listaB12False.add(pracaB1False_R);
        Fork forkB12 = aa.createFork();
        forkB12.set(warunekB_C);
        forkB12.setFalseBranch(listaB12False);
        forkB12.setTrueBranch(listaB12True);
        ExecutionList listaABC = aa.createList();
        listaABC.add(pracaA_R);
        listaABC.add(forkB12);
        listaABC.add(pracaC_R);
        Loop loop = aa.createLoop();
        loop.set(warunekD_C,false);
        loop.set(listaABC);
        loop.start();

    }

    private ExecutionList createList(){
        return new ExecutionList();
    }

    private Fork createFork(){
        return new Fork();
    }

    private Loop createLoop(){
        return new Loop();
    }

    @Override
    public void setList(ExecutionListInterface list) {
        this.list = list;
    }

    @Override
    public void start() {
        if(list != null)
            list.start();
    }

    public class ExecutionList implements ExecutionListInterface{
        private List<Object> objects;

        public ExecutionList(){
            objects = new ArrayList<>();
        }

        @Override
        public void add(Runnable run) {
            objects.add(run);
        }

        @Override
        public void add(ExecutionListInterface list) {
            objects.add(list);
        }

        @Override
        public void add(LoopInterface loop) {
            objects.add(loop);
        }

        @Override
        public void add(ForkInterface fork) {
            objects.add(fork);
        }

        @Override
        public void start() {
            for(Object o : objects){
                if(o instanceof Runnable){
                    ((Runnable) o).run();
                }
                else {
                    ((ExecutableInterface) o).start();
                }
            }
        }
    }

    public class Loop implements LoopInterface{
        private ExecutionListInterface list;
        private BooleanSupplier continuationCondition;
        private boolean preFlag;

        @Override
        public void set(ExecutionListInterface list) {
            this.list = list;
        }

        @Override
        public void set(BooleanSupplier continuationCondition, boolean preFlag) {
            this.continuationCondition = continuationCondition;
            this.preFlag = preFlag;
        }

        @Override
        public void start() {
            if(preFlag){
                while(continuationCondition.getAsBoolean()){
                    list.start();
                }
            }
            else {
                do{
                    list.start();
                }
                while(continuationCondition.getAsBoolean());
            }
        }
    }

    public class Fork implements ForkInterface{
        private BooleanSupplier forkCondition;
        private ExecutionListInterface trueList;
        private ExecutionListInterface falseList;

        @Override
        public void set(BooleanSupplier forkCondition) {
            this.forkCondition = forkCondition;
        }

        @Override
        public void setTrueBranch(ExecutionListInterface list) {
            this.trueList = list;
        }

        @Override
        public void setFalseBranch(ExecutionListInterface list) {
            this.falseList = list;
        }

        @Override
        public void start() {
            if(forkCondition.getAsBoolean()){
                trueList.start();
            }
            else {
                falseList.start();
            }
        }
    }
}