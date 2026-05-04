package cfg;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import ir.*;

public class Cfg {
    private List<CfgNode> nodes = new ArrayList<>();
    private List<CfgNode> cfgs = new ArrayList<>();
    private List<List<CfgNode>> cfgsNodes = new ArrayList<>();
    private Map<String, CfgNode> labelToNodeMap = new HashMap<>();
    public List<String> globalVars = new ArrayList<>();

    public Cfg(Ir ir) {
        List<IrCommand> globalInits = new ArrayList<>();
        List<IrCommand> mainBody = new ArrayList<>();
        /* sorting the commands into global inits and main body */
        IrCommand current = ir.getHead();
        IrCommandList tail = ir.getTail();

        while (current != null) {
            if (current.isGlobal) {
                globalInits.add(current);
                if (current instanceof IrCommandAllocate) {
                    globalVars.add(((IrCommandAllocate) current).getVar());
                }
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

        /* setting cfgs */
        if (!nodes.isEmpty()) {
            //add global cfg
            CfgNode firstNode = nodes.get(0);
            if (firstNode.command.isGlobal) {
                cfgs.add(firstNode);
            }

            String currFunc = null;
            for (CfgNode node : nodes) {
                if (node.command instanceof IrCommandFuncDec) {
                    cfgs.add(node);
                    currFunc = ((IrCommandFuncDec) node.command).funcName;

                    for (CfgNode predecessor : node.predecessors) {
                        predecessor.successors.remove(node);
                    }
                    node.predecessors.clear();
                }

                if (node.command instanceof IrCommandEndOfFunction && currFunc != null && ((IrCommandEndOfFunction) node.command).getFunctionName().equals(currFunc)) {
                    currFunc = null;

                    for (CfgNode successor : node.successors) {
                        successor.predecessors.remove(node);
                    }
                    node.successors.clear();
                }
            }
        }

        setCfgsNodes();
    }

    public List<CfgNode> getNodes() {
        return nodes;
    }

    private void setCfgsNodes() {
        for (CfgNode cfg : cfgs) {
            List<CfgNode> cfgNodes = new ArrayList<>();
            Set<CfgNode> visited = new HashSet<>();
            collectCfgNodes(cfg, visited, cfgNodes);
            cfgsNodes.add(cfgNodes);
        }
    }

    private void collectCfgNodes(CfgNode node, Set<CfgNode> visited, List<CfgNode> cfgNodes) {
        if (node == null || visited.contains(node)) {
            return;
        }
        visited.add(node);
        cfgNodes.add(node);
        for (CfgNode successor : node.successors) {
            collectCfgNodes(successor, visited, cfgNodes);
        }
    }

    private void computeOutSet(CfgNode node) {
        HashSet<String> tempUnion = new HashSet<String>();
        if (!node.successors.isEmpty()) {
            for (CfgNode succ : node.successors) {
                tempUnion.addAll(succ.inSetTemps);
            }
        }
        node.outSetTemps = tempUnion;
    }

    private void computeInSet(CfgNode node) {
        IrCommand command = node.command;

        node.inSetTemps = new HashSet<String>(node.outSetTemps);

        if (command == null) {
            return;
        }

        //kill
        if (command.tempDefined() != null) {
            node.inSetTemps.remove(command.tempDefined());
        }

        //gen
        if (command.tempsUsed() != null) {
            node.inSetTemps.addAll(command.tempsUsed());
        }
    }

    public void livenessAnalysis() {
        boolean changed;

        for (List<CfgNode> cfgNodes : cfgsNodes) {
            do {
                changed = false;
                for (int i = cfgNodes.size()-1; i>=0; i--) {
                    CfgNode node = cfgNodes.get(i);
                    HashSet<String> oldOutSetTemps = new HashSet<>(node.outSetTemps);
                    
                    computeOutSet(node);
                    computeInSet(node);

                    if (!node.outSetTemps.equals(oldOutSetTemps)) {
                        changed = true;
                    }
                }
            } while (changed);    
        }
    }

    public void printCfgs() {
        livenessAnalysis();
        for (CfgNode cfg : cfgs) {
            File file;
            if (cfg.command instanceof IrCommandFuncDec) {
                file = new File(((IrCommandFuncDec) cfg.command).funcName + "_cfg.txt");
            } else {
                file = new File("global_cfg.txt");
            }
            try (PrintWriter writer = new PrintWriter(file)) {
                if (cfg.command instanceof IrCommandFuncDec) {
                    writer.println("CFG for function: " + ((IrCommandFuncDec) cfg.command).funcName);
                } else {
                    writer.println("CFG for global initializations");
                }
                printCfg(cfg, new HashSet<>(), writer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 

    public void printCfg(CfgNode node, HashSet<CfgNode> visited, PrintWriter writer) {
        if (node == null || visited.contains(node)) {
            return;
        }
        visited.add(node);
        writer.print("Node: " );
        node.command.printMe(writer);
        for (CfgNode successor : node.successors) {
            writer.print("  Successor: " );
            successor.command.printMe(writer);
        }
        writer.print("  out: " + node.outSetTemps + "\n");
        writer.print("  in: " + node.inSetTemps + "\n");
        for (CfgNode successor : node.successors) {
            printCfg(successor, visited, writer);
        }
    }
}

