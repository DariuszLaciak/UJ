import java.util.Observable;
import java.util.Observer;

public class BetterValueNode extends ValueNode implements Observer {
	private ValueInterface vi;

	public BetterValueNode() {
		super(0, false);
	}

	public void setValue(ValueInterface vi) {
		this.vi = vi;
	}

	@Override
	public double getValue() {

		if (vi != null)
			return vi.getValue();

		return super.getValue();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof DoubleValue) {
			setValue(((DoubleValue) o).getValue());
		}
	}
}
