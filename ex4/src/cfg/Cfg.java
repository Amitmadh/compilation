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
import ir.IrCommandStore;

public class Cfg {
    private List<CfgNode> nodes = new ArrayList<>();
    private CfgNode startNode;
    private CfgNode exitNode;
    private Map<String, CfgNode> labelToNodeMap = new HashMap<>();

    public Cfg(Ir ir) {
        List<IrCommand> globalInits = new ArrayList<>();
        List<IrCommand> mainBody = new ArrayList<>();
        
        /* sorting the commands into global inits and main body */
        IrCommand current = ir.getHead();
        IrCommandList tail = ir.getTail();

        while (current != null) {
            if (isGlobalInitialization(current)) {
                globalInits.add(current);
            } 
            else {
                mainBody.add(current);
            }

            /* advance to the next command in IR */
            current = (tail != null) ? tail.head : null;
            tail = (tail != null) ? tail.tail : null;
        }

        /* merge the lists: first the global inits, then the main body */
        List<IrCommand> commands = new ArrayList<>();
        commands.addAll(globalInits);
        commands.addAll(mainBody);

        /* map labels to nodes */
        CfgNode previousNode = null;

        for (IrCommand cmd : commands) {
            CfgNode currentNode = new CfgNode(cmd);
            nodes.add(currentNode);

            if (cmd instanceof ir.IrCommandLabel) {
                labelToNodeMap.put(((ir.IrCommandLabel) cmd).labelName, currentNode);
            }

            /* create sequential edges */
            if (previousNode != null) {
                IrCommand prevCmd = previousNode.command;
                /* not creating a sequential edge if the previous command is an unconditional jump */
                if (!(prevCmd instanceof ir.IrCommandJumpLabel)) {
                    previousNode.addSuccessor(currentNode);
                }
            }
            previousNode = currentNode;
        }

        /* add jump edges */
        for (int i = 0; i < nodes.size(); i++) {
            CfgNode sourceNode = nodes.get(i);
            IrCommand cmd = sourceNode.command;

            String targetLabel = null;

            if (cmd instanceof ir.IrCommandJumpLabel) {
                targetLabel = ((ir.IrCommandJumpLabel) cmd).labelName;
            } 
            else if (cmd instanceof ir.IrCommandJumpIfEqToZero) {
                targetLabel = ((ir.IrCommandJumpIfEqToZero) cmd).labelName;
            }

            /* add edge to the target node for any jump*/
            if (targetLabel != null && labelToNodeMap.containsKey(targetLabel)) {
                CfgNode targetNode = labelToNodeMap.get(targetLabel);
                
                /* skip label nodes to point directly to the next executable command */
                while (targetNode.command instanceof IrCommandLabel && !targetNode.successors.isEmpty()) {
                    targetNode = targetNode.successors.get(0);
                }
                
                sourceNode.addSuccessor(targetNode);
            }
        }

        /* setting start and exit nodes */
        if (!nodes.isEmpty()) {
            startNode = nodes.get(0);
            exitNode = nodes.get(nodes.size() - 1);
        }
    }

    /* helper method to identify global initialization commands */
    private boolean isGlobalInitialization(IrCommand cmd) {
        /* if this is a Store command (from VarDec) and the offset is non-negative */
        if (cmd instanceof IrCommandStore) {
            return ((IrCommandStore) cmd).offset >= 0;
        }
        // other commands (like Load or local Store) belong to Main
        return false;
    }
    
        

    

    public void runChaoticIterations() {
        
        
    }
}
