public interface ClassGeneratorInterface {
	/**
	 * Generuje klase o podanej nazwie i zwracajacej wartosci zgodne z
	 * przekazanym drzewem. Wygenerowana klasa jest automatycznie kompilowana i
	 * implementuje interfejs UniversalGetterInterface.
	 * 
	 * @param className
	 *            nazwa klasy do wygenerowania
	 * @param root
	 *            korzen drzewa opisujacego formule
	 */
	void generate(String className, NodeInteface root);
}
