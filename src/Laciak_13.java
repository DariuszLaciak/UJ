import java.util.*;
import java.util.function.BooleanSupplier;

class GeneticProgramming implements GeneticProgrammingInterface {

    private EvaluatorInterface evaluator;

    @Override
    public void setEvaluator(EvaluatorInterface evi) {
        evaluator = evi;
    }

    @Override
    public List<NodeInteface> evalute(List<NodeInteface> roots) {
        List<NodeInteface> returnList = new ArrayList<>();
        Map<NodeInteface, Double> mapList = new HashMap<>();

        for (NodeInteface root : roots) {
            double error = evaluator.getError(root);
            if (Double.isFinite(error)) {
                mapList.put(root, error);
                putRootToList(returnList, root, error, mapList);
            }
        }


        return returnList;
    }

    private void putRootToList(List<NodeInteface> list, NodeInteface root, double error, Map<NodeInteface, Double> map) {
        List<NodeInteface> copyList = new ArrayList<>(list);
        if (list.size() == 0) {
            list.add(root);
        }
        for (int i = 0; i < copyList.size(); i++) {
            if (map.get(copyList.get(i)) > error) {
                list.add(i, root);
                break;
            } else if (i == copyList.size() - 1) {
                list.add(root);
            }
        }
    }

    @Override
    public NodeInteface mutate(NodeInteface root, BooleanSupplier mutate, List<NodeFabricInterface> value, List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO) {
        return doMutation(root, mutate, value, unaryO, binaryO);
    }

    private NodeInteface doMutation(NodeInteface root, BooleanSupplier mutate, List<NodeFabricInterface> value, List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO) {
        Random rand = new Random(System.currentTimeMillis());

        NodeInteface newRoot = null;
        List<NodeInteface> oldChilds = root.getChilds();
        if (mutate.getAsBoolean()) {
            List<NodeFabricInterface> usingList = identifyClass(root, value, unaryO, binaryO);
            newRoot = usingList.get(rand.nextInt(usingList.size())).get();
        }

        boolean flag = true;
        if(oldChilds != null) {
            List<NodeInteface> copyChild = new ArrayList<>(oldChilds);
            for (NodeInteface node : copyChild) {
                node = doMutation(node, mutate, value, unaryO, binaryO);
                try {
                    if (newRoot != null)
                        newRoot.addChild(node);
                    else {
                        if (flag) {
                            oldChilds.clear();
                            flag = false;
                        }
                        root.addChild(node);
                    }
                } catch (NodeInteface.TooManyChildsException e) {
                    e.printStackTrace();
                }
            }
        }
        if (newRoot != null) {
            root = newRoot;
        }

        return root;
    }

    private List<NodeFabricInterface> identifyClass(NodeInteface root, List<NodeFabricInterface> value, List<NodeFabricInterface> unaryO, List<NodeFabricInterface> binaryO) {
        if (root instanceof ValueNode) {
            return value;
        } else if (root instanceof UnaryOperatorNode) {
            return unaryO;
        } else if (root instanceof BinaryOperatorNode) {
            return binaryO;
        } else {
            return null;
        }
    }


    @Override
    public PairOfNodes crossover(NodeInteface root1, NodeInteface root2) {
        PairOfNodes pair = new PairOfNodes();
        Random rand = new Random(System.currentTimeMillis());

        NodeInteface child1 = pickChild(root1,0,rand.nextInt(countChild(root1,0)));
        NodeInteface child2 = pickChild(root2,0,rand.nextInt(countChild(root2,0)));


        NodeInteface root1New =  replaceChild(root1,child2,child1,false);
        NodeInteface root2New =  replaceChild(root2,child1,child2,false);

        pair.n1 = root1New;
        pair.n2 = root2New;

        return pair;
    }

    private NodeInteface replaceChild(NodeInteface root, NodeInteface child, NodeInteface childToReplace,boolean found){
        if(root.equals(childToReplace)){
            root = child;
            return root;
        }
        if(root.getChilds() != null){
            List<NodeInteface> childs = new ArrayList<>(root.getChilds());
            root.getChilds().clear();
            int childCount = childs.size();
            for(NodeInteface node : childs){
                childCount--;
                if(node.equals(childToReplace)){
                    node = child;
                    found = true;
                }
                if(!found)
                    node = replaceChild(node,child,childToReplace,found);
                try {
                    root.addChild(node);
                    if(childCount == 0)
                        break;
                } catch (NodeInteface.TooManyChildsException e) {
                    e.printStackTrace();
                }

            }
        }
        return root;
    }

    private NodeInteface pickChild(NodeInteface root, int count, int rand){
        if(root.getChilds() != null)
            for(NodeInteface child : root.getChilds()){
                count++;
                if(count == rand){
                    return child;
                }
                else
                    root = pickChild(child,count,rand);
            }

        return root;
    }

    private int countChild(NodeInteface root, int count){
        if(root.getChilds() != null)
            for(NodeInteface child : root.getChilds()){
                count++;
                count = countChild(child,count);
            }

        return count;
    }
}
