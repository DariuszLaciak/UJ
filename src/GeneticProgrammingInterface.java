import java.util.List;
import java.util.function.BooleanSupplier;

public interface GeneticProgrammingInterface {

	/**
	 * Metoda pozwala na ustwienie obiektu, ktory bedzie dostaczal informacji o
	 * bledzie oszacowania pewnych danych przez formule.
	 * 
	 * @param evi
	 *            referencja do obiektu ewaluatora formul.
	 */
	void setEvaluator(EvaluatorInterface evi);

	/**
	 * Metoda zwraca posortowana liste formul. Lista jest wolna od formul
	 * powodujacych bledy obliczeniowe (zwracajacych w wyniku pracy Evaluatora
	 * np. NaN, NEGATIVE_INFINITY, POSITIVE_INFINITY), moze wiec byc mniejsza od
	 * wejsciowej listy formul. Lista posortowana jest rosnaco wg. miary bledu
	 * jaka zwraca ewaluator. Czyli na poczatku listy sa rozwiazania aktalnie
	 * najlepsze, dalej coraz to gorsze.
	 * 
	 * @param roots
	 *            lista formul do analizy
	 * @return posortowana lista formul wolnych od bledow obliczeniowych
	 */
	List<NodeInteface> evalute(List<NodeInteface> roots);

	/**
	 * Operator mutacji. Kazdy z wezlow drzewa udostepnianego poprzez referencje
	 * do jego korzenia podlega mutacji z pewnym prawdopodobienstwem. W
	 * jednym drzewie moze dojsc do wielu mutacji lub mutacja nie bedzie
	 * wykonana - o wszystkim decyduje wynik BooleanSupplier-a.
	 * Drzewo wynikowe musi byc poprawne, tzn. nie mozna wymienic np.
	 * operatora dodawania (op. dwuargumentowy) + na np. Math.sin (op. jednoargumentowy).
	 * 
	 * @param root korzen drzewa
	 * @param mutate referencja do BooleanSupplier-a, ktory odpowiada true, gdy dany
	 * wezel ma zostac zmutowany
	 * @param value lista fabryk wartosci
	 * @param unaryO lista fabryk operatorow jednoargumentowych
	 * @param binaryO lista fabryk operatorow dwuargumentowych
	 * @return korzen wynikowego drzewa
	 */
	NodeInteface mutate(NodeInteface root, BooleanSupplier mutate,
			List<NodeFabricInterface> value, List<NodeFabricInterface> unaryO,
			List<NodeFabricInterface> binaryO);

	public class PairOfNodes {
		public NodeInteface n1;
		public NodeInteface n2;
	}

	/**
	 * Operator krzyzowania.
	 * Metoda zwraca pare formul bedacych wynikiem krzyzowania
	 * pary formul dostarczonych. Krzyzowanie realizowane jest losowo i polega
	 * na wymianie losowo wybranych galezi drzew czyli np.
	 * <pre>
	 * ( x + 7 ) - ( ( 5 + 2 ) * ( x * sin( x ) ) )
	 * ( ( x - 8 ) * ( x + x ) ) - ( 2 + x )
	 * moze dac np.:
	 *  ( x + 7 ) - ( ( 5 + 2 ) * ( 2 + x ) )
	 *  ( ( x - 8 ) * ( x + x ) ) - ( x * sin( x ) )
	 *  </pre>
	 *  W wynikowych drzewach znajduja sie obiekty, ktore sa w drzewach
	 *  do metody dostarczonych - metoda sumarycznej liczby wezlow w drzewach
	 *  nie zmienia.
	 *  
	 * @param root1 korzen pierwszej formuly do krzyzowania
	 * @param root2 korzen drugiej formuly fo krzyzowania
	 * @return para formul po operacji krzyzowania
	 */
	PairOfNodes crossover(NodeInteface root1, NodeInteface root2);

}
