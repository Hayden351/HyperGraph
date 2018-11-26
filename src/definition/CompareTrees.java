package definition;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * @author Hayden Fields
 */
public class CompareTrees
{
    public static void main (String[] args)
    {
        
        Graph tree = GenerateGraph.tree();
        
        GraphProperties.treeLevelOrderTraversal(tree).forEach(System.out::println);
        System.out.println("");
        Graph subTree = GenerateGraph.subTree();
        System.out.println("");
        
//        GraphProperties.options(tree).forEach(System.out::println);
        
        GraphProperties.options(GenerateGraph.wikiTrie()).forEach(System.out::println);
        
        List<Vertex> instances = findAllIn(subTree, tree);
        
        System.out.println(instances);
    }

    // basic inorder traversal of tree
    // at each node try to recursively match
    // try all combinations
    private static List<Vertex> findAllIn (Graph subTree, Graph tree)
    {
        List<Vertex> result = new ArrayList<>();
        // assume it is a rooted tree and has 1 root
        Vertex root = GraphProperties.findRoots(tree).iterator().next();
        
        Stack<Vertex> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty())
        {
            Vertex v = stack.pop();
            if (matches(subTree, tree, v))
                result.add(v);
            for (Vertex u : GraphProperties.getReachable(tree, v))
                stack.push(u);
        }
        return result;
    }
    
    private static boolean treesEqual(Graph leftTree, Graph rightTree)
    {
        return true;
    }

    private static boolean matches (Graph subTree, Graph tree, Vertex v)
    {
        Vertex root = GraphProperties.findRoots(subTree).iterator().next();
        if (!v.label.equals(root.label))
            return false;
        // level order traversal
        return true;
    }
}
