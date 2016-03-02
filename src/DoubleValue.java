import java.util.Observable;

public class DoubleValue extends Observable implements ValueInterface  {

	private double value;

	public void setValue(double value) {
		this.value = value;
		
		setChanged();
		notifyObservers();
	}
	
	@Override
	public double getValue() {
		return value;
	}
	
}
