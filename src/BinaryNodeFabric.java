import java.util.function.DoubleBinaryOperator;

public class BinaryNodeFabric implements NodeFabricInterface {

	private DoubleBinaryOperator operator;
	private String dsc;

	public BinaryNodeFabric(DoubleBinaryOperator operator, String dsc) {
		this.operator = operator;
		this.dsc = dsc;
	}

	@Override
	public Node get() {
		Node node = new BinaryOperatorNode(operator);
		node.setDescription( dsc );
		return node;
	}
}
