package IR.IRNodes;

public class IRStoreNode extends IRNode {
    public String tp = null, value = null, ptr = null;
    // public boolean eliminated = false;

    public String[] use() {
        String[] res = new String[2];
        if (isActive(value)) {
            res[0] = value;
        }
        if (isActive(ptr)) {
            res[1] = ptr;
        }
        return res;
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
