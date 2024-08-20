package IR.IRNodes;

public class IRConstStrNode extends IRNode {
    public String result = null, literal = null;
    public int length = 0;

    public void printToString() {

        shadow = false;

        if (shadow) {
            printNext();
            return;
        }

        System.out.println(result + " = private unnamed_addr constant [" + length + " x i8] c\"" + literal + "\\00\"");
        printNext();
    }

}
