import java.util.ArrayList;
import java.util.List;


abstract public class Node implements NodeInteface {
	private String dsc;
	protected List< NodeInteface > list;
	private int requiredChilds;
	
	public void setDescription( String dsc ) {
		this.dsc = dsc;
	}
	
	public String toString() {
		return dsc;
	}
	
	public Node( int requiredChilds ) {
		if ( requiredChilds > 0 )
			list = new ArrayList<NodeInteface>( requiredChilds );
		this.requiredChilds = requiredChilds;
	}

	@Override
	public void addChild(NodeInteface ni) throws TooManyChildsException {
		if ( (list==null ) || (list.size() == requiredChilds ) ) throw new TooManyChildsException();
		list.add( ni );
	}

	@Override
	public List<NodeInteface> getChilds() {
		return list;
	}

	@Override
	public void clear() {
		list.clear();
	}

}
