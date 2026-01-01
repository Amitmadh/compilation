package cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ir.Ir;
import ir.IrCommand;
import ir.IrCommandJumpIfEqToZero;
import ir.IrCommandJumpLabel;
import ir.IrCommandLabel;
import ir.IrCommandList;

public class Cfg {
    private List<CfgNode> nodes = new ArrayList<>();
    private CfgNode startNode;
    private CfgNode exitNode = new CfgNode(null); /* Exit node with no successors */
    private Map<String, CfgNode> labelToNode = new HashMap<>();

    public Cfg(Ir ir) {
        /* Create CFG nodes and edges by two passes */
        /* First pass: create nodes and map labels to nodes */
        IrCommand currentCommand = ir.getHead();
        IrCommandList tail = ir.getTail();
        CfgNode prevNode = null;
        while (currentCommand != null) {
            CfgNode currentNode = new CfgNode(currentCommand);
            nodes.add(currentNode);
            if (startNode == null) startNode = currentNode;

            if (currentCommand instanceof IrCommandLabel) {
                IrCommandLabel labelCmd = (IrCommandLabel) currentCommand;

                labelToNode.put(labelCmd.labelName, currentNode);
            }
            if (prevNode != null && !(prevNode.command instanceof IrCommandJumpLabel)) {
                prevNode.successors.add(currentNode);
                currentNode.predecessors.add(prevNode);
            }
            prevNode = currentNode;
            currentCommand = (tail != null) ? tail.head : null;
            tail = (tail != null) ? tail.tail : null;
        }
        /* Link last node to exit node if it's not a jump */
        if (prevNode != null && !(prevNode.command instanceof IrCommandJumpLabel)) {
            prevNode.successors.add(exitNode);
            exitNode.predecessors.add(prevNode);
        }
        nodes.add(exitNode);

        /* Second pass: add edges for jump commands */
        for (int i = 0; i < nodes.size(); i++) {
            CfgNode currentNode = nodes.get(i);
            IrCommand cmd = currentNode.command;
            String targetLabel = null;
            if (cmd instanceof IrCommandJumpLabel) {
                targetLabel = ((IrCommandJumpLabel) cmd).labelName;
            } 
            else if (cmd instanceof IrCommandJumpIfEqToZero) {
                targetLabel = ((IrCommandJumpIfEqToZero) cmd).labelName;
            }

            if (targetLabel != null) {
                CfgNode labelNode = labelToNode.get(targetLabel);
                if (labelNode != null) {
                    /* Find the target node by following successors until a non-label command or end of list is reached */
                    CfgNode target = labelNode;
                    while (target.command instanceof IrCommandLabel && !target.successors.isEmpty()) {
                        target = target.successors.get(0);
                    }
                    
                    /* If we reach a label with no successors, link to exit node */
                    if (target.command instanceof IrCommandLabel && target.successors.isEmpty()) {
                        target = exitNode;
                    }

                    currentNode.successors.add(target);
                    target.predecessors.add(currentNode);
                }
            }
        }

    }

    public void runChaoticIterations() {
        // כאן ירוץ הלופ שמעדכן את ה-BitSets
        
    }
}
