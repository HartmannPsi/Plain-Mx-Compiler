package Codegen.ASMNodes;

public class ASMMvNode extends ASMNode {
    public String rd = null, rs = null;

    public void printToString() {
        printRetract();
        System.out.println("mv " + rd + ", " + rs);
        printNext();
    }
}
