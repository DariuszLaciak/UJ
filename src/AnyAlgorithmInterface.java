import java.util.function.BooleanSupplier;

/**
 * Interfejs systemu pozwalajacego na stworzenie obiektowego opisu
 * algorytmu i jego wykonanie. Algorytm moze zawierac petle i rozgalezienia (warunki).
 * Podstawowa jednostka wykonujaca dzialanie jest obiekt zgodny z interfejsem
 * Runnable.
 * @author oramus
 *
 */
public interface AnyAlgorithmInterface extends ExecutableInterface {
	
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
		void add(Runnable run);
		
		/**
		 * Dodaje liste operacji do wykonania.
		 * @param list lista operacji do wykonania
		 */
		void add(ExecutionListInterface list);
		
		/**
		 * Dodaje petle.
		 * @param loop petla do wykonania
		 */
		void add(LoopInterface loop);
		
		/**
		 * Dodaje operacje warunkowa.
		 * @param fork operacja do wykonania
		 */
		void add(ForkInterface fork);
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
		void set(ExecutionListInterface list);
		
		/**
		 * Ustawia warunek kontynuacji. 
		 * @param continuationCondition obiekt zgodny z interfejsem funkcyjnym
		 * BooleanSupplier. Prawda logiczna oznacza, ze dzialanie petli ma byc
		 * kontynuowane.
		 * @param preFlag flaga okreslajaca czy warunek ma byc sprawdzany
		 * przed wykonaniem listy operacji (true) czy po jej wykonaniu (false). 
		 */
		void set(BooleanSupplier continuationCondition, boolean preFlag);
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
		void set(BooleanSupplier forkCondition);
		/**
		 * Lista operacji do realizacji jesli warunek okaze sie prawda.
		 * @param list lista operacji do wykonania dla prawdy
		 */
		void setTrueBranch(ExecutionListInterface list);
		
		/**
		 * Lista operacji do realizacji jesli warunek okaze sie falszem.
		 * @param list lista operacji do wykonania w przypadku falszu
		 */
		void setFalseBranch(ExecutionListInterface list);
	}
	
	/**
	 * Ustawia liste polecen do wykonania.
	 * 
	 * @param list - lista polecen do wykonania
	 */
	void setList(ExecutionListInterface list);
}
