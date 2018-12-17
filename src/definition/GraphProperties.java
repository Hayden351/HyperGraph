package definition;

import java.util.ArrayList;
import static java.util.Arrays.asList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * @author Hayden Fields
 */
public class GraphProperties
{
    public static boolean isSelfLoop (Edge e)
    {
        return e.vertices.keySet().size() == 1;
    }
    
    // two vertices are neighbors if they are connected by an edge
    public static Set<Vertex> getNeighborsInclusive (Graph G, int vid)
    {
        Set<Vertex> neighbors = new HashSet<>();
        Vertex v = G.getVertexById(vid);
        for (Edge e : G.edges)
            if (e.vertices.keySet().contains(v))
                neighbors.addAll(e.vertices.keySet());
       return neighbors;
    }
    public static Set<Vertex> getNeighbors (Graph G, int vid)
    {
        Set<Vertex> neighbors = new HashSet<>();
        Vertex v = G.getVertexById(vid);
        for (Edge e : G.edges)
            if (e.vertices.keySet().contains(v))
                neighbors.addAll(e.vertices.keySet());
        neighbors.remove(v);
        return neighbors;
    }
    public static Set<Vertex> getNeighborsInclusive (Graph G, Vertex v)
    {
        Set<Vertex> neighbors = new HashSet<>();
        for (Edge e : G.edges)
            if (e.vertices.keySet().contains(v))
                neighbors.addAll(e.vertices.keySet());
       return neighbors;
    }
     
    public static Set<Vertex> getNeighbors (Graph G, Vertex v)
    {
        return getNeighbors(G.edges, v);
    }
    
    public static Set<Vertex> getNeighbors (Iterable<Edge> edges, Vertex v)
    {
        Set<Vertex> neighbors = new HashSet<>();
        for (Edge e : edges)
            if (e.vertices.keySet().contains(v))
                neighbors.addAll(e.vertices.keySet());
        neighbors.remove(v);
        return neighbors;
    }
    
    
    public static Set<Vertex> getReachable(Graph G, Vertex v)
    {
        Set<Vertex> neighbors = new HashSet<>();
        for (Edge e : G.edges)
            // cannot be oriented towards v (to be consistent with a 2-hypergraph)
            if (e.vertices.keySet().contains(v) && !e.vertices.get(v))
                for (Vertex u : e.vertices())
                    if (e.vertices.get(u)) // is oriented towards u
                        neighbors.add(u);
        return neighbors;
    }
    
    public static Set<Vertex> findRoots(Graph G)
    {
        Set<Vertex> verticesWithNoParents = new HashSet<>(G.vertices);
        for (Iterator<Vertex> it = verticesWithNoParents.iterator(); it.hasNext();)
        {
            Vertex v = it.next();
            for (Edge e : G.edges)
                if (e.orientedTowards(v))
                {
                    it.remove();
                    break;
                }
        }
        return verticesWithNoParents;
    }
    
    public static List<Vertex> depthFirstSearch (Graph G, Vertex src)
    {
        Map<Vertex, Boolean> visited = new HashMap<>();
        for (Vertex v : G.vertices)
            visited.put(v, Boolean.FALSE);
        Queue<Vertex> vertexQueue = new LinkedList<>();
        List<Vertex> visitOrder = new ArrayList<>();
        
        vertexQueue.add(src);
        visitOrder.add(src);
        visited.put(src, Boolean.TRUE);
        
        while (!vertexQueue.isEmpty())
        {
            Vertex v = vertexQueue.remove();
            visitOrder.add(v);
            for (Vertex u : getNeighbors(G, v))
            {
                if (!visited.get(u))
                {
                    visited.put(u, Boolean.TRUE);
                    vertexQueue.add(u);
                }
            }
        }
        return visitOrder;
    }

    public static Set<Vertex> getReachableIncusive (Graph tree, Vertex v)
    {
        Set<Vertex> vertices = getReachable(tree, v);
        vertices.add(v);
        return vertices;
    }
    
    public static List<Set<Vertex>> treeLevelOrderTraversal(Graph G)
    {
        Vertex root = GraphProperties.findRoots(G).iterator().next();
        List<Set<Vertex>> result = new ArrayList<>(asList(new HashSet<>(asList(root))));
        
        for (int i = 0; ; i++)
        {
            Set<Vertex> layer = new HashSet<>();
            // get next layer
            for (Vertex v : result.get(i))
                layer.addAll(GraphProperties.getReachable(G, v));
            if (layer.isEmpty())
                break;
            else
                result.add(layer);
        }
        return result;
    }
    
    public static List<List<Integer>> options(Graph G)
    {
        Vertex root = GraphProperties.findRoots(G).iterator().next();
        List<List<Integer>> result = new ArrayList<>(/*asList(new ArrayList<>(asList(GraphProperties.getReachable(G, root).size())))*/);
        Set<Vertex>[] layers = new Set[2];
        layers[0] = new HashSet<>(asList(root));
//        layers[1] = new HashSet<>();
        for (int i = 0; ; i++)
        {
            layers[~i & 1] = new HashSet<>();
            // get next layer
            List<Integer> layerResult = new ArrayList<>();
            for (Vertex v : layers[i & 1])
            {
                Set<Vertex> reachableVertices = GraphProperties.getReachable(G, v);
                layers[~i & 1].addAll(reachableVertices);
                layerResult.add(reachableVertices.size());
                
            }
            
            result.add(layerResult);
            if (layers[~i & 1].isEmpty())
                break;
        }
        return result;
    }

    private static boolean isPathAux (List<Edge> edges, Vertex v, Vertex u, HashSet<Vertex> visited)
    {
        for (Vertex neighbor : GraphProperties.getNeighbors(edges, v))
            if (neighbor.equals(u))
                return true; // we found it
            else if (visited.add(neighbor))
                if (isPathAux(edges, neighbor, u, visited))
                    return true;
        return false;
    }

    public static boolean isConnected (List<Vertex> vertices, List<Edge> edges)
    {
        for (Vertex v : vertices)
            for (Vertex u : vertices)
                if (!v.equals(u))
                    if (!isPath(edges, v, u))
                        return false;
        return true;
    }

    private static boolean isPath (List<Edge> edges, Vertex v, Vertex u)
    {
        return v.equals(u) || isPathAux(edges, v, u, new HashSet<>());
    }
}