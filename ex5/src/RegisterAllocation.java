import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import cfg.*;
import ir.*;
import temp.Temp;

public class RegisterAllocation {
    Set<String> vertices = new HashSet<>();
    Set<Set<String>> edges = new HashSet<>();

    boolean alocFailed = false;
    
    public RegisterAllocation(Cfg cfg) throws FileNotFoundException {
        cfg.livenessAnalysis();

        /* collect all temps from the CFG */
        for (CfgNode node : cfg.getNodes()) {
            IrCommand cmd = node.command;
            vertices.addAll(cmd.tempsUsed());
            vertices.add(cmd.tempDefined());
        }

        vertices.remove(null); // Remove null if tempDefined() returns null

        /* collect edges from the CFG */
        for (CfgNode node : cfg.getNodes()) {
            for (String temp1 : node.inSetTemps) {
                for (String temp2 : node.inSetTemps) {
                    addEdge(temp1, temp2);
                }
            }
        }

        Map<String, Integer> mapping = colorGraph();

        if (alocFailed == true) {
            throw new RuntimeException("Register Allocation Failed");
        }

        for (CfgNode node : cfg.getNodes()) {
            IrCommand cmd = node.command;
            for (Temp temp : cmd.temps()) {
                String tempName = "t" + temp.getSerialNumber();
                if (mapping.containsKey(tempName)) {
                    temp.setRegister(mapping.get(tempName));
                }
            }
        }
    }

    public Map<String, Integer> allocateRegisters() {
        Map<String, Integer> mapping = colorGraph();
        return mapping;
    }

    private void addEdge(String temp1, String temp2) {
        if (temp1.equals(temp2)) {
            return; // No self-loops
        }
        Set<String> edge = new HashSet<>();
        edge.add(temp1);
        edge.add(temp2);
        edges.add(edge);
    }

    private Map<String, Integer> colorGraph() {
        Set<String> currVertices = new HashSet<>(vertices);
        Set<Set<String>> currEdges = new HashSet<>(edges);

        Map<String, Integer> coloring = new HashMap<>();
        Stack<String> stack = new Stack<>();
        boolean changed = true;
        while (!currVertices.isEmpty() && changed) {
            changed = false;

            Set<String> verticesToRemove = new HashSet<>();
            for (String vertex : currVertices) {
                if (degree(vertex, currEdges) < 10) {
                    verticesToRemove.add(vertex);
                }
            }
            for (String vertex : verticesToRemove) {
                removeVertex(vertex, currVertices, currEdges);
                stack.push(vertex);
                changed = true;
            }
        }

        if (!currVertices.isEmpty()) {
            alocFailed = true;
        }

        List<Integer> colors = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            currVertices.add(vertex);

            for (Set<String> edge : edges) {
                if (currVertices.containsAll(edge)) {
                    currEdges.add(edge);
                }
            }

            Set<Integer> usedColors = new HashSet<>();
            for (Set<String> edge : currEdges) {
                if (edge.contains(vertex)) {
                    for (String neighbor : edge) {
                        if (!neighbor.equals(vertex) && coloring.containsKey(neighbor)) {
                            usedColors.add(coloring.get(neighbor));
                        }
                    }
                }
            }

            for (Integer color : colors) {
                if (!usedColors.contains(color)) {
                    coloring.put(vertex, color);
                    break;
                }
            }
        }
        return coloring;
    }

    private int degree(String vertex, Set<Set<String>> edgeSet) {
        int degree = 0;
        for (Set<String> edge : edgeSet) {
            if (edge.contains(vertex)) {
                degree++;
            }
        }
        return degree;
    }

    private void removeVertex(String vertex, Set<String> currVertices, Set<Set<String>> currEdges) {
        Set<Set<String>> edgesToRemove = new HashSet<>();

        for (Set<String> edge : currEdges) {
            if (edge.contains(vertex)) {
                edgesToRemove.add(edge);
            }
        }
        currEdges.removeAll(edgesToRemove);
        currVertices.remove(vertex);
    }
}
