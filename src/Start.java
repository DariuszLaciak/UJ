import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Start {

	private static DoubleValue xv = new DoubleValue(); // zmienna

	private static void show( NodeInteface root ) {
		for ( int i = 0; i < 10; i++ ) {
			xv.setValue( i / 10.0 );
			System.out.println( "Dla x = " + ( i / 10.0 ) + " wartosc = " + root.getValue() );
		}
	}
	
	public static void main(String[] args) {

		// terminale
		List<NodeFabricInterface> values = new ArrayList<NodeFabricInterface>() {
			{
				add(new ConstantValueNodeFabric(1));
				add(new ConstantValueNodeFabric(2));
				add(new ConstantValueNodeFabric(5));
				add(new VariableNodeFabric((ValueInterface) xv, "x"));
			}
		};

		// operatory jednoargumentowe
		List<NodeFabricInterface> unaryO = new ArrayList<NodeFabricInterface>() {
			{
				add(new UnaryNodeFabric((x) -> Math.sin(x), "Math.sin"));
				add(new UnaryNodeFabric((x) -> Math.cos(x), "Math.cos"));
				add(new UnaryNodeFabric((x) -> Math.exp(x), "Math.exp"));
			}
		};

		// operatory dwuargumentowe
		List<NodeFabricInterface> binaryO = new ArrayList<NodeFabricInterface>() {
			{
				add(new BinaryNodeFabric((x, y) -> (x + y), "+"));
				add(new BinaryNodeFabric((x, y) -> (x - y), "-"));
				add(new BinaryNodeFabric((x, y) -> (x * y), "*"));
				add(new BinaryNodeFabric((x, y) -> (x / y), "/"));
			}
		};

		Generator g = new Generator();
		g.setComplexity(3); // im wiecej tym formulki bardziej rozbudowane
		
		show( g.generate(values, unaryO, binaryO));
	}
}
