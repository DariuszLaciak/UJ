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

            }
        };
        Runnable pracaB1True_R = new Runnable() {
            @Override
            public void run() {

            }
        };
        Runnable pracaB2True_R = new Runnable() {
            @Override
            public void run() {

            }
        };
        Runnable pracaB1False_R = new Runnable() {
            @Override
            public void run() {

            }
        };
        Runnable pracaB2False_R = new Runnable() {
            @Override
            public void run() {

            }
        };
        Runnable pracaC_R = new Runnable() {
            @Override
            public void run() {

            }
        };
        BooleanSupplier warunekB_C = new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return false;
            }
        };
        BooleanSupplier warunekD_C = new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() {
                return false;
            }
        };
        AnyAlgorithm aa = new AnyAlgorithm();
        ExecutionList listaB12True = aa.createList();
        listaB12True.add(pracaB1True_R);
        listaB12True.add(pracaB2True_R);
        ExecutionList listaB12False = aa.createList();
        listaB12False.add(pracaB1False_R);
        listaB12False.add(pracaB2False_R);
        Fork forkB12 = aa.createFork();
        forkB12.set(warunekB_C);
        forkB12.setFalseBranch(listaB12False);
        forkB12.setTrueBranch(listaB12True);
        ExecutionList listaABC = aa.createList();
        listaABC.add(forkB12);
        listaABC.add(pracaA_R);
        listaABC.add(pracaC_R);
        Loop loop = aa.createLoop();
        loop.set(warunekD_C,true);
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

    }

    public class ExecutionList implements ExecutionListInterface{
        private List<Runnable> run;
        private ExecutionListInterface list;
        private LoopInterface loop;
        private ForkInterface fork;

        public ExecutionList(){
            this.run = new ArrayList<>();
        }

        @Override
        public void add(Runnable run) {
            this.run.add(run);
        }

        @Override
        public void add(ExecutionListInterface list) {
            this.list = list;
        }

        @Override
        public void add(LoopInterface loop) {
            this.loop = loop;
        }

        @Override
        public void add(ForkInterface fork) {
            this.fork = fork;
        }

        @Override
        public void start() {
            if(loop != null){
                loop.start();
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