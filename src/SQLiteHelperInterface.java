
public interface SQLiteHelperInterface {
	/**
	 * Metoda uzywajac refleksji dokonuje inspekcji przekazanego obiektu i generuje
	 * ciag znakow tworzacy w bazie danych typy SQLite tabele, ktora pozwoli
	 * zachowac dane z tego obiektu.
	 * Metoda wyszukuje wylacznie publiczne pola i bada ich typ. Zwracajac
	 * rezultat dokonuje nastepujacego powiazania typow Java z typami dostepnymi w SQLite
	 * <br>
	 * <ul>
	 * <li>int,long,Integer,Long -&gt; INTEGER
	 * <li>float,double,Float,Double -&gt; REAL
	 * <li>String -&gt; TEXT
	 * <li>boolean/Boolean -&gt; INTEGER
	 * </ul>
	 * 
	 * Inne typy i niepubliczne pola sa <b>ignorowane</b>.
	 * Nazwa tworzonej kolumny to nazwa pola klasy. Nazwa tworzonej tablicy
	 * to nazwa klasy, ktorej obiekt przekazano.
	 * Zwracany ciag znakow zaczyna sie zawsze od: <tt>CREATE TABLE IF NOT EXISTS</tt> 
	 * 
	 * @param o obiekt do analizy
	 * @return ciag znakow z przepisem na tabele do przechowania danych z obiektu
	 * 
	 * @see <a href="https://www.sqlite.org/lang_createtable.html">Create table</a>
	 * @see <a href="https://www.sqlite.org/datatype3.html">Datatypes In SQLite Version 3</a>
	 * 
	 */
	String createTable( Object o );
	
	/**
	 * Metoda zwraca ciag znakow reprezentujacych operacje INSERT, ktora ma
	 * spowodowac, ze w tabeli o nazwie klasy obiektu pojawia sie wartosci publicznych
	 * pol zapisanych o obiekcie. Zaklada sie, ze stosowna tabela w bazie juz istnieje.
	 * Zwracany ciag znakow zaczyna sie zawsze od: <tt>INSERT INTO</tt>
	 * Typ logiczny zapisywany jest do kolumny typu calkowitoliczbowego, nalezy dokonac
	 * wiec nastepujacej konwersji <tt>true</tt> zapisywane jest jako 1,
	 * <tt>false</tt> zapisywane jest jako 0.
	 * @param o obiekt do analizy
	 * @return ciag znakow z przepisem na umieszczenie w tabeli danych z przekazanego obiektu
	 * 
	 * @see <a href="http://www.tutorialspoint.com/sqlite/sqlite_insert_query.htm">Insert</a>
	 * @see <a href="https://www.sqlite.org/lang_insert.html">Insert z dokumentacji SQLite</a>
	 */
	String insert( Object o );
}
