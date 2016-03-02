import java.util.List;

public interface ToDoListsExtInterface extends ToDoListsInterface {
	/**
	 * Laczy liste z lista.
	 * 
	 * @param listNameSrc
	 *            lista podlaczana (zrodlo danych)
	 * @param listNameDst
	 *            lista, do ktorej wykonywane jest polaczenie - cel polaczenia
	 * @throws ToDoListsInterface.DoesNotExistException
	 *             blad zglaszany w przypadku brak albo listy zrodlowej albo
	 *             docelowej
	 */
	void connectListToList(String listNameSrc, String listNameDst)
			throws ToDoListsInterface.DoesNotExistException;

	/**
	 * Zwraca liste nazw pozycji wchodzacych w sklad danej listy oraz list do
	 * niej podlaczonych. Rezultat pracy ma odzwierciedlac aktualny stan list
	 * podlaczonych.
	 */
	List<String> getItems(String listName)
			throws ToDoListsInterface.DoesNotExistException;

	/**
	 * Pozycja zostaje ustawiona w stan UNCHECKED.
	 * 
	 * @param itemID
	 *            identyfikator pozycji
	 * @throws ToDoListsInterface.DoesNotExistException
	 *             blad zglaszany w przypadku brak takiej pozycji w systemie
	 */
	void uncheckItem(Integer itemID)
			throws ToDoListsInterface.DoesNotExistException;

	/**
	 * Sprawdza czy wszystkie pozycje na wskazanej liscie i do niej podlaczonych
	 * sa w stanie CHECKED.
	 * 
	 * @param listName
	 *            nazwa sprawdzanej listy
	 * @return true - wszystkie pozycje na tej liscie i do niej podlaczonej sa w
	 *         stanie CHECKED
	 * @throws ToDoListsInterface.DoesNotExistException
	 *             brak listy
	 */
	boolean allChecked(String listName)
			throws ToDoListsInterface.DoesNotExistException;
}
