package cfg;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import ir.IrCommand;

public class CfgNode {
    public IrCommand command;       
    public List<CfgNode> successors;   
    public List<CfgNode> predecessors;

    
    // public BitSet inSet;  
    // public BitSet outSet; 

    public CfgNode(IrCommand command) {
        this.command = command;
        this.successors = new ArrayList<>();
        this.predecessors = new ArrayList<>();
    
    }
}
