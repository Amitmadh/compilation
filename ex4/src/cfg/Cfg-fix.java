// package cfg;

// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.HashSet;

// import java.io.PrintWriter;

// import ir.*;

// public class Cfg {
//     private List<CfgNode> nodes = new ArrayList<>();
//     private CfgNode startNode;
//     private CfgNode exitNode = new CfgNode(null); /* Exit node with no successors */
//     private Map<String, CfgNode> labelToNode = new HashMap<>();

//     private HashSet<String> allTemps = new HashSet<>();
//     private HashSet<String> allVars = new HashSet<>();


//     public Cfg(Ir ir) {
//         /* Create CFG nodes and edges by two passes */
//         /* First pass: create nodes and map labels to nodes */
//         IrCommand currentCommand = ir.getHead();
//         IrCommandList tail = ir.getTail();
//         CfgNode prevNode = null;

//         while (currentCommand != null) {
//             CfgNode currentNode = new CfgNode(currentCommand);
//             nodes.add(currentNode);
//             if (startNode == null) startNode = currentNode;

//             if (currentCommand instanceof IrCommandLabel) {
//                 IrCommandLabel labelCmd = (IrCommandLabel) currentCommand;

//                 labelToNode.put(labelCmd.labelName, currentNode);
//             }
//             if (prevNode != null && !(prevNode.command instanceof IrCommandJumpLabel)) {
//                 prevNode.successors.add(currentNode);
//                 currentNode.predecessors.add(prevNode);
//             }
//             prevNode = currentNode;
//             currentCommand = (tail != null) ? tail.head : null;
//             tail = (tail != null) ? tail.tail : null;
//         }
//         /* Link last node to exit node if it's not a jump */
//         if (prevNode != null && !(prevNode.command instanceof IrCommandJumpLabel)) {
//             prevNode.successors.add(exitNode);
//             exitNode.predecessors.add(prevNode);
//         }
//         nodes.add(exitNode);

//         /* Second pass: add edges for jump commands */
//         for (int i = 0; i < nodes.size(); i++) {
//             CfgNode currentNode = nodes.get(i);
//             IrCommand cmd = currentNode.command;
//             String targetLabel = null;
//             if (cmd instanceof IrCommandJumpLabel) {
//                 targetLabel = ((IrCommandJumpLabel) cmd).labelName;
//             } 
//             else if (cmd instanceof IrCommandJumpIfEqToZero) {
//                 targetLabel = ((IrCommandJumpIfEqToZero) cmd).labelName;
//             }

//             if (targetLabel != null) {
//                 CfgNode labelNode = labelToNode.get(targetLabel);
//                 if (labelNode != null) {
//                     currentNode.successors.add(labelNode);
//                     labelNode.predecessors.add(currentNode);
//                 }
//             }
//         }

//         collectAllSymbols();
//         initializeDataflow();
//     }

//     /* helper method to identify global initialization commands */
//     private boolean isGlobalInitialization(IrCommand cmd) {
//         /* if this is a Store command (from VarDec) and the offset is non-negative */
//         if (cmd instanceof IrCommandStore) {
//             return ((IrCommandStore) cmd).offset >= 0;
//         }
//         // other commands (like Load or local Store) belong to Main
//         return false;
//     }

//     private void collectAllSymbols() {
//         for (CfgNode node : nodes) {
//             IrCommand cmd = node.command;
//             if (cmd == null) continue;

//             if (cmd.tempsUsed() != null) {
//                 allTemps.addAll(cmd.tempsUsed());
//             }
//             if (cmd.tempDefined() != null) {
//                 allTemps.add(cmd.tempDefined());
//             }

//             if (cmd instanceof IrCommandStore) {
//                 allVars.add(((IrCommandStore) cmd).getVar());
//             }
//             if (cmd instanceof IrCommandLoad) {
//                 allVars.add(((IrCommandLoad) cmd).getVar());
//             }
//         }
//     }

//     private void initializeDataflow() {
//         for (CfgNode node : nodes) {
//             if (node == startNode) {
//                 node.inSetTemps  = new HashSet<>();
//                 node.outSetTemps = new HashSet<>();
//                 node.inSetVars   = new HashSet<>();
//                 node.outSetVars  = new HashSet<>();
//             } else {
//                 node.inSetTemps  = new HashSet<>(allTemps);
//                 node.outSetTemps = new HashSet<>(allTemps);
//                 node.inSetVars   = new HashSet<>(allVars);
//                 node.outSetVars  = new HashSet<>(allVars);
//             }
//         }
//     }

