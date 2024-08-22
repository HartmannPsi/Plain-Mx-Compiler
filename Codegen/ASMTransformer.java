package Codegen;

import IR.IRNodes.*;

public class ASMTransformer {

    IRNode beg;

    public ASMTransformer(IRNode beg) {
        this.beg = beg;
    }

    public void generateASM() {
    }

    public void printASM() {
        beg.printToString();
    }

}
