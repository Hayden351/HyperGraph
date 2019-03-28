package definition;

import generate.GenerateGraph;
import com.github.javaparser.JavaParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hayden Fields
 * TODO: where should i put util classes
 */
public class Utils
{

    public static <T, U> Map<T, U> generateMap (Object... elements)
    {
        Map<T, U> result = new HashMap<>();
        for (int i = 1; i < elements.length; i++)
            result.put((T) elements[i - 1], (U) elements[i]);
        return result;
    }
    //    public static Graph randomGraph(int numberOfVertices, int numberOfEdges)
    //    {
    //        Graph G = new Graph();
    //
    //        for (int i = 0; i < numberOfVertices; i++)
    //            G.addVertex(Integer.toString(i));
    //        for (int i = 0; i < numberOfEdges; i++)
    //            G.addEdge("", RandomUtils.randomSubSet(G.vertices), RandomUtils.randomMap(G.vertices));
    //        return G;
    //    }

    public static <T> boolean containsAll (Collection<T> a, Collection<T> b)
    {
        for (T element : b)
            if (!a.contains(element))
                return false;
        return true;
    }

    public static void main (String[] args) throws FileNotFoundException
    {
        GenerateGraph.randomGraph(5, 3);
        System.out.println(new File("").getAbsolutePath());
        GenerateGraph.fromNode(JavaParser.parse(new File("src/hypergraph/Graph.java")));
        Graph G = GenerateGraph.fromAST("C:\\Users\\DarkFight753\\OneDrive\\NBMisc\\HyperGraph\\src\\hypergraph\\Graph.java");
        //        System.out.println(G);
        //        System.out.println("\nV(G)\n");
        //        for (Vertex v : G.vertices)
        //            System.out.println(v);
        //        System.out.println("\nE(G)\n");
        //        for (Edge e : G.edges)
        //            System.out.println(e);
        for (Edge e : G.edges)
            if (!containsAll(G.vertices, e.orientations.keySet()))
                System.out.println(e);
    }

    public static <T> List<T> newSubList (List<T> sequence, int start, int end)
    {
        if (end < 0)
            return null;
        List<T> result = new ArrayList<>();
        for (int i = start; i < end; i++)
            result.add(sequence.get(i));
        return result;
    }

}
