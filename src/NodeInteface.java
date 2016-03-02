import java.util.List;

public interface NodeInteface {
	class TooManyChildsException extends Exception {

		private static final long serialVersionUID = -925787587460444443L;

	}

	/**
	 * Dodaje node nizszego poziomu
	 * 
	 * @param ni
	 *            node do dodania
	 * @throws TooManyChildsException
	 *             wyjatek zglaszany po przekroczeniu liczby mozliwych galezi.
	 */
	void addChild(NodeInteface ni) throws TooManyChildsException;

	/**
	 * Zwraca liste podlaczonych nodow nizszego poziomu
	 * 
	 * @return lista podlaczonych nodo
	 */
	List<NodeInteface> getChilds();

	/**
	 * Wykasowuje podlaczone nody
	 */
	void clear();

	/**
	 * Zwraca wyliczona wartosc dla tego nodu
	 * 
	 * @return wartosc nodu
	 */
	double getValue();

	/**
	 * Ustawia opis nodu
	 * 
	 * @param dsc
	 *            opis nodu
	 */
	void setDescription(String dsc);
	
	
	@Override
	public String toString();
}
