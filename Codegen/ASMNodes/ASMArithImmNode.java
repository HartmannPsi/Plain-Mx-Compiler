package Codegen.ASMNodes;

public class ASMArithImmNode extends ASMNode {
    public String rd = null, rs1 = null, imm = null, op = null;

    public void printToString() {
        System.out.println(op + " " + rd +
                ", " + rs1 + ", " + imm);
        printNext();
    }
}
