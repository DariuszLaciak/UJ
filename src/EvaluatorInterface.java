
public interface EvaluatorInterface {
	/**
	 * Metoda zwraca blad oszacowania danych doswiadczalnych
	 * przez wzor przekazany przez NodeInterface.
	 * @param root korzen drzewa formuly
	 * @return blad oszacowania. Uwaga: wynik moze
	 * zawierac NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY
	 */
	double getError( NodeInteface root );
}
