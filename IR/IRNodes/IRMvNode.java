package IR.IRNodes;

public class IRMvNode extends IRBinaryNode {
    // public String result = null, src = null;

    public void printToString() {

        // shadow = false;

        // if (shadow) {
        // printNext();
        // return;
        // }

        if (tp.equals("ptr")) {
            tp = "i32";
        }

        System.out.println(result + " = add " + tp + " 0, " + op2 + "; this is a MV instruction to eliminate PHI");

        printNext();
    }

    public String toString() {
        if (tp.equals("ptr")) {
            tp = "i32";
        }

        return result + " = add " + tp + " 0, " + op2 + "; this is a MV instruction to eliminate PHI";
    }

}
