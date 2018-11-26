package definition;

import collections.GenericTrie;
import collections.Trie;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import regexproject.RegexProject;
import regexproject.RegexProject.Regex;
import regexproject.RegexProject.RegexElement;
import regexproject.RegexProject.RegexOperation;
import utils.random.RandomUtils;

/**
 * @author Hayden Fields
 */
public class GenerateGraph
{
    public static Graph fullyConnectedExample ()
    {
        Graph G = new Graph();
        for (int i = 0; i < 8; i++)
            G.addVertex(String.format("%d", i));
        for (int i = 0; i < 20; i++)
            G.addEdge(String.format("%d", i), G.vertices);
        return G;
    }
    
    public static Graph fullyConnected (Integer vertices, Integer edges)
    {
        Graph G = new Graph();
        for (int i = 0; i < vertices; i++)
            G.addVertex(String.format("%d", i));
        for (int i = 0; i < edges; i++)
            G.addEdge(String.format("%d", i), G.vertices);
        return G;
    }
    
    public static Graph randomGraph (Integer numberOfVertices, Integer numberOfEdges)
    {
        Graph G = new Graph();
        for (int i = 0; i < numberOfVertices; i++)
            G.addVertex(Integer.toString(i));
         for (int i = 0; i < numberOfEdges; i++)
         {
            Set<Vertex> vertices = new TreeSet<>();
            
            while (vertices.size() < 2)
                vertices = RandomUtils.randomSubSet(G.vertices, .2);
            G.addEdge("", vertices, RandomUtils.randomMap(G.vertices));
         }
        
        return G;
    }
    
    public static Graph fromAST (String filePath)
    {
        try
        {
            return fromNode(JavaParser.parse(new File(filePath)));
        }
        catch (FileNotFoundException fnfe)
        {
            return null;
        }
    }
    
    public static Graph fromNode (Node cu)
    {
        Map<Node, Vertex> map = new HashMap<>();
        Graph G = new Graph();
        
        cu.walk(node -> 
        {   
            String vertexLabel = node.getClass().getName();
            vertexLabel = vertexLabel.substring(vertexLabel.lastIndexOf('.') + 1);
            Vertex v = G.addVertex(new Vertex(vertexLabel));
            // make 1-1 correspondence between ast and the graph we are constructing
            map.put(node, v);
            
            node.getParentNode().ifPresent( parent ->
                G.addEdge("", new TreeSet<>(asList(map.get(parent), v)),
                          Utils.<Vertex, Boolean>generateMap(v, true)));
        });
        
        return G;
    }
    
    public static Graph fromExpr (String expr)
    {
        return fromNode(new JavaParser().parse(ParseStart.EXPRESSION, Providers.provider(expr)).getResult().get());
    }
    
    
    public static void main (String[] args)
    {
        wikiTrie();
    }
    public static Graph fromTrie(Trie trie)
    {
        Graph G = new Graph();
        Map<String, Vertex> map = new HashMap<>();
        trie.preorder(sequence ->
        {
            Vertex v = G.addVertex(new Vertex(sequence.toString()));
            
            map.put(sequence, v);
            
            if (sequence.length() >= 1)
                G.addEdge(""+sequence.charAt(sequence.length() - 1), new TreeSet<>(asList(map.get(sequence.substring(0, sequence.length() - 1)), v)), Utils.<Vertex, Boolean>generateMap(v, true));
        });
        return G;
    }
    
    public static <T> Graph fromTrie(GenericTrie<T> trie)
    {
        Graph G = new Graph();
        Map<List<T>, Vertex> map = new HashMap<>();
        trie.preorder(sequence ->
        {
            Vertex v = G.addVertex(new Vertex(sequence.toString()));
            
            map.put(new ArrayList<>(sequence), v);
            
            // TODO: kind of a hack to identify root
            List<T> parentSequence = Utils.newSubList(sequence,0, sequence.size() - 1);
            if (parentSequence != null)
                G.addEdge("", new TreeSet<>(asList(map.get(parentSequence), v)), Utils.<Vertex, Boolean>generateMap(v, true));
        });
        return G;
    }
    
