package IR.IRNodes;

public class IRPseudoArgNode extends IRNode {
    public IRDefFuncNode def_args = null;
    public String pseudo_def = null;
    public int idx = -1;

    public String toString() {
        return "[ Pseudo Arg Node: " + pseudo_def + " ]";
    }

    public void printToString() {
        System.out.println("; [ Pseudo Arg Node: " + pseudo_def + " ]");
        printNext();
    }

    public String def() {
        return pseudo_def;
    }

}
