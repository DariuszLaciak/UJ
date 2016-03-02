import java.util.List;
import java.util.Set;

/**
 * Interfejs prostego systemu list ToDo z mozliwoscia tworzenia list ToDo,
 * dodawania do nich pozycji i zmiany ich stanu.
 */
public interface ToDoListsInterface {
	
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
	public List<String> getItems( String listName ) throws DoesNotExistException;
	
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
