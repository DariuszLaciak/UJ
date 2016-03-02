import java.util.function.DoubleUnaryOperator;


public class UnaryOperatorNode extends Node {

	private DoubleUnaryOperator op;
	
	public UnaryOperatorNode( DoubleUnaryOperator op ) {
		super( 1 );
		this.op = op;
	}
	
	@Override
	public double getValue() {
		return op.applyAsDouble( list.get(0).getValue() );
	}

}
