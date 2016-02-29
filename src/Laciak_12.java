import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class ToDoListsExt implements ToDoListsExtInterface {

    List<ToDoList> lists = new ArrayList<>();
    List<ToDoList> viewedLists = new ArrayList<>();
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
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(list.getItems());
        allItems = getAllItems(listName,allItems);
        for(Item i : allItems){
            if(i.getName().equals(itemName)){
                throw new AlreadyExistsException();
            }
        }
        list.addItem(itemName);
        viewedLists.clear();
    }

    @Override
    public Set<String> getLists() {
        return lists.stream().map(ToDoList::getName).collect(Collectors.toSet());
    }

    @Override
    public List<String> getItems(String listName) throws DoesNotExistException {
        viewedLists.clear();
        ToDoList list = getListByName(listName);
        viewedLists.add(list);
        List<Item> items = list.getItems();
        List<Item> combinedItems = new ArrayList<>();
        combinedItems.addAll(items);
        combinedItems = getAllItems(listName,combinedItems);
        return combinedItems.stream().map(Item::getName).collect(Collectors.toList());
    }

    private List<Item> getAllItems(String listName, List<Item> list) throws DoesNotExistException {
        if(list == null){
            list = new ArrayList<>();
        }

        ToDoList thisList = getListByName(listName);
        viewedLists.add(thisList);
        for(ToDoList otherList : thisList.connectedLists){
            if(!viewedLists.contains(otherList)) {
                viewedLists.add(otherList);
                list.addAll(otherList.getItems());
                list = getAllItems(otherList.getName(),list);
            }
        }

        return list;
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
        private List<ToDoList> connectedLists;

        public ToDoList(String name){
            this.name = name;
            items = new ArrayList<>();
            connectedLists = new ArrayList<>();
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

        public void connectList(ToDoList list){
            connectedLists.add(list);
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

        public List<ToDoList> getConnectedLists(){
            return connectedLists;
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

        public void setUnChecked(){
            state = ItemState.UNCHECKED;
        }

        public ItemState getState(){
            return state;
        }

        public boolean isChecked(){
            return state == ItemState.CHECKED;
        }
    }


    @Override
    public void connectListToList(String listNameSrc, String listNameDst) throws DoesNotExistException {
        ToDoList src = getListByName(listNameSrc);
        ToDoList dst = getListByName(listNameDst);
        if(!src.equals(dst))
            dst.connectList(src);
    }
    @Override
    public void uncheckItem(Integer itemID) throws DoesNotExistException {
        Item i = getItemById(itemID);
        i.setUnChecked();
    }

    @Override
    public boolean allChecked(String listName) throws DoesNotExistException {
        List<Item> allItems = new ArrayList<>();
        viewedLists.clear();
        ToDoList list = getListByName(listName);
        allItems.addAll(list.getItems());
        allItems = getAllItems(listName,allItems);
        for(Item i : allItems){
            if(!i.isChecked()){
                return false;
            }
        }
        return true;
    }
}
