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


    /**
     * 按照要求查询条件单行查询所有查询
     *
     * @param searchOrder
     * @return 外层list：不同行查询；第三层list：名称相同但位置不同的根节点；第二层list：不同路径；内层list：路径上节点
     */
    public List<List<List<List<NodeUtil>>>> getCallPathOrdered(List<SearchInfoUtil> searchOrder) {
        List<List<List<List<NodeUtil>>>> searchResult = new ArrayList<List<List<List<NodeUtil>>>>();
        for (SearchInfoUtil r : searchOrder) {
            List<List<List<NodeUtil>>> result = getPathFuzzyAndClearly(r);
            if (result == null || result.isEmpty()) {
                return null;
            }
            searchResult.add(result);
        }
        return searchResult;
    }

    /**
     * 获取符合条件的查询树集合
     *
     * @param searchOrder
     * @return Set<Pair<树根节点, 所有调用路径>>
     */
    public Set<Pair<NodeUtil, List<List<NodeUtil>>>> getCallPathTreeOrdered(List<SearchInfoUtil> searchOrder) {
        Set<Pair<NodeUtil, List<List<NodeUtil>>>> result = new HashSet<Pair<NodeUtil, List<List<NodeUtil>>>>();
        //外层list：不同行查询；第三层list：名称相同但位置不同的根节点；第二层list：不同路径；内层list：路径上节点
        List<List<List<List<NodeUtil>>>> searchResult = getCallPathOrdered(searchOrder);
        if (searchResult == null || searchResult.isEmpty()) {
            return result;
        }
        //将名称相同但位置不同的根节点合并在一个list中
        //外层list：不同行查询；第二层list：不同路径；内层list：路径上节点
        List<List<List<NodeUtil>>> resultMerged = new ArrayList<List<List<NodeUtil>>>();
        for (List<List<List<NodeUtil>>> r : searchResult) {
            List<List<NodeUtil>> tmp_1 = new ArrayList<List<NodeUtil>>();
            for (List<List<NodeUtil>> c : r) {
                tmp_1.addAll(c);
            }
            resultMerged.add(tmp_1);
        }
        //排列组合单行结果，并获取其根节点
        Stack<List<NodeUtil>> toSearch = new Stack<List<NodeUtil>>();
        List<NodeUtil> callOrder = new ArrayList<NodeUtil>();
        treeToList(root, callOrder);
        getAllCallTree(toSearch, resultMerged, result, callOrder);
//        List<List<NodeUtil>> toSearch = new ArrayList<List<NodeUtil>>();
        //第三层list：名称相同但位置不同的根节点；第二层list：不同路径；内层list：路径上节点
//        for (List<List<List<NodeUtil>>> r : searchResult) {
//            toSearch.add(new ArrayList<NodeUtil>(r.get(0).get(0)));
//        }
//        Pair<NodeUtil, List<List<NodeUtil>>> tmp = getOneCallPathTree(toSearch);
//        if (tmp != null) {
//            result.add(tmp);
//        }
        return result;
    }

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

    /**
     * 根据待查询信息（起始节点名和经过函数）查询合适路径
     *
     * @param searchInfo
     * @return 外层list：名称相同但位置不同的根节点；第二层list：不同路径；内层list：路径上节点
     */
    private List<List<List<NodeUtil>>> getPathFuzzyAndClearly(SearchInfoUtil searchInfo) {
        if (searchInfo == null) {
            return null;
        }

        List<List<List<NodeUtil>>> paths = new ArrayList<List<List<NodeUtil>>>();
        final int pathSize = searchInfo.path.size();
        List<NodeUtil> strNodes = findDest(root, searchInfo.str);
        for (NodeUtil strNode : strNodes) {
            List<List<NodeUtil>> tmp = getPathFuzzyAndClearly(strNode, searchInfo.path);
            if (tmp == null || tmp.isEmpty())
                continue;
            paths.add(tmp);
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

    /**
     * 根据目标调用序列获取符合目标调用序列的最小子树根节点
     *
     * @param toSearch
     * @return 根节点和目标调用序列
     */
    private Pair<NodeUtil, List<List<NodeUtil>>> getOneCallPathTree(List<List<NodeUtil>> toSearch, List<NodeUtil> callOrder) {
        if (toSearch == null) {
            return null;
        }
        if (toSearch.size() == 1) {
            return new Pair<NodeUtil, List<List<NodeUtil>>>(toSearch.get(0).get(0), toSearch);
        }
        if (!judCallOrder(toSearch, callOrder)) {
            return null;
        }
        List<NodeUtil> strNodes = new ArrayList<NodeUtil>();
        for (List<NodeUtil> r : toSearch) {
            strNodes.add(r.get(0));
        }
        Set<NodeUtil> strNodes_t = new HashSet<NodeUtil>();
        strNodes_t.addAll(strNodes);
        strNodes.clear();
        strNodes.addAll(strNodes_t);
        Map<NodeUtil, Integer> containNum = new HashMap<NodeUtil, Integer>();
        contains(root, strNodes, containNum);
        NodeUtil root = findRoot(this.root, strNodes, containNum);
        if (root != null) {
//            Set<NodeUtil> runIn = new HashSet<NodeUtil>();
//            for (List<NodeUtil> r : toSearch) {
//                runIn.addAll(r);
//            }
//            List<NodeUtil> nodes = new ArrayList<NodeUtil>();
//            nodes.addAll(runIn);
            return new Pair<NodeUtil, List<List<NodeUtil>>>(root, toSearch);
        }
        return null;
    }

    /**
     * 在以root为根节点的树中根据目标节点获取包含目标节点的最小子树根节点
     *
     * @param root
     * @param strNodes
     * @param containNum 每个节点包含的目标节点的个数
     * @return
     */
    private NodeUtil findRoot(NodeUtil root, List<NodeUtil> strNodes, Map<NodeUtil, Integer> containNum) {
        if (containAll(root, strNodes, containNum)) {
            for (NodeUtil r : root.getChildNodes()) {
                if (containAll(r, strNodes, containNum)) {
                    return findRoot(r, strNodes, containNum);
                }
            }
            return root;
        }
        return null;
    }

    /**
     * 判定该root节点为根节点的子树是否包含所有目标节点
     *
     * @param root
     * @param strNodes
     * @param containNum
     * @return
     */
    private boolean containAll(NodeUtil root, List<NodeUtil> strNodes, Map<NodeUtil, Integer> containNum) {
        return containNum.get(root) == strNodes.size();
    }

    /**
     * 统计以root为根节点的树的所有子节点为根的树中包含目标节点的个数，并存入containNum
     *
     * @param root
     * @param strNodes
     * @param containNum
     */
    private void contains(NodeUtil root, List<NodeUtil> strNodes, Map<NodeUtil, Integer> containNum) {
        int result = 0;
        if (strNodes.contains(root)) {
            result++;
        }
        for (NodeUtil r : root.getChildNodes()) {
            contains(r, strNodes, containNum);
            result += containNum.get(r);
        }
        containNum.put(root, result);
    }

    private void treeToList(NodeUtil root, List<NodeUtil> toList) {
        if (root == null) return;
        toList.add(root);
        for (NodeUtil r : root.getChildNodes()) {
            treeToList(r, toList);
            toList.add(root);
        }
    }

    void getAllCallTree(Stack<List<NodeUtil>> toSearch, List<List<List<NodeUtil>>> resultMerged,
                        Set<Pair<NodeUtil, List<List<NodeUtil>>>> result, List<NodeUtil> callOrder) {
        if (toSearch.size() == resultMerged.size()) {
            List<List<NodeUtil>> searchInfo = new ArrayList<List<NodeUtil>>();
            searchInfo.addAll(toSearch);
            Pair<NodeUtil, List<List<NodeUtil>>> tmp = getOneCallPathTree(searchInfo, callOrder);
            if (tmp != null) {
                result.add(tmp);
            }
        } else {
            for (List<NodeUtil> r : resultMerged.get(toSearch.size())) {
                toSearch.push(r);
                getAllCallTree(toSearch, resultMerged, result, callOrder);
                toSearch.pop();
            }
        }
    }

    private boolean judCallOrder(List<List<NodeUtil>> toSearch, List<NodeUtil> callOrder) {
        int callOrder_size = callOrder.size();
        int toSearch_size = toSearch.size();
        int iter_call = 0;
        int iter_search = 0;
        for (; iter_call < callOrder_size; ++iter_call) {
            if (iter_search == toSearch_size) {
                break;
            } else {
                List<NodeUtil> searchTmp = toSearch.get(iter_search);
                List<NodeUtil> callTmp = callOrder.subList(iter_call,
                        searchTmp.size() + iter_call > callOrder_size
                                ? callOrder_size
                                : searchTmp.size() + iter_call);
                if (searchTmp.equals(callTmp)) {
                    iter_search += 1;
                    iter_call += searchTmp.size() - 2;
                }
            }
        }
        if (iter_search == toSearch_size) {
            return true;
        }
        return false;
    }
}
