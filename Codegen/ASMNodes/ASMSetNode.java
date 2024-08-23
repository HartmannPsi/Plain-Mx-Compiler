package Codegen.ASMNodes;

public class ASMSetNode extends ASMNode {
    public String rd = null, rs = null, op = null;

    public void printToString() {
        printRetract();
        System.out.println(op + " " + rd + ", " + rs);
        printNext();
    }
}
