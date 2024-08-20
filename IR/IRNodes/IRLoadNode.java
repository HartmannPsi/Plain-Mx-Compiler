package IR.IRNodes;

public class IRLoadNode extends IRNode {
    public String result = null, tp = null, ptr = null;

    public void printToString() {

        if (shadow) {
            printNext();
            return;
        }

        System.out.println(result + " = load " + tp + ", ptr " + ptr);
        printNext();
    }
}
