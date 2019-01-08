package generate;

import collections.GenericTrie;
import collections.Trie;
import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseStart;
import com.github.javaparser.Providers;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import definition.Edge;
import definition.Graph;
import definition.GraphProperties;
import definition.Utils;
import definition.Vertex;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import regexproject.CharRange;
import regexproject.NonDeterministicStateMachine;
import regexproject.NonDeterministicStateMachine.Transition;
import regexproject.RegexNode;
import regexproject.RegexProject;
import regexproject.RegexProject.Regex;
import regexproject.RegexProject.RegexElement;
import regexproject.RegexProject.RegexOperation;
import regexproject.StateMachine;
import utils.random.RandomUtils;

/**
 * @author Hayden Fields
 */
public class GenerateGraph extends ExampleGraphs
{
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
    
    public static Graph fromTrie(Trie trie)
    {
        Graph G = new Graph();
        Map<String, Vertex> map = new HashMap<>();
        trie.preorder(sequence ->
        {
            Vertex v = G.addVertex(new Vertex(sequence));
            
            map.put(sequence, v);
            
            if (sequence.length() >= 1)
                G.addEdge(String.format("%c", sequence.charAt(sequence.length() - 1)),
                          new TreeSet<>(asList(map.get(sequence.substring(0, sequence.length() - 1)), v)),
                          Utils.<Vertex, Boolean>generateMap(v, true));
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
    
    public static RegexNode fromPostfixRegexToRegexAst (Regex regex)
    {
        return null;
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
    
    public static Graph fromNFA (NonDeterministicStateMachine nfa)
    {
        Graph G = new Graph();
        Map<Integer, Vertex> states = new HashMap<>();
        for (Transition t : nfa.transitions)
        {
            if (!states.containsKey(t.antecedent))
                states.put(t.antecedent, G.addVertex(new Vertex(String.format("%s", t.antecedent))));
            if (!states.containsKey(t.consequent))
                states.put(t.consequent, G.addVertex(new Vertex(String.format("%s", t.consequent))));
        }
        for (Transition t : nfa.transitions)
            G.addEdge
            (
                String.join(",", t.condition.stream().map(x -> x.toString()).collect(Collectors.toList())),
                asList(states.get(t.antecedent), states.get(t.consequent)),
                Utils.generateMap(states.get(t.consequent), true)
            );
        return G;
    }
    
    public static List<Graph> kruskalGraphs(Graph G)
    {
        List<Graph> graphs = new ArrayList<>(asList(G));
        List<Vertex> vertices = new ArrayList<>(G.vertices);
        List<Edge> edges = new ArrayList<>(G.edges);
        edges.sort((a,b) -> -(Integer.parseInt(a.label) - Integer.parseInt(b.label)));
        
        int pos = 0;
        while (edges.size() != vertices.size() - 1)
        {
            List<Edge> attempt = new ArrayList<>(edges);
            attempt.remove(pos);
            if (GraphProperties.isConnected(vertices, attempt))
            {
                edges.remove(pos);
                graphs.add(new Graph(vertices, new ArrayList<>(edges)));
            }
            else pos++;
        }
        return graphs;
    }
    
    public static Graph regexToNfa(RegexNode regex)
    {
        Graph G = new Graph();
        StateMachine nfa = regexToNfaAux(regex);
        
        System.out.println("");
        System.out.println(nfa);
        System.out.println("");
        
        for (StateMachine.Vertex vertex : nfa.getStates())
            G.addVertex(String.format("%d", vertex.state));
        for (AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Integer, List<CharRange>>, Integer> transition : nfa.getTransitions())
        {
            Vertex v = G.findFirstVertexByLabel(String.format("%d", transition.getKey().getKey()));
            Vertex u = G.findFirstVertexByLabel(String.format("%d", transition.getValue()));
            G.addEdge(String.format("%s", transition.getKey().getValue()), asList(v, u), Utils.generateMap(u, true));
        }
        
        return G;
    }
    
    public static StateMachine regexToNfaAux(RegexNode regex)
    {
        RegexElement element = regex.element;
        System.out.println(element.getClass());
        if (element instanceof RegexProject.Concatenation)
        {
            StateMachine lValue = regexToNfaAux (regex.children.get(1));
            StateMachine rValue = regexToNfaAux (regex.children.get(0)).upto(lValue.largestStateValue() + 1);
            
            System.out.println("");
            System.out.print(lValue);
            System.out.println("   *  concat  *  ");
            System.out.print(rValue);
            
            
            for (StateMachine.Vertex vertex : lValue.getFinalStates())
                lValue.addTransition(vertex.state, '\u03B5', rValue.startState.state);
            lValue.finalStates = rValue.finalStates;
            for (AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Integer, List<CharRange>>, Integer> transition : rValue.getTransitions())
                lValue.addTransition(transition.getKey().getKey(), transition.getKey().getValue(), transition.getValue());
            System.out.println("-->");
            System.out.println(lValue);
            return lValue;
        }
        else if (element instanceof RegexProject.Intersection)
            ;
        else if (element instanceof RegexProject.MatchSome)
        {
            RegexProject.MatchSome matchSome = (RegexProject.MatchSome)element;
            StateMachine dfa = new StateMachine();
            dfa.setStartState(0);
            dfa.addTransition(0, matchSome.charRanges, 1);
            dfa.addFinalState(1);
            
            System.out.println(dfa);

            return dfa;
        }
        else if (element instanceof RegexProject.Negation)
        {
            RegexProject.Negation negation = (RegexProject.Negation)element;
            StateMachine dfa = regexToNfaAux (regex.children.get(0));
            // TODO: resolve overlapping state conflict
            Set<StateMachine.Vertex> universe = dfa.getStates();
            Set<Integer> inverse = new HashSet<>();

            for (StateMachine.Vertex state : universe)
                if (!dfa.finalStates.contains(state.state))
                    inverse.add(state.state);
            dfa.finalStates = inverse;

            return dfa;
        }
        else if (element instanceof RegexProject.Repitition)
        {
            StateMachine lValue = regexToNfaAux (regex.children.get(0));
            
            for (StateMachine.Vertex vertex : lValue.getFinalStates())
                lValue.addTransition(vertex.state, '\u03B5', lValue.startState.state);
            System.out.println("Repitition");
            System.out.println(lValue);
            return lValue;
        }
        else if (element instanceof RegexProject.StringLiteral)
        {
            RegexProject.StringLiteral stringLiteral = (RegexProject.StringLiteral)element;
            StateMachine dfa = new StateMachine();

            dfa.setStartState(0);
            int i = 0;
            for (; i < stringLiteral.literal.length(); i++)
                dfa.addTransition(i, stringLiteral.literal.charAt(i), i + 1);
            dfa.addFinalState(i);
            
            System.out.println(dfa);

            return dfa;
        }
        else if (element instanceof RegexProject.Union)   
        {
            StateMachine result = new StateMachine();
            result.setStartState(0);
            StateMachine lValue = regexToNfaAux (regex.children.get(0)).upto(1);

            // TODO: used to be after but should be before probably
            result.addTransition(0, '\u03B5', lValue.startState.state);
            for (AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Integer, List<CharRange>>, Integer> transition : lValue.getTransitions())
                result.addTransition(transition.getKey().getKey(), transition.getKey().getValue(), transition.getValue());
            StateMachine rValue = regexToNfaAux (regex.children.get(1)).upto(lValue.largestStateValue() + 1);
            result.addTransition(0, '\u03B5', rValue.startState.state);
            for (AbstractMap.SimpleEntry<AbstractMap.SimpleEntry<Integer, List<CharRange>>, Integer> transition : rValue.getTransitions())
                result.addTransition(transition.getKey().getKey(), transition.getKey().getValue(), transition.getValue());
            
            return result;
        }
        else 
            throw new IllegalArgumentException(String.format("Must add another case to %s.%s", StateMachine.class, "regexToNfaAux"));
        return null;
    }
    
    public static List<Graph> regexToAstAndDfa(Regex regex)
    {
        List<Graph> graphs = new ArrayList<>();
        
        RegexNode tree = RegexNode.fromPostFixExpression(regex);
        
        for (RegexNode v : tree)
            convertToDfaToGraph(v);
        return graphs;
    }

    private static void convertToDfaToGraph (RegexNode v)
    {
        
    }
    
    public static Graph undirectedUnlabeledGraphFrom(String str)
    {
        return aux(str, x -> asList(x.split("\n")));
    }
    
    public static Graph undirectedUnlabeledGraphFromFile(String file)
    {
        return undirectedUnlabeledGraphFromFile(new File(file));
    }
    
    public static Graph undirectedUnlabeledGraphFromFile(File file)
    {
        return aux(file, f ->
        {
            try (BufferedReader in = new BufferedReader(new FileReader(f)))
                { return in.lines().collect(Collectors.toList()); } 
            catch (Exception ex)
                { return null; } 
        } );
    }
    
    
    public static <T> Graph aux(T source, Function<T, List<String>> toLines)
    {
        Graph G = new Graph();
        List<String> lines = toLines.apply(source);
        Vertex[] vertices = new Vertex[lines.size()];
        
        for (int i =0; i < lines.size(); i++)
        {
            String[] elements = lines.get(i).split("\\s+");
            for (int j = 0; j < elements.length; j++)
            {
                if ("1".equals(elements[j]))
                    G.addEdge(asList(vertices[i], vertices[j]));
            }
        }
        return G;
    }
    public static Graph undirectedLabeledGraphFromFile(String file)
    {
        return undirectedLabeledGraphFromFile(new File(file));
    }
    
    public static Graph undirectedLabeledGraphFromFile(File file)
    {
        Graph G = new Graph();
        
        try (BufferedReader in = new BufferedReader(new FileReader(file)))
        {
            String[] labels = in.readLine().split("\\s+");
            Vertex[] vertices = new Vertex[labels.length];
            for (int i = 0; i < labels.length; i++)
                vertices[i] = G.addVertex(new Vertex(labels[i]));
            for (int i = 0; i < labels.length; i++)
            {
                String[] edgeLabels = in.readLine().split("\\s+");
                for (int j = 0; j < labels.length; j++)
                {
                    if ("_".equals(edgeLabels[j])) // no edge
                        ;
                    else if ("%".equals(edgeLabels[j])) // unlabeled edge
                        G.addEdge(asList(vertices[i], vertices[j]), Utils.generateMap(vertices[j], true)); 
                    else  // labeled edge
                        G.addEdge(edgeLabels[j], asList(vertices[i], vertices[j]), Utils.generateMap(vertices[j], true));
                }
            }
        }
        catch (Exception e)
        {
            ; // exceptional
        }
        return G;
    }
}
