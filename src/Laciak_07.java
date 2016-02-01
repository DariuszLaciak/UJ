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

        @Override
        public void add(Runnable run) {

        }

        @Override
        public void add(ExecutionListInterface list) {

        }

        @Override
        public void add(LoopInterface loop) {

        }

        @Override
        public void add(ForkInterface fork) {

        }

        @Override
        public void start() {

        }
    }

    public class Loop implements LoopInterface{

        @Override
        public void set(ExecutionListInterface list) {

        }

        @Override
        public void set(BooleanSupplier continuationCondition, boolean preFlag) {

        }

        @Override
        public void start() {

        }
    }

    public class Fork implements ForkInterface{

        @Override
        public void set(BooleanSupplier forkCondition) {

        }

        @Override
        public void setTrueBranch(ExecutionListInterface list) {

        }

        @Override
        public void setFalseBranch(ExecutionListInterface list) {

        }

        @Override
        public void start() {

        }
    }
}