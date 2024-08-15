package IR.IRNodes;

public class IRAllocaNode extends IRNode {
    public String result = null, tp = null;

    public void printToString() {
        System.out.println(result + " = alloca " + tp);
        printNext();
    }
}
