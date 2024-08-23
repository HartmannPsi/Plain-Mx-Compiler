package Codegen.ASMNodes;

public class ASMDotInstNode extends ASMNode {
    public String inst = null, arg1 = null, arg2 = null;

    public void printToString() {
        printRetract();
        System.out.print(inst + " " + arg1);
        if (arg2 != null) {
            System.out.println(", " + arg2);
        } else {
            System.out.println();
        }

        printNext();
    }

}
