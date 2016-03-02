import java.util.List;

@FunctionalInterface
public interface GeneratorInterface {
	NodeInteface generate(List<NodeFabricInterface> value,
			List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO);
}
