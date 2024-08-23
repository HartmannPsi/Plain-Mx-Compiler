package IR.IRNodes;

public class IRAllocaNode extends IRNode {
    public String result = null, tp = null;

    public void printToString() {

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = alloca " + tp);
        printNext();
    }

    public String toString() {
        return result + " = alloca " + tp;
    }
}
