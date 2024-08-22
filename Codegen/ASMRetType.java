package Codegen;

import Codegen.ASMNodes.ASMNode;

public class ASMRetType {
    public ASMNode head = null, tail = null;

    public ASMRetType(ASMNode head, ASMNode tail) {
        this.head = head;
        this.tail = tail;
    }
}