    public static Graph wikiTrie ()
    {
        Trie trie = new Trie();
        trie.add("A");
        trie.add("to");
        trie.add("tea");
        trie.add("ted");
        trie.add("ten");
        trie.add("i");
        trie.add("in");
        trie.add("inn");
        return fromTrie(trie);
    }
    
    public static Graph wikiGenericTrie ()
    {
        GenericTrie<Character> wikiTrie = new GenericTrie<>();
        wikiTrie.add(asList('A'));
        wikiTrie.add(asList('t', 'o'));
        wikiTrie.add(asList('t', 'e','a'));
        wikiTrie.add(asList('t', 'e','d'));
        wikiTrie.add(asList('t', 'e','n'));
        wikiTrie.add(asList('i'));
        wikiTrie.add(asList('i', 'n'));
        wikiTrie.add(asList('i', 'n','n'));
        
        return fromTrie(wikiTrie);
    }
    
    public static Graph exampleRegex ()
    {
        List<RegexElement> regex = new ArrayList<>();
        
        regex.add(new RegexProject.StringLiteral("0"));
        regex.add(new RegexProject.MatchSome(ch -> '1' <= ch && ch <= '9'));
        regex.add(new RegexProject.MatchSome(ch -> '0' <= ch && ch <= '9'));
        regex.add(new RegexProject.Repitition(0));
        regex.add(new RegexProject.Concatenation());
        regex.add(new RegexProject.Union());
        
        return fromPostfixRegex(new Regex(regex));
    }
    
    public static Graph fromPostfixRegex (Regex regex)
    {
        Graph G = new Graph();
        Stack<RegexElement> stack = new Stack<>();
        Stack<Vertex> tree = new Stack<>();
        for (RegexElement re : regex.regex)
            if (re.isOperation())
            {
                RegexOperation regOp = (RegexOperation)re;
                Vertex op = G.addVertex(new Vertex(regOp.getClass().getSimpleName()));
                for (int i = 0; i < regOp.numberOfOperands(); i++)
                {
                    Vertex v =  G.addVertex(tree.pop());
                    G.addEdge("", new HashSet<>(asList(op, v)), Utils.generateMap(v, true));
                }
                
                stack.push(regOp);
                tree.push(op);
            }
            else
            {
                stack.push(re);
                tree.push(new Vertex(re.getClass().getSimpleName()));
            }
        return G;
    }
    
    public static Graph tree()
    {
        Graph G = new Graph();
        
        Vertex root = G.addVertex(new Vertex("R"));
        Vertex A =    G.addVertex(new Vertex("A"));
        Vertex B =    G.addVertex(new Vertex("B"));
        Vertex A1 =   G.addVertex(new Vertex("A"));
        Vertex B1 =   G.addVertex(new Vertex("B"));
        
        G.addEdge("", asList(root, A), Utils.<Vertex, Boolean>generateMap(A, true));
        G.addEdge("", asList(A, B)   , Utils.<Vertex, Boolean>generateMap(B, true));
        G.addEdge("", asList(A, A1)  , Utils.<Vertex, Boolean>generateMap(A1, true));
        G.addEdge("", asList(A1, B1) , Utils.<Vertex, Boolean>generateMap(B1, true));
        return G;
    }
    public static Graph subTree()
    {
        Graph G = new Graph();
        
        
        Vertex A =    G.addVertex(new Vertex("A"));
        Vertex B =    G.addVertex(new Vertex("B"));
        Vertex A1 =   G.addVertex(new Vertex("A"));
        Vertex B1 =   G.addVertex(new Vertex("B"));
        
        G.addEdge("", asList(A, B)   , Utils.<Vertex, Boolean>generateMap(B, true));
        G.addEdge("", asList(A, A1)  , Utils.<Vertex, Boolean>generateMap(A1, true));
        G.addEdge("", asList(A1, B1) , Utils.<Vertex, Boolean>generateMap(B1, true));
        return G;
    }
    
    
}
