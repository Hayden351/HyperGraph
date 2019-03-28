package definition;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
    
    public static void main (String[] args)
    {
        generate.ExampleGraphs.wikiGenericTrie().serializeToFile("wikiTrie.gr");
        
        Graph G = Graph.deserializeFromFile("wikiTrie.gr");
        System.out.println(G);
    }
    
    public boolean serializeToFile(String relativeFilePath)
    {
        return Graph.serializeToFile(this, relativeFilePath);
    }
    
    public static boolean serializeToFile(Graph G, String relativeFilePath)
    {
        try (PrintStream out = new PrintStream(new File(relativeFilePath)))
        {
            out.println(String.join(",", G.vertices.stream().map(
                v -> String.format("(%s~%s)", v.uniqueId, v.label)
            ).collect(Collectors.toList())));
            out.println(String.join(",", G.edges.stream().map(
                x -> String.format("(%s~%s~%s)", x.uniqueId, x.label, x.orientations.entrySet().stream().map(p -> String.format("((%s~%s)~%s)", p.getKey().uniqueId, p.getKey().label, p.getValue())).collect(Collectors.toList()))
            ).collect(Collectors.toList())));
            System.out.println(String.join(",", G.vertices.stream().map(
                x -> String.format("(%s~%s)", x.uniqueId, x.label)
            ).collect(Collectors.toList())));
            System.out.println(String.join(",", G.edges.stream().map(
                x -> String.format("(%s~%s~%s)", x.uniqueId, x.label, x.orientations.entrySet().stream().map(p -> String.format("((%s~%s)~%s)", p.getKey().uniqueId, p.getKey().label, p.getValue())).collect(Collectors.toList()))
            ).collect(Collectors.toList())));
            return true;
        } catch (FileNotFoundException ex) {}
        return false;
    }
    
    public static Graph deserializeFromFile(String relativeFilePath)
    {
        Graph G = new Graph();
        try (BufferedReader in = new BufferedReader(new FileReader(new File(relativeFilePath))))
        {
            String vertices = in.readLine();
            Pattern p = Pattern.compile("\\((.*?)~(.*?)\\),?");
            Matcher m = p.matcher(vertices);
            for (int i = 0; i < m.groupCount(); i++)
                System.out.println(m.group(i));
            
            return G;
        } catch (IOException ex) {}
        return null;
    }

    public void addAllVertices (Vertex[] vertices)
    {
        for (Vertex v : vertices)
            this.addVertex(v);
    }
    
    public static Graph unmarshallGraph(JsonNode json)
    {
        Graph G = new Graph();
        JsonNode vertices = json.get("vertices");
        
        json.get("vertices").elements().forEachRemaining(vertexJson ->
        {
            int uniqueId = vertexJson.get("uniqueId").asInt();
            String label = vertexJson.get("label").asText();
            G.addVertex(new Vertex(uniqueId, label));
        });
        
        json.get("edges").elements().forEachRemaining(edgeJson ->
        {
            System.out.println(edgeJson);
            G.addEdge(unmarshallEdge(G, edgeJson));
        });
        return G;
    }
    
    
    // assumes G has already populated its vertices from json
    // because it relies on the vertex ids
    public static Edge unmarshallEdge(Graph G, JsonNode json)
    {
        int uniqueId = json.get("uniqueId").asInt();
        String label = json.get("label").asText();
        Map<Vertex, Boolean> orientations = new TreeMap<>();
        JsonNode orientationJson = json.get("orientations");
        orientationJson.fieldNames().forEachRemaining(field ->
        {
            orientations.put(G.findVertexById(Integer.parseInt(field)), orientationJson.get(field).asBoolean());
        });
        
        return new Edge(uniqueId, label, orientations);
    }
    
    public Vertex findVertexById(int id)
    {
        for (Vertex v : this.vertices)
            if (v.uniqueId == id)
                return v;
        return null;
    }

    public Edge findEdgeById (int id)
    {
        for (Edge e : this.edges)
            if (e.uniqueId == id)
                return e;
        return null;
    }
}
