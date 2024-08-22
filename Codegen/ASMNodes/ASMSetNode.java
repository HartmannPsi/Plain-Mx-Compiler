package Codegen.ASMNodes;

public class ASMSetNode extends ASMNode {
    public String rd = null, rs = null, op = null;

    public void printToString() {
        System.out.println(op + " " + rd + ", " + rs);
        printNext();
    }
}
