package IR.IRNodes;

public class IRStoreNode extends IRNode {
    public String tp = null, value = null, ptr = null;
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

        System.out.println("store " + tp + " " + value + ", ptr " + ptr);
        printNext();
    }

    public String toString() {

        if (eliminated) {
            return "";
        }

        return "store " + tp + " " + value + ", ptr " + ptr;
    }
}
