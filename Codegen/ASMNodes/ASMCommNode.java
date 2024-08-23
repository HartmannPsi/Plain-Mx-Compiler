package Codegen.ASMNodes;

public class ASMCommNode extends ASMNode {
    public String message = null;

    public void printToString() {
        printRetract();
        System.out.println(message);
        printNext();
    }
}