//     private void computeInSet(CfgNode node) {
//         //temps in set
//         HashSet<String> tempIntersection = new HashSet<String>();
//         if (!node.predecessors.isEmpty()) {
//             tempIntersection.addAll(node.predecessors.get(0).outSetTemps);
//             for (int i = 1; i < node.predecessors.size(); i++) {
//                 tempIntersection.retainAll(node.predecessors.get(i).outSetTemps);
//             }
//         }
//         node.inSetTemps = tempIntersection;

//         //vars in set
//         HashSet<String> varIntersection = new HashSet<String>();
//         if (!node.predecessors.isEmpty()) {
//             varIntersection.addAll(node.predecessors.get(0).outSetVars);
//             for (int i = 1; i < node.predecessors.size(); i++) {
//                 varIntersection.retainAll(node.predecessors.get(i).outSetVars);
//             }
//         }
//         node.inSetVars = varIntersection;
//     }

//     private void computeOutSet(CfgNode node) {
//         IrCommand command = node.command;

//         node.outSetTemps = new HashSet<String>(node.inSetTemps);
//         node.outSetVars = new HashSet<String>(node.inSetVars);

//         if (command == null) {
//             return;
//         }

//         if (command instanceof IrCommandStore) {
//             IrCommandStore storeCmd = (IrCommandStore) command;
//             if (node.inSetTemps.containsAll(storeCmd.tempsUsed())) {
//                 node.outSetVars.add(storeCmd.getVar());
//             } else {
//                 node.outSetVars.remove(storeCmd.getVar());
//             }
//         } else if (command instanceof IrCommandLoad) {
//             IrCommandLoad loadCmd = (IrCommandLoad) command;
//             if (loadCmd.tempDefined() != null && node.inSetVars.contains(loadCmd.getVar())) {
//                 node.outSetTemps.add(loadCmd.tempDefined());
//             } else {
//                 node.outSetTemps.remove(loadCmd.tempDefined());
//             }
//         } else if (command instanceof IrCommandAllocate){
//             IrCommandAllocate allocCmd = (IrCommandAllocate) command;
//             node.outSetVars.remove(allocCmd.getVar());
//         } else {
//             if (command.tempDefined() != null && node.inSetTemps.containsAll(command.tempsUsed())) {
//                 node.outSetTemps.add(command.tempDefined());
//             } else {
//                 node.outSetTemps.remove(command.tempDefined());
//             }
//         }
//     }

//     private void runChaoticIterations() throws java.io.FileNotFoundException {
        
//         /* PrintWriter fileWriter = new PrintWriter("output/in&out.txt"); */

//         boolean changed;
//         do {
//             changed = false;
//             for (CfgNode node : nodes) {
//                 HashSet<String> oldOutSetTemps = new HashSet<>(node.outSetTemps);
//                 HashSet<String> oldOutSetVars = new HashSet<>(node.outSetVars);
                
//                 computeInSet(node);
//                 computeOutSet(node);

// /*                 if (node.command != null) {
//                     node.command.printMe(fileWriter);
//                     fileWriter.format("IN Temps: %s\n", node.inSetTemps);
//                     fileWriter.format("OUT Temps: %s\n", node.outSetTemps);
//                     fileWriter.format("IN Vars: %s\n", node.inSetVars);
//                     fileWriter.format("OUT Vars: %s\n\n", node.outSetVars);
//                 } */

//                 if (!node.outSetTemps.equals(oldOutSetTemps) || !node.outSetVars.equals(oldOutSetVars)) {
//                     changed = true;
//                 }
//             }
//         } while (changed);

//         /* fileWriter.close(); */
//     }

//     public List<String> usedBeforeSet() throws java.io.FileNotFoundException {
//         List<String> usedBefore = new ArrayList<>();

//         runChaoticIterations();

//         for (CfgNode node : nodes) {
//             IrCommand command = node.command;
//             if (command != null && command instanceof IrCommandLoad) {
//                 IrCommandLoad loadCmd = (IrCommandLoad) command;
//                 if (!node.inSetVars.contains(loadCmd.getVar()) && !usedBefore.contains(loadCmd.getVar())) {
//                     usedBefore.add(loadCmd.getVar());
//                 }
//             }
//         }
//         return usedBefore;
//     }
// }
