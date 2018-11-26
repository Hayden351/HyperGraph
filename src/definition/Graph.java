package definition;

import java.util.Collection;
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
    
}
