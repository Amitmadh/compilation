package cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;

import java.io.PrintWriter;

import ir.*;

public class Cfg_fix {
    private List<CfgNode> nodes = new ArrayList<>();
    private CfgNode startNode;
    private CfgNode exitNode;
    private Map<String, CfgNode> labelToNodeMap = new HashMap<>();
    private HashSet<String> allTemps = new HashSet<>();
    private HashSet<String> allVars = new HashSet<>();


    public Cfg_fix(Ir ir) {
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

        collectAllSymbols();
        initializeDataflow();
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

    private void collectAllSymbols() {
        for (CfgNode node : nodes) {
            IrCommand cmd = node.command;
            if (cmd == null) continue;

            if (cmd.tempsUsed() != null) {
                allTemps.addAll(cmd.tempsUsed());
            }
            if (cmd.tempDefined() != null) {
                allTemps.add(cmd.tempDefined());
            }

            if (cmd instanceof IrCommandStore) {
                allVars.add(((IrCommandStore) cmd).getVar());
            }
            if (cmd instanceof IrCommandLoad) {
                allVars.add(((IrCommandLoad) cmd).getVar());
            }
        }
    }

    private void initializeDataflow() {
        for (CfgNode node : nodes) {
            if (node == startNode) {
                node.inSetTemps  = new HashSet<>();
                node.outSetTemps = new HashSet<>();
                node.inSetVars   = new HashSet<>();
                node.outSetVars  = new HashSet<>();
            } else {
                node.inSetTemps  = new HashSet<>(allTemps);
                node.outSetTemps = new HashSet<>(allTemps);
                node.inSetVars   = new HashSet<>(allVars);
                node.outSetVars  = new HashSet<>(allVars);
            }
        }
    }

    private void computeInSet(CfgNode node) {
        //temps in set
        HashSet<String> tempIntersection = new HashSet<String>();
        if (!node.predecessors.isEmpty()) {
            tempIntersection.addAll(node.predecessors.get(0).outSetTemps);
            for (int i = 1; i < node.predecessors.size(); i++) {
                tempIntersection.retainAll(node.predecessors.get(i).outSetTemps);
            }
        }
        node.inSetTemps = tempIntersection;

        //vars in set
        HashSet<String> varIntersection = new HashSet<String>();
        if (!node.predecessors.isEmpty()) {
            varIntersection.addAll(node.predecessors.get(0).outSetVars);
            for (int i = 1; i < node.predecessors.size(); i++) {
                varIntersection.retainAll(node.predecessors.get(i).outSetVars);
            }
        }
        node.inSetVars = varIntersection;
    }

    private void computeOutSet(CfgNode node) {
        IrCommand command = node.command;

        node.outSetTemps = new HashSet<String>(node.inSetTemps);
        node.outSetVars = new HashSet<String>(node.inSetVars);

        if (command == null) {
            return;
        }

        if (command instanceof IrCommandStore) {
            IrCommandStore storeCmd = (IrCommandStore) command;
            if (node.inSetTemps.containsAll(storeCmd.tempsUsed())) {
                node.outSetVars.add(storeCmd.getVar());
            } else {
                node.outSetVars.remove(storeCmd.getVar());
            }
        } else if (command instanceof IrCommandLoad) {
            IrCommandLoad loadCmd = (IrCommandLoad) command;
            if (loadCmd.tempDefined() != null && node.inSetVars.contains(loadCmd.getVar())) {
                node.outSetTemps.add(loadCmd.tempDefined());
            } else {
                node.outSetTemps.remove(loadCmd.tempDefined());
            }
        } else if (command instanceof IrCommandAllocate){
            IrCommandAllocate allocCmd = (IrCommandAllocate) command;
            node.outSetVars.remove(allocCmd.getVar());
        } else {
            if (command.tempDefined() != null && node.inSetTemps.containsAll(command.tempsUsed())) {
                node.outSetTemps.add(command.tempDefined());
            } else {
                node.outSetTemps.remove(command.tempDefined());
            }
        }
    }

    private void runChaoticIterations() throws java.io.FileNotFoundException {
        
        /* PrintWriter fileWriter = new PrintWriter("output/in&out.txt"); */

        boolean changed;
        do {
            changed = false;
            for (CfgNode node : nodes) {
                HashSet<String> oldOutSetTemps = new HashSet<>(node.outSetTemps);
                HashSet<String> oldOutSetVars = new HashSet<>(node.outSetVars);
                
                computeInSet(node);
                computeOutSet(node);

/*                 if (node.command != null) {
                    node.command.printMe(fileWriter);
                    fileWriter.format("IN Temps: %s\n", node.inSetTemps);
                    fileWriter.format("OUT Temps: %s\n", node.outSetTemps);
                    fileWriter.format("IN Vars: %s\n", node.inSetVars);
                    fileWriter.format("OUT Vars: %s\n\n", node.outSetVars);
                } */

                if (!node.outSetTemps.equals(oldOutSetTemps) || !node.outSetVars.equals(oldOutSetVars)) {
                    changed = true;
                }
            }
        } while (changed);

        /* fileWriter.close(); */
    }

    public List<String> usedBeforeSet() throws java.io.FileNotFoundException {
        List<String> usedBefore = new ArrayList<>();

        runChaoticIterations();

        for (CfgNode node : nodes) {
            IrCommand command = node.command;
            if (command != null && command instanceof IrCommandLoad) {
                IrCommandLoad loadCmd = (IrCommandLoad) command;
                if (!node.inSetVars.contains(loadCmd.getVar()) && !usedBefore.contains(loadCmd.getVar())) {
                    usedBefore.add(loadCmd.getVar());
                }
            }
        }
        return usedBefore;
    }
}
