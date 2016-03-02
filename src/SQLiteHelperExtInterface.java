import java.sql.Connection;
import java.util.List;


public interface SQLiteHelperExtInterface extends SQLiteHelperInterface {
	/**
	 * Zwraca liste krotek (wierszy) znajdujacych sie w podanej z nazwy tablicy
	 * bazy danych, do ktorej polaczenie jest takze przekazywane.
	 * Metoda generuje obiekty typu zgodnego z nazwa tabeli (czyli
	 * jesli tabela ma nazwe XaYeZy, to dostepna bedzie klasa
	 * XaYeZy.class i to jej obiekty wypelnione danymi z tabeli maja byc zwrocone.
	 * @param connection polaczenie z baza danych SQLite
	 * @param tableName nazwa tabeli i zarazem klasy, ktorej obiekty beda po wpisaniu na liste 
	 * wynikiem pracy metody
	 * @return lista obiektow utworzonych na podstawie danych z tabeli
	 */
	List<Object> select( Connection connection, String tableName );
}
