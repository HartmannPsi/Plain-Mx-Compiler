package IR.IRNodes;

public class IRLoadNode extends IRNode {
    public String result = null, tp = null, ptr = null;
    // public boolean eliminated = false;

    public String[] use() {
        if (isActive(ptr)) {
            return new String[] { ptr };
        } else {
            return null;
        }
    }

    public String def() {
        return result;
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

    public String glb() {
        if (ptr.charAt(0) == '@') {
            return ptr;
        } else {
            return null;
        }
    }

    public String toString() {

        if (eliminated) {
            return "";
        }

        return result + " = load " + tp + ", ptr " + ptr;
    }
}
