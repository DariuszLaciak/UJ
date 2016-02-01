import java.util.function.BooleanSupplier;

/**
 * Created by Dariusz on 2016-02-01.
 */
class AnyAlgorithm implements AnyAlgorithmInterface {
    private ExecutionListInterface list;

    @Override
    public void setList(ExecutionListInterface list) {
        this.list = list;
    }

    @Override
    public void start() {

    }

    public class ExecutionList implements ExecutionListInterface{
        private Runnable run;
        private ExecutionListInterface list;
        private LoopInterface loop;
        private ForkInterface fork;

        @Override
        public void add(Runnable run) {
            this.run = run;
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