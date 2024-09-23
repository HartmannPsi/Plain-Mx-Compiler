package IR.IRNodes;

public class IRMvNode extends IRNode {
    public String result = null, src = null;

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        System.out.println(result + " = add i32 0, " + src + "; this is a mv instruction to eliminate phi");

        printNext();
    }

    public String toString() {
        return result + " = add i32 0, " + src + "; this is a mv instruction to eliminate phi";
    }

}
