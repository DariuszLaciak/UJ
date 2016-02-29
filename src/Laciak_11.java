import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

class ClassGenerator implements ClassGeneratorInterface {
    public static Map<String,List<BetterValueNode>> result;
    public static  NodeInteface root;
    @Override
    public void generate(String className, NodeInteface root) {
        ClassGenerator.root = root;
        result = new HashMap<>();
        getVariables(root);
        createCode(className,createFileConente(className));
        JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
        if ( javac.run(null, null, null, className+".java") == 0 ) {
            try {
                Class.forName( className );
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private String createFileConente(String className){
        String result = "import java.util.List;\n" +
                "import java.util.Map;\n\n" +
                "class "+className+" implements UniversalGetterInterface{\n" +
                "\n" +
                "    @Override\n" +
                "    public double get(double... variables) {\n" +
                "        NodeInteface root = ClassGenerator.root;\n" +
                "        Map<String,List<BetterValueNode>> nodeVariables = ClassGenerator.result;\n" +
                "        int iterator = 0;\n" +
                "        for(Map.Entry<String,List<BetterValueNode>> entry : nodeVariables.entrySet()){\n" +
                "            for(BetterValueNode node : entry.getValue()){\n" +
                "                node.setValue(variables[iterator]);\n" +
                "            }\n" +
                "            iterator++;\n" +
                "        }\n" +
                "        return root.getValue();\n" +
                "    }\n" +
                "}";


        return result;
    }


    private void getVariables(NodeInteface node){
        if(node.getChilds() != null && node.getChilds().size() >0) {
            for (NodeInteface child : node.getChilds()) {
                if (child instanceof BetterValueNode) {
                    if (result.containsKey(child.toString())) {
                        List<BetterValueNode> values = result.get(child.toString());
                        values.add((BetterValueNode)child);
                    } else {
                        List<BetterValueNode> values = new ArrayList<>();
                        values.add((BetterValueNode)child);
                        result.put(child.toString(), values);
                    }
                }
                getVariables(child);
            }
        }
    }


    private static void createCode(String className, String content) {
        try ( Writer wr = new FileWriter( className+".java") ){
            wr.write( content );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

