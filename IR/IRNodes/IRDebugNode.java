package IR.IRNodes;

public class IRDebugNode extends IRNode {
    public String message = null;

    public void printToString() {

        System.out.println(message);
        printNext();
    }

    public String toString() {
        return message;
    }
}
