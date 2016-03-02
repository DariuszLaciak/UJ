import java.util.function.DoubleUnaryOperator;


public class UnaryNodeFabric implements NodeFabricInterface {

	private DoubleUnaryOperator operator;
	private String dsc;
	
	public UnaryNodeFabric( DoubleUnaryOperator operator, String dsc ) {
		this.operator = operator;
		this.dsc = dsc;
	}
	
	@Override
	public Node get() {
		Node ni = new UnaryOperatorNode( operator );
		ni.setDescription( dsc );
		
		return ni;
	}

}
