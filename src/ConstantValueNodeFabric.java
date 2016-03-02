public class ConstantValueNodeFabric implements NodeFabricInterface {

	private NodeInteface node;

	public ConstantValueNodeFabric(double value) {
		node = new ValueNode(value);
		node.setDescription((new Double(value)).toString());
	}

	@Override
	public NodeInteface get() {
		return node;
	}

}
