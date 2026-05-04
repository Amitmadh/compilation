package cfg;

import ir.IrCommand;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CfgNode {
    public IrCommand command;       
    public List<CfgNode> successors;   
    public List<CfgNode> predecessors;
    
    public HashSet<String> inSetTemps;  
    public HashSet<String> outSetTemps; 

    public CfgNode(IrCommand command) {
        this.command = command;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
        
        this.inSetTemps = new HashSet<>();
        this.outSetTemps = new HashSet<>();
    }

    public void addSuccessor(CfgNode node) {
        if (node != null && !this.successors.contains(node)) {
            this.successors.add(node);
            node.predecessors.add(this); 
        }
    }
}
