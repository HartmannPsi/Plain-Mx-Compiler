package Codegen.ASMNodes;

public class ASMArithNode extends ASMNode {
    public String rd = null, rs1 = null, rs2 = null, op = null;

    public void printToString() {
        System.out.println(op + " " + rd +
                ", " + rs1 + ", " + rs2);
        printNext();
    }
}
