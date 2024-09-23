package IR.IRNodes;

public class IRLoadNode extends IRNode {
    public String result = null, tp = null, ptr = null;
    public boolean eliminated = false;

    public String use() {
        return ptr;
    }

    public void printToString() {

        if (eliminated) {
            printNext();
            return;
        }

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = load " + tp + ", ptr " + ptr);
        printNext();
    }

    public String toString() {

        if (eliminated) {
            return "";
        }

        return result + " = load " + tp + ", ptr " + ptr;
    }
}
