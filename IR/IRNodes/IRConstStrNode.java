package IR.IRNodes;

public class IRConstStrNode extends IRNode {
    public String result = null, literal = null;
    public int length = 0;

    public void printToString() {

        if (shadow) {
            printNext();
            return;
        }

        System.out.println(result + " = constant [" + length + " x i8] c\"" + literal + "\\00\"");
        printNext();
    }

}
