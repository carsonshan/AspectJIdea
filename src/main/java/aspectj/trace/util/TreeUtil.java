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
    public NodeUtil addRunTime(String srcFunction, String dstFunction, String callLocation, int runLineNum) {
        if (root == null) {
            root = new NodeUtil(srcFunction, null, "str", 0);
            now = root;
        }

        return runNext(srcFunction, dstFunction, callLocation, runLineNum);
    }


    public List<List<NodeUtil>> test(List<SearchInfoUtil> searchOrder) {
        List<List<NodeUtil>> searchResult = new ArrayList<List<NodeUtil>>();
        for (SearchInfoUtil r : searchOrder) {
            List<List<NodeUtil>> result = getPathFuzzyAndClearly(r);
            if (result == null || result.isEmpty()) {
                continue;
            }
            searchResult.addAll(result);
        }
        return searchResult;
    }

//    public List<Pair<NodeUtil, List<List<NodeUtil>>>> getCallPathOrdered(List<SearchInfoUtil> searchOrder) {
//        List<List<NodeUtil>> searchResult = new ArrayList<List<NodeUtil>>();
//        for (SearchInfoUtil r : searchOrder) {
//            List<List<NodeUtil>> result = getPathFuzzyAndClearly(r);
//            if (result == null || !result.isEmpty()) {
//                searchResult.clear();
//                return null;
//            }
//        }
//        //// TODO: 4/13/16
//    }

    /**
     * 获取从起始函数到目标函数且按顺序经过所有中间函数的所有调用路径
     *
     * @param srcFunction
     * @param dstFunction
     * @param path
     * @return
     */
    public List<List<NodeUtil>> getCallPathMultiNode(String srcFunction, String dstFunction, String[] path) {
        List<List<NodeUtil>> allPath = getCallPathsStrTODst(srcFunction, dstFunction);
        List<List<NodeUtil>> fitPath = new ArrayList<List<NodeUtil>>();
        for (List<NodeUtil> r : allPath) {
            int pathIndex = 0;
            int rSize_NL = r.size() - 1;
            for (int rIndex = 1; rIndex < rSize_NL; rIndex++) {
                if (pathIndex == path.length) {
                    break;
                }
                if (r.get(rIndex).getName().equals(path[pathIndex])) {
                    pathIndex++;
                }
            }
            if (pathIndex == path.length) {
                fitPath.add(r);
            }
        }
        return fitPath;
    }

    /**
     * 获取从起始函数到目标函数的所有调用路径
     *
     * @param srcFunction
     * @param dstFunction
     * @return
     */
    public List<List<NodeUtil>> getCallPathsStrTODst(String srcFunction, String dstFunction) {
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

    private NodeUtil runNext(String srcFunction, String dstFunction, String callLocation, int runLineNum) {
        while (now.getParentNode() != null) {
            if (!now.getName().equals(srcFunction)) {
                now = now.getParentNode();
            } else {
                break;
            }
        }
        NodeUtil childNode = new NodeUtil(dstFunction, now, callLocation, runLineNum);
        now = childNode;
        return now;
    }

    /**
     * 获取以strNode为根节点的树中从起始函数到目标函数的所有调用路径
     *
     * @param strNode
     * @param srcFunction
     * @param dstFunction
     * @return
     */
    private List<List<NodeUtil>> findPaths(NodeUtil strNode, String srcFunction, String dstFunction) {
        List<List<NodeUtil>> paths = new ArrayList<List<NodeUtil>>();
        if (strNode == null) {
            return paths;
        }

        if (strNode.getName().equals(srcFunction)) {
            paths.addAll(findToDest(strNode, dstFunction));
        }
        for (NodeUtil r : strNode.getChildNodes()) {
            paths.addAll(findPaths(r, srcFunction, dstFunction));
        }
        return paths;
    }

    /**
     * 当当前树根节点为起始函数时，获取所有从该节点到目标函数节点的路径
     *
     * @param strNode
     * @param dstFunction
     * @return
     */
    private List<List<NodeUtil>> findToDest(NodeUtil strNode, String dstFunction) {
        List<List<NodeUtil>> paths = new ArrayList<List<NodeUtil>>();
        if (strNode == null) {
            return paths;
        }
        List<NodeUtil> dests = findDest(strNode, dstFunction);
        for (NodeUtil r : dests) {
            List<NodeUtil> tmp = getOnePath(strNode, r);
            if (tmp != null && !tmp.isEmpty()) {
                paths.add(tmp);
            }
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
    private List<NodeUtil> findDest(NodeUtil strNode, String dstFunction) {
        List<NodeUtil> dests = new ArrayList<NodeUtil>();
        if (strNode == null) {
            return dests;
        }
        for (NodeUtil r : strNode.getChildNodes()) {
            if (r.getName().equals(dstFunction)) {
                dests.add(r);
            }
            dests.addAll(findDest(r, dstFunction));
        }
        return dests;
    }

    /**
     * 获取从起始节点到目标节点的调用信息
     *
     * @param str
     * @param dest
     * @return
     */
    private List<NodeUtil> getOnePath(NodeUtil str, NodeUtil dest) {
        List<NodeUtil> path_r = new ArrayList<NodeUtil>();
        if (dest == null) {
            return path_r;
        }
        NodeUtil now_t = dest;
        while (now_t != null && now_t != str) {
            path_r.add(now_t);
            now_t = now_t.getParentNode();
        }
        if (now_t == null) {
            path_r.clear();
        } else {
            path_r.add(now_t);
            Collections.reverse(path_r);
        }
        return path_r;
    }

    private List<List<NodeUtil>> getPathFuzzyAndClearly(SearchInfoUtil searchInfo) {
        if (searchInfo == null) {
            return null;
        }

        List<List<NodeUtil>> paths = new ArrayList<List<NodeUtil>>();
        final int pathSize = searchInfo.path.size();
        List<NodeUtil> strNodes = findDest(root, searchInfo.str);
        for (NodeUtil strNode : strNodes) {
            List<List<NodeUtil>> tmp = getPathFuzzyAndClearly(strNode, searchInfo.path);
            paths.addAll(tmp);
        }
        return paths;
    }

    /**
     * 根据起始节点和经过函数查询合适路径
     *
     * @param strNode
     * @param path
     * @return
     */
    private List<List<NodeUtil>> getPathFuzzyAndClearly(NodeUtil strNode, List<Pair<String, SearchInfoUtil.Method>> path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        //下一个查询队列
        List<Pair<String, SearchInfoUtil.Method>> nextPath = path.subList(1, path.size());
        //下一个目标函数
        Pair<String, SearchInfoUtil.Method> next = path.get(0);
        //查询结果
        List<List<NodeUtil>> result = new ArrayList<List<NodeUtil>>();
        //下一个目标函数节点
        List<List<NodeUtil>> res = new ArrayList<List<NodeUtil>>();
        switch (next.second) {
            case FUZZY: {
                res = findToDest(strNode, next.first);
                break;
            }
            case CLEAR: {
                for (NodeUtil child : strNode.getChildNodes()) {
                    if (child.getName().equals(next.first)) {
                        List<NodeUtil> tmp = new ArrayList<NodeUtil>();
                        tmp.add(strNode);
                        tmp.add(child);
                        res.add(tmp);
                    }
                }
            }
            default:
                break;
        }
        if (res == null && res.isEmpty()) {
            return null;
        }
        //查询队列不为空
        if (!nextPath.isEmpty()) {
            for (List<NodeUtil> r : res) {
                List<List<NodeUtil>> nextRes = getPathFuzzyAndClearly(r.get(r.size() - 1), nextPath);
                if (nextRes != null && !nextRes.isEmpty()) {
                    for (List<NodeUtil> c : nextRes) {
                        if (c != null && !c.isEmpty()) {
                            List<NodeUtil> tmp = new ArrayList<NodeUtil>();
                            tmp.addAll(r);
                            tmp.addAll(c.subList(1, c.size()));
                            result.add(tmp);
                        }
                    }
                }
            }
        }
        //查询队列为空
        else {
            result = res;
        }
        return result;
    }
}
