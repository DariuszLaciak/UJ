import java.util.List;
import java.util.function.BooleanSupplier;

class GeneticProgramming implements GeneticProgrammingInterface {
    @Override
    public void setEvaluator(EvaluatorInterface evi) {

    }

    @Override
    public List<NodeInteface> evalute(List<NodeInteface> roots) {
        return null;
    }

    @Override
    public NodeInteface mutate(NodeInteface root, BooleanSupplier mutate, List<NodeFabricInterface> value, List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO) {
        return null;
    }

    @Override
    public PairOfNodes crossover(NodeInteface root1, NodeInteface root2) {
        return null;
    }
}
