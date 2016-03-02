import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class Generator implements GeneratorInterface {

	private List<NodeFabricInterface> terminals;
	private List<NodeFabricInterface> allNodes;
	private Queue<NodeInteface> queue = new ArrayDeque<>();
	private Random rnd = new Random();
	private int counter;
	private int complexity = 1;

	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}

	private void iterate(NodeInteface localRoot) {
		NodeInteface newNode;
		for (int i = 0; i < 2; i++) { // nie ma wiecej rozgalezien
			newNode = pickRandomNode(allNodes); 
			try {
				if ((counter + complexity) * rnd.nextDouble() < counter) {
					newNode = pickRandomNode(terminals);  // terminujemy sciezke
				}
				localRoot.addChild(newNode);
				queue.add(newNode);
				counter++;
				System.out.println("C = " + counter + " do "
						+ localRoot.toString() + " podpinam "
						+ newNode.toString());
			} catch (NodeInteface.TooManyChildsException e) {
				return;
			}
		}
	}

	private NodeInteface pickRandomNode(List<NodeFabricInterface> nodes) {
		return nodes.get(rnd.nextInt(nodes.size())).get();
	}

	@Override
	public NodeInteface generate(List<NodeFabricInterface> value,
			List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO) {

		terminals = value;
		allNodes = new ArrayList<NodeFabricInterface>();
		allNodes.addAll(value);
		allNodes.addAll(unaryO);
		allNodes.addAll(binaryO);

		NodeInteface root = pickRandomNode(binaryO);
		System.out.println( "Root to operator: " + root );
		queue.add(root);
		counter = 0;

		while (!queue.isEmpty()) {
			iterate(queue.poll());
		}

		return root;
	}

}
