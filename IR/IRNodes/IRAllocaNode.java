package IR.IRNodes;

public class IRAllocaNode extends IRNode {
    public String result = null, tp = null;
    public boolean eliminated = false;

    public void printToString() {

        if (eliminated) {
            printNext();
            return;
        }

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = alloca " + tp);
        printNext();
    }

    public String def() {
        return result;
    }

    public String toString() {

        if (eliminated) {
            return "";
        }

        return result + " = alloca " + tp;
    }
}
