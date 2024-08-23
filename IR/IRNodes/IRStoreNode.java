package IR.IRNodes;

public class IRStoreNode extends IRNode {
    public String tp = null, value = null, ptr = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println("store " + tp + " " + value + ", ptr " + ptr);
        printNext();
    }
}
