package data;

import java.util.List;

public class FunctionData {
    public String funcName;
    public List<String> args;
    public List<String> localVars;

    public FunctionData(String funcName, List<String> args, List<String> localVars) {
        this.funcName = funcName;
        this.args = args;
        this.localVars = localVars;
    }
}
