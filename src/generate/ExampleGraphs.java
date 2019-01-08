package generate;

import collections.GenericTrie;
import collections.Trie;
import definition.Graph;
import definition.Utils;
import definition.Vertex;
import static generate.GenerateGraph.fromNFA;
import static generate.GenerateGraph.fromPostfixRegex;
import static generate.GenerateGraph.fromTrie;
import static generate.GenerateGraph.kruskalGraphs;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.List;
import regexproject.NonDeterministicStateMachine;
import regexproject.RegexNode;
import regexproject.RegexProject;

/**
 * @author Hayden Fields
 */
public class ExampleGraphs
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
        List<RegexProject.RegexElement> regex = new ArrayList<>();
        
        regex.add(new RegexProject.StringLiteral("0"));
        regex.add(new RegexProject.MatchSome('1', '9'));
        regex.add(new RegexProject.MatchSome('0', '9'));
        regex.add(new RegexProject.Repitition(0));
        regex.add(new RegexProject.Concatenation());
        regex.add(new RegexProject.Union());
        
        return fromPostfixRegex(new RegexProject.Regex(regex));
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
    
    public static List<Graph> minimumSpanningTreeExample()
    {
        Graph G = new Graph();
        Vertex A = new Vertex("A");
        Vertex B = new Vertex("B");
        Vertex C = new Vertex("C");
        Vertex D = new Vertex("D");
        Vertex E = new Vertex("E");
        Vertex F = new Vertex("F");
        G.addVertex(A);
        G.addVertex(B);
        G.addVertex(C);
        G.addVertex(D);
        G.addVertex(E);
        G.addVertex(F);
        G.addEdge("5", new ArrayList<>(asList(A, B)));
        G.addEdge("10", new ArrayList<>(asList(A, C)));
        G.addEdge("1", new ArrayList<>(asList(A, E)));
        
        G.addEdge("2", new ArrayList<>(asList(B, C)));
        G.addEdge("4", new ArrayList<>(asList(B, F)));
        G.addEdge("9", new ArrayList<>(asList(B, E)));
        
        G.addEdge("5", new ArrayList<>(asList(C, D)));
        
        G.addEdge("3", new ArrayList<>(asList(D, F)));
        
        G.addEdge("7", new ArrayList<>(asList(E, F)));
        
        return kruskalGraphs(G);
    }
    
    public static Graph exampleNFA()
    {
        NonDeterministicStateMachine nfa = new NonDeterministicStateMachine();
         
         nfa.transitions.add(new NonDeterministicStateMachine.Transition(0, '0', 1));
         nfa.transitions.add(new NonDeterministicStateMachine.Transition(0, '1', '9', 2));
         nfa.transitions.add(new NonDeterministicStateMachine.Transition(2, '0', '9', 2));
         nfa.start = 0;
         nfa.finalStates.addAll(asList(1, 2));
        return fromNFA(nfa);
    }
    
    public static Graph exampleRegexToNfa()
    {
        List<RegexProject.RegexElement> regex = new ArrayList<>();
        
        regex.add(new RegexProject.StringLiteral("0"));
        regex.add(new RegexProject.MatchSome('1', '9'));
        regex.add(new RegexProject.MatchSome('0', '9'));
        regex.add(new RegexProject.Repitition(0));
        regex.add(new RegexProject.Concatenation());
        regex.add(new RegexProject.Union());
        
        RegexNode result = RegexNode.fromPostFixExpression(regex);
        
        RegexNode.preOrderTraversal(System.out, result);
        
        Graph G = GenerateGraph.regexToNfa(result);
        System.out.printf("number of edges : %s\n", G.edges.size());
        return G;
    }
    
    public static List<Graph> dfaNfa()
    {
        return new ArrayList<>(asList(exampleNFA(), exampleRegexToNfa()));
    }
    
    public static Graph exampleMerge()
    {
        Graph G = new Graph();
        Vertex predIn1 = new Vertex("");
        Vertex predIn2 = new Vertex("");
        
        Vertex in = new Vertex(""); G.addEdge("d", new ArrayList<>(asList(in)), Utils.generateMap(in, true));
        G.addEdge("a", new ArrayList<>(asList(predIn1, in)), Utils.generateMap(in, true));
        G.addEdge("b", new ArrayList<>(asList(predIn2, in)), Utils.generateMap(in, true));
        Vertex out = new Vertex(""); G.addEdge("e", new ArrayList<>(asList(out)));
        G.addEdge("\u03B5", new ArrayList<>(asList(in, out)), Utils.generateMap(out, true));
        
        Vertex consOut1 = new Vertex("");
        Vertex consOut2 = new Vertex("");
        G.addEdge("b", new ArrayList<>(asList(out, consOut1)), Utils.generateMap(consOut1, true));
        G.addEdge("c", new ArrayList<>(asList(out, consOut2)), Utils.generateMap(consOut2, true));
        
        G.addAllVertices(new ArrayList<>(asList(predIn1, predIn2, in, out, consOut1, consOut2)));
        return G;
    }
    public static Graph from() throws IOException
    {
//        throw new NullPointerException(new File("").getCanonicalPath());
        return GenerateGraph.undirectedLabeledGraphFromFile("testFile");
    }
}
