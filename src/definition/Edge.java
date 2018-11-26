package definition;

import definition.Vertex;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * @author Hayden Fields
 */
public class Edge implements Comparable<Edge>
{
    private static int edgeId = 0;
    public static class OrientedVertex implements Comparable<OrientedVertex>
    {
        Vertex v;
        boolean orientedToV;

        @Override
        public String toString ()
        {
            return "OrientedVertex{" + "v=" + v + ", orientedToV=" + orientedToV + '}';
        }

        public OrientedVertex(Vertex v, boolean towards)
        {
            this.v = v;
            this.orientedToV = towards;
        }

        @Override
        public int compareTo(OrientedVertex v)
        {
            return this.v.compareTo(v.v);
        }
    }

    public final int uniqueId;

    public String label;
    public Map<Vertex, Boolean> vertices;

    public Edge()
    {
        this("");
    }

    public Edge(String label)
    {
        this(label, new TreeSet<Vertex>());
    }

    public Edge(String label, Set<Vertex> vertices) 
    {
        this(label, vertices, new TreeMap<Vertex, Boolean>());
    }

    public Edge(String label, Set<Vertex> verticesIn, Map<Vertex, Boolean> orientation)
    {
        uniqueId = edgeId++;

        this.label = label;
        this.vertices = new TreeMap<>();
        
        // orientation passed in could change over time so lets get our own copy
        for (Vertex v : verticesIn)
            if (orientation.containsKey(v))
                this.vertices.put(v, orientation.get(v));
            else
                this.vertices.put(v, false);
    }

    public Set<Vertex> vertices ()
    {
        return new TreeSet<>(vertices.keySet());
    }

    public boolean sameConnections (Edge e)
    {
        return e.vertices.keySet().equals(vertices.keySet());
    }

    public boolean orientedTowards (Vertex v)
    {
        Boolean value = vertices.get(v);
        return value != null && value;
    }

    @Override
    public int compareTo (Edge that)
    {
        return this.uniqueId - that.uniqueId;
    }
    
    @Override
    public String toString ()
    {
        return "Edge{" + "uniqueId=" + uniqueId + ", label=" + label + ", vertices=" + vertices + '}';
    }
}

