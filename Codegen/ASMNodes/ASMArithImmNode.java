package Codegen.ASMNodes;

public class ASMArithImmNode extends ASMNode {
    public String rd = null, rs1 = null, imm = null, op = null;

    public void printToString() {
        System.out.print(op + " " + rd +
                ", " + rs1);
        if (imm != null) {
            System.out.println(", " + imm);
        } else {
            System.out.println();
        }

        printNext();
    }
}
