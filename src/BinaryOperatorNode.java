import java.util.function.DoubleBinaryOperator;

public class BinaryOperatorNode extends Node {
	private DoubleBinaryOperator op;

	public BinaryOperatorNode(DoubleBinaryOperator op) {
		super(2);
		this.op = op;
	}

	@Override
	public double getValue() {
		return op.applyAsDouble(list.get(0).getValue(), list.get(1).getValue());
	}

}
