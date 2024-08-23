package IR.IRNodes;

public class IRLabelNode extends IRNode {
    public String label = null;

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println();
        System.out.println(label + ":");
        printNext();
    }
}
