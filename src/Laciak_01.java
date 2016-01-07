import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class FillBase {
	/**
	 * Metoda wypelnia zadanym kolorem (color) obrazek (image). Obrazek reprezentowany jest
	 * przez dwowymiarowa tablice. Wypelnienie zaczyna sie 
	 * w pozycji image[ firstIndex ][ secondIndex ] i obejmuje caly obszar obrazka, ktory
	 * jest osiagalny przemieszczajac sie z pozycji startowej do sasiednich pikseli
	 * wg. ponizszych zasad:
	 * <ul>
	 * <li>Kazdy piksel wewnatrz obrazka sasiaduje z 8-mioma sasiednimi pikselami. Liczba sasiadow
	 * dla pikseli brzegowych jest odpowienio mniejsza.</li>
	 * <li>Z aktualnie analizowanej pozycji wolno przemieszczac sie wylacznie do tych sasiednich lokalizacji, 
	 * ktore oznaczone sa w tablicy sasiadow jako <code>true</code>. <code>false</code> oznacza brak
	 * mozliwosci ruchu w kierunku danego sasiada.</li>
	 * 
	 * <li>Tablica sasiadow (neighbours) jest zawsze rozmiaru 3x3. Informacja o tym czy
	 * z danej pozycji mozna przesunac sie o w danym kierunku znajduje sie na stosownej pozycji
	 * tej tablicy. I tak jesli analizujemy mozliwosc ruchu z polozenia [x][y], to obowiazuje nastepujace
	 * powiazenie polozen w tablicy sasiadow i mozliwosci przesuniec.
	 * <table style="border:1px solid blue" border="1">
	 * <tr><td></td><td>neighbour[0][]</td><td>neighbour[1][]</td><td>neighbour[2][]</td></tr>
	 * <tr><td>neighbour[][0]</td><td>[ x - 1 ][ y + 1 ]</td><td>[ x ][ y + 1 ]</td><td>[ x + 1 ][ y + 1 ]</td></tr> 
	 * <tr><td>neighbour[][1]</td><td>[ x - 1 ][ y ]</td><td>[ x ][ y  ]</td><td>[ x + 1 ][ y ]</td></tr> 
	 * <tr><td>neighbour[][2]</td><td>[ x - 1 ][ y - 1 ]</td><td>[ x ][ y - 1 ]</td><td>[ x + 1 ][ y - 1 ]</td></tr> 
	 * </table>
	 * </li>
	 * 
	 * <li>Nie wolno nadpisywac kolorem (color) i tym samym przekraczac tych elementow obrazka, ktore
	 * maja kolor wymieniony w tablicy <code>colors</code>. Uwaga: color moze znajdowac sie w tablicy colors.</li>   
	 * </ul>
	 * 
	 * @param image obrazek do wypelnienia kolorem
	 * @param neighbours informacja, ktore piksele sa traktowane jako sasiednie
	 * @param colors tablica chronionych kolorow
	 * @param color kolor, ktorym wypelniamy obrazek
	 * @param firstIndex pierwszy indeks pozycji startowej
	 * @param secondIndex drugi indeks pozycji startwej
	 */
	public void fill( int[][] image, boolean [][] neighbours, int[] colors, int color, int firstIndex, int secondIndex ) {
		// tak, to nic nie robi...
	}
}

class FillBaseImplementation extends FillBase {
	private Set<int[]> neighboursColored;
	private boolean[][] alreadyVisited;

	public FillBaseImplementation() {
		neighboursColored = new HashSet<>();
	}

	public void fill(int[][] image, boolean[][] neighbours, int[] colors, int color, int firstIndex, int secondIndex) {
		if (alreadyVisited == null) {
			alreadyVisited = new boolean[image.length][image[0].length];
		}
		if (isPointOk(image, colors, color, firstIndex, secondIndex)) {
			checkNeighbours(image, neighbours, colors, color, firstIndex, secondIndex);
			for (int[] coords : neighboursColored) {
				fill(image, neighbours, colors, color, coords[0], coords[1]);
			}
		}
	}

	private boolean isPointOk(int[][] image, int[] colors, int color, int firstIndex, int secondIndex) {
		for (int c : colors) {
			if (image[firstIndex][secondIndex] == c && color != c) {
				return false;
			}
		}
		return true;
	}

	private void checkNeighbours(int[][] image, boolean[][] neighbours, int[] colors, int color, int firstIndex,
			int secondIndex) {
		neighboursColored = new HashSet<>();
		Arrays.sort(colors);
		if (image[firstIndex][secondIndex] != color) {
			neighboursColored.add(new int[] { firstIndex, secondIndex });
			alreadyVisited[firstIndex][secondIndex] = true;
		}
		int x = firstIndex - 1;
		int y = secondIndex + 1;

		for (boolean[] n : neighbours) {
			for (boolean nn : n) {
				try {
					if (nn == true) {
						if (Arrays.binarySearch(colors, image[x][y]) == -1 && !alreadyVisited[x][y]) {
							int[] coords = { x, y };
							neighboursColored.add(coords);
							alreadyVisited[x][y] = true;
						}
					}
				} catch (ArrayIndexOutOfBoundsException ex) {
				}
				x++;
			}
			x = firstIndex - 1;
			y--;
		}
		colorNeighbours(image, color);
	}

	private void colorNeighbours(int[][] image, int color) {
		for (int[] coords : neighboursColored) {
			image[coords[0]][coords[1]] = color;
		}
	}
}
