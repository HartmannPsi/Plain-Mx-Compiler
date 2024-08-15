package IR.IRNodes;

public class IRLabelNode extends IRNode {
    public String label = null;

    public void printToString() {
        System.out.println(label + ":");
        printNext();
    }
}
