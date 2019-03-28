package definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import definition.Vertex;
import java.util.HashMap;
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

    // TODO: I don't actually think that this is used anywhere
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
    @JsonIgnore public Map<Vertex, Boolean> orientations;
    @JsonProperty("orientations") public Map<String, Boolean> verticesJsonRepresentation()
    {
        Map<String, Boolean> jsonOrientations = new HashMap<>();
        for (Vertex v : orientations.keySet())
            jsonOrientations.put(String.format("%d", v.uniqueId), orientations.get(v));
        return jsonOrientations;
    }
    

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
    
    public Edge(String label, Map<Vertex, Boolean> orientations)
    {
        this(label, orientations.keySet(), orientations);
    }

    public Edge(String label, Set<Vertex> verticesIn, Map<Vertex, Boolean> orientation)
    {
        this(edgeId++, label, verticesIn, orientation);
    }
    public Edge(int id, String label, Set<Vertex> verticesIn, Map<Vertex, Boolean> orientation)
    {
        uniqueId = id;
        // so that invariant that the edgeId has a unused id will be maintained
        if (edgeId <= uniqueId)
            edgeId = uniqueId + 1;

        this.label = label;
        this.orientations = new TreeMap<>();
        
        // orientation passed in could change over time so lets get our own copy
        for (Vertex v : verticesIn)
            if (orientation.containsKey(v))
                this.orientations.put(v, orientation.get(v));
            else
                this.orientations.put(v, false);
    }
    
    public Edge (int uniqueId, String label, Map<Vertex, Boolean> orientations)
    {
        this(uniqueId, label, orientations.keySet(), orientations);
    }

    public Set<Vertex> vertices ()
    {
        return new TreeSet<>(orientations.keySet());
    }

    public boolean sameConnections (Edge e)
    {
        return e.orientations.keySet().equals(orientations.keySet());
    }

    public boolean orientedTowards (Vertex v)
    {
        Boolean value = orientations.get(v);
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
        return "Edge{" + "uniqueId=" + uniqueId + ", label=" + label + ", vertices=" + orientations + '}';
    }
    
    public Edge cloneWithNewId ()
    {
        // gets a new id
        Edge e = new Edge(this.label);
        e.orientations.forEach((v,b) -> e.orientations.put(v.cloneWithNewId(), b));
        return e;
    }
}

