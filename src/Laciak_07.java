import java.util.ArrayList;
import java.util.List;
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
        if(list != null)
            list.start();
    }

    public static class ExecutionList implements ExecutionListInterface {
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
            objects.stream().filter(o -> o != null).forEach(o -> {
                if (o instanceof Runnable) {
                    ((Runnable) o).run();
                } else {
                    ((ExecutableInterface) o).start();
                }
            });
        }
    }

    public static class Loop implements LoopInterface {
        private ExecutionListInterface list;
        private BooleanSupplier continuationCondition;
        private boolean preFlag;

        public Loop(){
            continuationCondition = () -> false;
            preFlag = false;
        }

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

    public static class Fork implements ForkInterface {
        private BooleanSupplier forkCondition;
        private ExecutionListInterface trueList;
        private ExecutionListInterface falseList;

        public Fork(){
            forkCondition = () -> false;
        }
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
