import java.util.*;
import java.util.stream.Collectors;

class ToDoLists implements ToDoListsInterface{
    List<ToDoList> lists = new ArrayList<>();
    int prevId = 0;

    @Override
    public void createToDoList(String name) throws AlreadyExistsException {
        for(ToDoList list : lists){
            if(Objects.equals(list.getName(), name)){
                throw new AlreadyExistsException();
            }
        }
        ToDoList newList = new ToDoList(name);
        lists.add(newList);
    }

    @Override
    public void addItemToList(String itemName, String listName) throws AlreadyExistsException, DoesNotExistException {
        ToDoList list = getListByName(listName);
        list.addItem(itemName);
    }

    @Override
    public Set<String> getLists() {
        return lists.stream().map(ToDoList::getName).collect(Collectors.toSet());
    }

    @Override
    public List<String> getItems(String listName) throws DoesNotExistException {
        ToDoList list = getListByName(listName);
        List<Item> items = list.getItems();
        return items.stream().map(Item::getName).collect(Collectors.toList());
    }

    @Override
    public Integer getUniqItemId(String itemName, String listName) throws DoesNotExistException {
        ToDoList list = getListByName(listName);
        Item item = list.getItem(itemName);
        return item.getId();
    }

    @Override
    public ItemState getItemState(Integer itemID) throws DoesNotExistException {
        Item i = getItemById(itemID);
        return i.getState();
    }

    @Override
    public void checkItem(Integer itemID) throws DoesNotExistException {
        Item i = getItemById(itemID);
        i.setChecked();
    }

    public Item getItemById(int itemID) throws DoesNotExistException{
        for(ToDoList list : lists){
            for(Item i : list.getItems()){
                if(i.getId() == itemID){
                    return i;
                }
            }
        }
        throw new DoesNotExistException();
    }

    public int generateNextId(){
        return ++prevId;
    }

    public ToDoList getListByName(String name) throws DoesNotExistException{
        for(ToDoList list : lists){
            if(Objects.equals(list.getName(), name)){
                return list;
            }
        }
        throw new DoesNotExistException();
    }

    class ToDoList{
        private String name;
        private List<Item> items;

        public ToDoList(String name){
            this.name = name;
            items = new ArrayList<>();
        }

        public void addItem(String item) throws AlreadyExistsException{
            for(Item i : items){
                if(Objects.equals(i.getName(), item)){
                    throw new AlreadyExistsException();
                }
            }
            Item newItem = new Item(item,generateNextId());
            items.add(newItem);
        }

        public List<Item> getItems(){
            return items;
        }

        public String getName(){
            return name;
        }

        public Item getItem(String name) throws DoesNotExistException{
            for(Item i : items){
                if(Objects.equals(i.getName(), name)){
                    return i;
                }
            }
            throw new DoesNotExistException();
        }
    }

    class Item{
        private String name;
        private int id;
        private ItemState state;

        public Item(String name, int id){
            this.name = name;
            this.id = id;
            state = ItemState.UNCHECKED;
        }

        public String getName(){
            return name;
        }

        public int getId(){
            return id;
        }

        public void setChecked(){
            state = ItemState.CHECKED;
        }

        public ItemState getState(){
            return state;
        }
    }
}

interface ToDoListsInterface {

    class AlreadyExistsException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -2707662303264145043L;
    }

    class DoesNotExistException extends Exception {

        /**
         *
         */
        private static final long serialVersionUID = -48702234591940372L;
    }

    enum ItemState {
        CHECKED,UNCHECKED;
    }

    /**
     * Tworzy liste ToDo o wskazanej nazwie. Nazwa listy musi byc unikalna.
     *
     * @param name nazwa listy
     * @throws AlreadyExistsException wyjatek wskazujacy na powtorzenie nazwy listy
     */
    public void createToDoList( String name ) throws AlreadyExistsException;

    /**
     * Dodaje pozycje do listy o wskazanej nazwie. Nazwa pozycji musi byc unikalna na poziomie
     * listy. W calym systemie pozycje o tych samych nazwach moga sie powtarzac.
     * @param itemName nazwa dodawanej pozycji
     * @param listName lista, do ktorej pozycja jest dodawana
     * @throws AlreadyExistsException wyjatek wskazujacy na ponowne uzycie nazwy pozycji w tej samej liscie
     * @throws DoesNotExistException wyjatek wskazujacy na brak listy w wskazanej nazwie
     */
    public void addItemToList( String itemName, String listName ) throws AlreadyExistsException, DoesNotExistException;

    /**
     * Metoda zwraca zbior nazw utworzonych list. Zbior moze byc pusty, gdy zadnej
     * listy nie utworzono.
     *
     * @return zbior nazw utworzonych list
     */
    public Set<String> getLists();

    /**
     * Metoda zwraca liste nazw pozycji wchodzacych w sklad danej listy.
     * @param listName nazwa listy
     * @return pozycje chodzace w sklad listy (lista moze byc pusta, jesli brak pozycji)
     * @throws DoesNotExistException wyjatek informujacy o braku listy
     */
    public List<String> getItems(String listName ) throws DoesNotExistException;

    /**
     * Metoda zwraca unikalny numer identyfikujacy dana pozycje w systemie.
     * Numer musi byc unikalny w calym systemie list ToDo.
     * @param itemName nazwa pozycji
     * @param listName nazwa listy
     * @return globalnie unilany numer identyfikacyjny
     * @throws DoesNotExistException brak listy lub pozycji
     */
    public Integer getUniqItemId( String itemName, String listName ) throws DoesNotExistException;

    /**
     * Zwraca stan pozycji. Utworzone pozycje sa zawsze w stanie ItemState.UNCHECKED.
     * @param itemID identyfikator pozycji w systemie
     * @return stan pozycji
     * @throws DoesNotExistException brak pozycji w systemie
     */
    public ItemState getItemState( Integer itemID ) throws DoesNotExistException;

    /**
     * Ustawia pozycje na ItemState.CHECKED
     * @param itemID identyfikator pozycji w systemie
     * @throws DoesNotExistException brak pozycji w systemie
     */
    public void checkItem( Integer itemID ) throws DoesNotExistException;
}