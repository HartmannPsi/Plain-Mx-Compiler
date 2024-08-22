package Codegen.ASMNodes;

public class ASMBrNode extends ASMNode {
    public String rs1 = null, rs2 = null, label = null, op = null;

    public void printToString() {
        System.out.println(op + " " + rs1 + ", " + (rs2 == null ? "" : (rs2 + ", ")) + label);
        printNext();
    }
}
