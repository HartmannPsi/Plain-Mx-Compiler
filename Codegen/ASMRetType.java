package Codegen;

import Codegen.ASMNodes.ASMNode;

public class ASMRetType {
    public ASMNode head = null, tail = null;
    public String reg = null;

    public ASMRetType(ASMNode head, ASMNode tail) {
        this.head = head;
        this.tail = tail;
    }

    public ASMRetType(ASMNode head, ASMNode tail, String reg) {
        this.head = head;
        this.tail = tail;
        this.reg = reg;
    }
}
