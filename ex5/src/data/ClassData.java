package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassData {
    public String className;
    public String extendsName;

    public List<String> vars;
    public List<String> varsNoOffset;
    public List<String> methods;
    public List<String> methodsNoClass;
    public Map<String, FunctionData> methodsData;

    public Map<String,Integer> intVals;
	public Map<String,String> strVals;

    public ClassData(String className, String extendsName, List<String> vars, List<String> varsNoOffset, List<String> methods, List<String> methodsNoClass, Map<String, FunctionData> methodsData, Map<String,Integer> intVals, Map<String,String> strVals) {
        this.className = className;
        this.extendsName = extendsName;
        this.vars = vars;
        this.varsNoOffset = varsNoOffset;
        this.methods = methods;
        this.methodsNoClass = methodsNoClass;
        this.methodsData = methodsData;
        this.intVals = intVals;
        this.strVals = strVals;
    }

    public ClassData(ClassData other) {
        this.className = other.className;
        this.extendsName = other.extendsName;
        this.vars = new ArrayList<>(other.vars);
        this.varsNoOffset = new ArrayList<>(other.varsNoOffset);
        this.methods = new ArrayList<>(other.methods);
        this.methodsNoClass = new ArrayList<>(other.methodsNoClass);
        this.methodsData = new  HashMap<String, FunctionData>(other.methodsData);
        this.intVals = new HashMap<>(other.intVals);
        this.strVals = new HashMap<>(other.strVals);
    }

    public void addFields(ClassData other) {
        vars.addAll(other.vars);
        varsNoOffset.addAll(other.varsNoOffset);
        for (int i = 0; i < other.methodsNoClass.size(); i++) {
            String methodNoClass = other.methodsNoClass.get(i);
            String otherMethod = other.methods.get(i);
            FunctionData otherData = other.methodsData.get(otherMethod);
            if (methodsNoClass.contains(methodNoClass)) {
                int index = methodsNoClass.indexOf(methodNoClass);
                String thisMethod = methods.get(index);
                methods.set(index, otherMethod);
                methodsData.remove(thisMethod);
                methodsData.put(otherMethod, otherData);
            } else {
                methods.add(otherMethod);
                methodsNoClass.add(methodNoClass);
                methodsData.put(otherMethod, otherData);
            }
        }
    }
}
