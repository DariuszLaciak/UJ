import java.util.Observable;


public class VariableNodeFabric implements NodeFabricInterface {
	
	private BetterValueNode node;
	private String dsc;

	{
		node = new BetterValueNode();		
	}
	
	public VariableNodeFabric( Observable value, String dsc ) {
		node.setDescription( dsc );
		value.addObserver( node );
	}
	
	public VariableNodeFabric( ValueInterface value, String dsc ) {
		node.setDescription( dsc );
		node.setValue( value.getValue() );
	}	
	
	@Override
	public NodeInteface get() {
		return node;
	}
	
	
}
