import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Created by Dariusz on 2016-02-01.
 */
class AnyAlgorithm implements AnyAlgorithmInterface {
    private ExecutionListInterface list;


    public ExecutionList createList(){
        return new ExecutionList();
    }

    public Fork createFork(){
        return new Fork();
    }


    public Loop createLoop(){
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
                if(o != null){
                    if(o instanceof Runnable){
                        ((Runnable) o).run();
                    }
                    else {
                        ((ExecutableInterface) o).start();
                    }
                }

            }
        }
    }

    public class Loop implements LoopInterface{
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

    public class Fork implements ForkInterface{
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

interface AnyAlgorithmInterface extends ExecutableInterface {

    /**
     * Interfejs budowy listy polecen do wykonania. W sklad
     * listy moga wchodzic inne listy, petle, rozgalezienia i
     * obiekty Runnable. Nazwa ExecutionList zostala wprowadzona
     * w celu unikniecia problemow z interfejsem List.
     */
    interface ExecutionListInterface extends ExecutableInterface {
        /**
         * Dodanie jednej operacji do wykonania.
         * @param run obiekt zgodny z interfejsem Runnable
         */
        void add( Runnable run );

        /**
         * Dodaje liste operacji do wykonania.
         * @param list lista operacji do wykonania
         */
        void add( ExecutionListInterface list );

        /**
         * Dodaje petle.
         * @param loop petla do wykonania
         */
        void add( LoopInterface loop );

        /**
         * Dodaje operacje warunkowa.
         * @param fork operacja do wykonania
         */
        void add( ForkInterface fork );
    }

    /**
     * Interfejs budowy petli. Petla sklada sie z listy
     * operacji do wykonania i warunku kontynuacji.
     * Warunek sprawdzany jest przed lub po kazdorazowym wykonaniu
     * kompletu operacji nalezacych do listy.
     */
    interface LoopInterface extends ExecutableInterface {
        /**
         * Ustawia liste operacji do wykonania w petli.
         * @param list lista operacji do wykonania
         */
        void set( ExecutionListInterface list );

        /**
         * Ustawia warunek kontynuacji.
         * @param continuationCondition obiekt zgodny z interfejsem funkcyjnym
         * BooleanSupplier. Prawda logiczna oznacza, ze dzialanie petli ma byc
         * kontynuowane.
         * @param preFlag flaga okreslajaca czy warunek ma byc sprawdzany
         * przed wykonaniem listy operacji (true) czy po jej wykonaniu (false).
         */
        void set( BooleanSupplier continuationCondition, boolean preFlag );
    }

    /**
     * Interfejs budowy rozgalezienia. Elementami
     * skladowymi sa warunek wyboru sciezki wykonania oraz
     * listy operacji do wykonania w przypadku
     * wyboru danej sciezki. Warunek sprawdzany jest jako
     * pierwszy - od uzyskanego wyniku zalezy, ktora
     * z dwoch sciezek zostanie wybrana.
     */
    interface ForkInterface extends ExecutableInterface {
        /**
         * Ustawia warunek, ktory zostanie uzyty do podjecia decyzji,
         * ktora z galezi bedzie realizowana.
         * @param forkCondition warunek
         */
        void set( BooleanSupplier forkCondition );
        /**
         * Lista operacji do realizacji jesli warunek okaze sie prawda.
         * @param list lista operacji do wykonania dla prawdy
         */
        void setTrueBranch( ExecutionListInterface list );

        /**
         * Lista operacji do realizacji jesli warunek okaze sie falszem.
         * @param list lista operacji do wykonania w przypadku falszu
         */
        void setFalseBranch( ExecutionListInterface list );
    }

    /**
     * Ustawia liste polecen do wykonania.
     *
     * @param list - lista polecen do wykonania
     */
    void setList( ExecutionListInterface list );
}

interface ExecutableInterface {
    /**
     * Metoda zleca rozpoczecie wykonania.
     */
    void start();
}
