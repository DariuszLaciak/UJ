public class ValueNode extends Node {

	private double value;
	private boolean readOnlyFlag = true;

	public ValueNode() {
		super(0);
	}

	public ValueNode(double value) {
		this();
		this.value = value;
	}

	public ValueNode(double value, boolean readOnlyFlag ) {
		this( value );
		this.readOnlyFlag = readOnlyFlag;
	}	
	
	public void setValue(double value) {
		if (!readOnlyFlag)
			this.value = value;
	}

	@Override
	public double getValue() {
		return value;
	}

}
