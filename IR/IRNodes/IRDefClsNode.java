package IR.IRNodes;

public class IRDefClsNode extends IRNode {
    public String cls_name = null;
    public String[] tps = null;

    public void printToString() {
        System.out.print(cls_name + " = type { ");
        if (tps != null) {
            for (int i = 0; i < tps.length; i++) {
                System.out.print(tps[i]);
                if (i != tps.length - 1) {
                    System.out.print(", ");
                }
            }
        }
        System.out.println(" }");
        System.out.println();

        printNext();
    }
}
