package Codegen;

import Codegen.ASMNodes.*;
import IR.IRNodes.*;

public class ASMTransformer {

    IRNode ir_beg;
    ASMNode asm_beg = new ASMNode();

    public ASMTransformer(IRNode beg) {
        this.ir_beg = beg;
    }

    public void generateASM() {
        visit(ir_beg, asm_beg);
    }

    public void printASM() {
        asm_beg.printToString();
    }

    void visit(IRNode node, ASMNode prev) {

        if (node instanceof IRAllocaNode) {
            visit((IRAllocaNode) node, prev);
        } else if (node instanceof IRBinaryNode) {
            visit((IRBinaryNode) node, prev);
        } else if (node instanceof IRBrNode) {
            visit((IRBrNode) node, prev);
        } else if (node instanceof IRCallNode) {
            visit((IRCallNode) node, prev);
        } else if (node instanceof IRConstStrNode) {
            visit((IRConstStrNode) node, prev);
        } else if (node instanceof IRDclFuncNode) {
            visit((IRDclFuncNode) node, prev);
        } else if (node instanceof IRDebugNode) {
            visit((IRDebugNode) node, prev);
        } else if (node instanceof IRDefClsNode) {
            visit((IRDefClsNode) node, prev);
        } else if (node instanceof IRDefFuncNode) {
            visit((IRDefFuncNode) node, prev);
        } else if (node instanceof IRGetEleNode) {
            visit((IRGetEleNode) node, prev);
        } else if (node instanceof IRGlbInitNode) {
            visit((IRGlbInitNode) node, prev);
        } else if (node instanceof IRIcmpNode) {
            visit((IRIcmpNode) node, prev);
        } else if (node instanceof IRLabelNode) {
            visit((IRLabelNode) node, prev);
        } else if (node instanceof IRLoadNode) {
            visit((IRLoadNode) node, prev);
        } else if (node instanceof IRNLNode) {
            visit((IRNLNode) node, prev);
        } else if (node instanceof IRPhiNode) {
            visit((IRPhiNode) node, prev);
        } else if (node instanceof IRRetNode) {
            visit((IRRetNode) node, prev);
        } else if (node instanceof IRSelectNode) {
            visit((IRSelectNode) node, prev);
        } else if (node instanceof IRStoreNode) {
            visit((IRStoreNode) node, prev);
        } else if (node == null) {
            return;
        } else {
            visit(node.next, prev);
        }
    }

    void visit(IRAllocaNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRBinaryNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRBrNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRCallNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRConstStrNode node, ASMNode prev) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result;
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".asciz";
        dot_node.arg1 = node.prac_val;
        label_node.next = dot_node;

        ASMDotInstNode dot_node2 = new ASMDotInstNode();
        dot_node2.inst = ".size";
        dot_node2.arg1 = node.result;
        dot_node2.arg2 = Integer.toString(node.length);
        dot_node.next = dot_node2;

        visit(node.next, dot_node2);
    }

    void visit(IRDclFuncNode node, ASMNode prev) {
        visit(node.next, prev);
    }

    void visit(IRDebugNode node, ASMNode prev) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "; " + node.message;
        prev.next = comm_node;
        visit(node.next, comm_node);
    }

    void visit(IRDefClsNode node, ASMNode prev) {
        visit(node.next, prev);
    }

    void visit(IRDefFuncNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRGetEleNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRGlbInitNode node, ASMNode prev) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.result;
        prev.next = label_node;

        ASMDotInstNode dot_node = new ASMDotInstNode();
        dot_node.inst = ".zero";
        dot_node.arg1 = "4";
        label_node.next = dot_node;

        visit(node.next, dot_node);
    }

    void visit(IRIcmpNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRLabelNode node, ASMNode prev) {
        ASMLabelNode label_node = new ASMLabelNode();
        label_node.label = node.label;
        prev.next = label_node;

        visit(node.next, label_node);
    }

    void visit(IRLoadNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRNLNode node, ASMNode prev) {
        ASMCommNode comm_node = new ASMCommNode();
        comm_node.message = "\n";
        prev.next = comm_node;
        visit(node.next, comm_node);
    }

    void visit(IRPhiNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRRetNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRSelectNode node, ASMNode prev) {
        // TODO:
    }

    void visit(IRStoreNode node, ASMNode prev) {
        // TODO:
    }
}
