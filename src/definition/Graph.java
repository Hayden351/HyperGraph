package definition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Hayden Fields
 */
public class Graph
{
    public Set<Vertex> vertices;
    public Set<Edge> edges;

    public Graph()
    {
        vertices = new TreeSet<>();
        edges = new TreeSet<>();
    }

    public Graph (Collection<Vertex> vertices, Collection<Edge> edges)
    {
        this.vertices = new TreeSet<>(vertices);
        this.edges = new TreeSet<>(edges);
    }

    public Vertex addVertex(String label)
    {
        Vertex v = new Vertex(label);
        vertices.add(v);
        return v;
    }
    public Vertex addVertex(Vertex v)
    {
        vertices.add(v);
        return v;
    }

    public Edge addEdge (String label, Collection<Vertex> vertices)
    {
        return addEdge (label, vertices, new TreeMap<>());
    }

    public Edge addEdge (String label, Collection<Vertex> vertices, Map<Vertex, Boolean> orientation) 
    {
        Edge e = new Edge(label, new TreeSet<>(vertices), orientation);
        edges.add(e);
        return e;
    }
    
    public Edge addEdge(Edge e)
    {
        edges.add(e);
        return e;
    }
    
    public Edge addEdge(Collection<Vertex> vertices)
    {
        return addEdge("", vertices);
    }
    public Edge addEdge (Collection<Vertex> vertices, Map<Vertex, Boolean> orientation) 
    {
        return addEdge("", vertices, orientation);
    }

    public Iterable<Vertex> vertices() {
        return vertices;
    }

    public Iterable<Edge> edges() {
        return edges;
    }

    @Override
    public String toString ()
    {
        return "Graph{" + "vertices=" + vertices + ", edges=" + edges + '}';
    }

    public Vertex getVertexById (int vid)
    {
        for (Vertex v : this.vertices)
            if (v.uniqueId == vid)
                return v;
        return null;
    }

    public Graph createDistinctCopy ()
    {
        Graph G = new Graph();
        
        for (Edge e: this.edges)
            G.addEdge(e.cloneWithNewId());
        for (Edge e : G.edges)
            for (Vertex v : e.vertices())
                G.addVertex(v);
        return G;
    }

    public boolean addAllVertices (List<Vertex> list)
    {
        boolean result = false;
        for (Vertex v : list)
            result |= (addVertex(v) != null);
        return result;
    }

    public Vertex findFirstVertexByLabel (String value)
    {
        for (Vertex v : this.vertices())
            if (v.label.equals(value))
                return v;
        return null;
    }
}
