package cfg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import ir.IrCommand;

public class CfgNode {
    public IrCommand command;       
    public List<CfgNode> successors;   
    public List<CfgNode> predecessors;

    
    public HashSet<String> inSetTemps; 
    public HashSet<String> inSetVars;  
    public HashSet<String> outSetTemps; 
    public HashSet<String> outSetVars; 

    public CfgNode(IrCommand command) {
        this.command = command;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    
    }
}
