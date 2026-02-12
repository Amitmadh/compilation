package cfg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import java.io.PrintWriter;
import ir.*;

public class Cfg {
    private List<CfgNode> nodes = new ArrayList<>();
    private CfgNode startNode;
    private CfgNode exitNode;
    private Map<String, CfgNode> labelToNodeMap = new HashMap<>();
    private HashSet<String> allTemps = new HashSet<>();
    private HashSet<String> allVars = new HashSet<>();


    public Cfg(Ir ir) {
        List<IrCommand> globalInits = new ArrayList<>();
        List<IrCommand> mainBody = new ArrayList<>();
        /* sorting the commands into global inits and main body */
        IrCommand current = ir.getHead();
        IrCommandList tail = ir.getTail();

        while (current != null) {
            if (current.isGlobal) {
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
                sourceNode.addSuccessor(labelToNodeMap.get(targetLabel));
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
        boolean changed;
        do {
            changed = false;
            for (CfgNode node : nodes) {
                HashSet<String> oldOutSetTemps = new HashSet<>(node.outSetTemps);
                HashSet<String> oldOutSetVars = new HashSet<>(node.outSetVars);
                
                computeInSet(node);
                computeOutSet(node);

                if (!node.outSetTemps.equals(oldOutSetTemps) || !node.outSetVars.equals(oldOutSetVars)) {
                    changed = true;
                }
            }
        } while (changed);

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

        List<String> updated = new ArrayList<>();

        for (String s : usedBefore) {
            int index = s.lastIndexOf("offset");
            if (index != -1) {
                updated.add(s.substring(0, index));
            } else {
                updated.add(s);
            }
        }
        return updated;
    }
}
