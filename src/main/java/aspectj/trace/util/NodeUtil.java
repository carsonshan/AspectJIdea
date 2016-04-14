package aspectj.trace.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by syc on 4/3/16.
 */
public class NodeUtil {
    /*节点被调用位置*/
    private String callLocation;
    private NodeUtil parentNode;
    private List<NodeUtil> childNodes;
    private String name;
    private int runLineNum;

    public NodeUtil(String name, NodeUtil parentNode, String callLocation, int runLineNum) {
        this.name = name;
        this.parentNode = parentNode;
        this.callLocation = callLocation;
        this.runLineNum = runLineNum;
        this.childNodes = new ArrayList<NodeUtil>();
        if (parentNode != null) {
            parentNode.addChild(this);
        }
    }

    public void addChild(NodeUtil child) {
        childNodes.add(child);
    }

    public String getName() {
        return name;
    }

    public List<NodeUtil> getChildNodes() {
        return childNodes;
    }

    public NodeUtil getParentNode() {
        return parentNode;
    }

    public String getCallLocation() {
        return callLocation;
    }

    public int getRunLineNum() {
        return runLineNum;
    }

    @Override
    public boolean equals(Object o){
        return this == o;
    }
}
