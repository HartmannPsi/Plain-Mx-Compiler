package Codegen.ASMNodes;

public class ASMSwNode extends ASMNode {
    public String rs1 = null, rs2 = null, imm = null;

    public void printToString() {
        if (rs1 != null)
            System.out.println("sw " + rs2 + ", " + imm + "(" + rs1 + ")");
        else
            System.out.println("sw " + rs2 + ", " + imm);
        printNext();
    }
}
