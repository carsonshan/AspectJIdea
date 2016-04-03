package aspectj.trace.util;

import javax.xml.soap.Node;
import java.util.*;

/**
 * Created by syc on 4/3/16.
 */
public class TreeUtil {
    private NodeUtil root;
    private NodeUtil now;

    public TreeUtil() {
        root = now = null;
    }

    /**
     * 添加一个调用
     *
     * @param srcFunction
     * @param dstFunction
     * @param callLocation
     * @return
     */
    public NodeUtil addRunTime(String srcFunction, String dstFunction, String callLocation) {
        if(root == null){
            root = new NodeUtil(srcFunction,null,"str");
            now = root;
        }
        return runNext(srcFunction, dstFunction, callLocation);
    }

    /**
     * 获取从起始函数到目标函数的所有调用路径
     *
     * @param srcFunction
     * @param dstFunction
     * @return List<List<Pair<调用行,Pair<调用者,被调用者>>>>
     */
    public List<List<Pair<String,Pair<String,String>>>> getCallPaths( String srcFunction, String dstFunction){
        return findPaths(root, srcFunction, dstFunction);
    }

    /**
     * 添加一个叶子节点
     *
     * @param srcFunction
     * @param dstFunction
     * @param callLocation
     * @return
     */
    private NodeUtil runNext(String srcFunction, String dstFunction, String callLocation){
        while(now!=root){
            if(!now.getName().equals(srcFunction)){
                now = now.getParentNode();
            }
        }
        NodeUtil childNode = new NodeUtil(srcFunction,now,callLocation);
        now.addChild(childNode);
        now = childNode;
        return now;
    }

    /**
     * 获取以strNode为根节点的树中从起始函数到目标函数的所有调用路径
     *
     * @param strNode
     * @param srcFunction
     * @param dstFunction
     * @return List<List<Pair<调用行,Pair<调用者,被调用者>>>>
     */
    private List<List<Pair<String,Pair<String,String>>>> findPaths(NodeUtil strNode, String srcFunction, String dstFunction){
        List<List<Pair<String,Pair<String,String>>>> paths = new ArrayList<List<Pair<String, Pair<String, String>>>>();
        if(strNode == null){
            return paths;
        }

        if(strNode.getName().equals(srcFunction)){
            paths.addAll(findToDest(strNode,dstFunction));
        }
        else{
            for(NodeUtil r:strNode.getChildNodes()){
                paths.addAll(findPaths(r,srcFunction,dstFunction));
            }
        }
        return paths;
    }

    /**
     * 当当前树根节点为起始函数时，获取所有从该节点到目标函数节点的路径
     *
     * @param strNode
     * @param dstFunction
     * @return List<List<Pair<调用行,Pair<调用者,被调用者>>>>
     */
    private List<List<Pair<String,Pair<String,String>>>> findToDest(NodeUtil strNode ,String dstFunction){
        List<List<Pair<String,Pair<String,String>>>> paths = new ArrayList<List<Pair<String, Pair<String, String>>>>();
        if(strNode == null){
            return paths;
        }
        List<NodeUtil> dests = findDest(strNode,dstFunction);
        for(NodeUtil r:dests){
            paths.add(getOnePath(strNode, r));
        }
        return paths;
    }

    /**
     * 获取以strNode为根节点的树的所有子节点中为目标函数的节点
     *
     * @param strNode
     * @param dstFunction
     * @return
     */
    private List<NodeUtil> findDest(NodeUtil strNode ,String dstFunction){
        List<NodeUtil> dests = new ArrayList<NodeUtil>();
        if(strNode == null){
            return dests;
        }
        for(NodeUtil r:strNode.getChildNodes()){
            if(r.getName().equals(dstFunction)){
                dests.add(r);
            }
            dests.addAll(findDest(r,dstFunction));
        }
        return dests;
    }

    /**
     * 获取从起始节点到目标节点的调用信息
     *
     * @param str
     * @param dest
     * @return List<Pair<调用行,Pair<调用者,被调用者>>>
     */
    private List<Pair<String,Pair<String,String>>> getOnePath(NodeUtil str, NodeUtil dest){
        List<Pair<String,Pair<String,String>>> path_r = new ArrayList<Pair<String, Pair<String, String>>>();
        if(dest == null){
            return path_r;
        }
        NodeUtil now_t = dest;
        NodeUtil parent = dest.getParentNode();
        while(parent!=null&& !parent.getName().equals(str.getName())){
            path_r.add(new Pair<String, Pair<String, String>>(now_t.getCallLocation(),new Pair<String, String>(parent.getName(),now_t.getName())));
            now_t = parent;
            parent = now_t.getParentNode();
        }
        if(parent==null){
            path_r.clear();
        }
        else {
            Collections.reverse(path_r);
        }
        return path_r;
    }
}
