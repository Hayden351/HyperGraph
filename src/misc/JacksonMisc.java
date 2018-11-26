package misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Hayden Fields
 */
public class JacksonMisc
{
    // Jackson deserializes based on public fields/methods
    // fields that are public or methods that start with get
    
    // the script user tells us what he changed
    // We changed Type C to have a new field, what should the default value be?
    // that field is of type A
    // if A is primitive {boolean, int, float, string}
    // else if A is a complex type for fields [f_1,..., f_n] of A (as determined by getters)
    //
    public static void main (String[] args) throws IOException
    {
        ObjectMapper om = new ObjectMapper();
        
        // isX is only allowed for Boolean or boolean types
        // get works for anything
        om.writeValue(new UnclosableOutputStream(System.out), new Object()
        {
            private Boolean p;
            
            public Boolean isP()
            {
                return p;
            }
                
        });
//        System.out.println(new File("").getAbsolutePath());
        // types are {boolean int, float, string, object, array}
        System.out.println("");
        System.out.println(om.writerWithDefaultPrettyPrinter().writeValueAsString(om.readTree(new File("src/misc/json"))));
        System.out.println("");
    }
    
    
    
    // command line arguments
    // algorithm for find classes that contain classes of interest as a field
    // syntax for defining a function that gives the json value a value
    // f in C needs a value, use the other f_i in C to generate a value
    // 
    
    // use elements of C to calculate value of type T and name N
    // f1 -> f2 -> f3 ->... fn -> f
    // unless specified on the command line uses a default value
    // expr : x + y
    public static <C, T> T generate(C cls)
    {
        return null;
    }
    
    // controller response body
    // get cvshome > projectName > c > className > .java
    // parse with javaparser
    // for each public field
    // for each method that starts with get or is + returns boolean or Boolean (could check superclasses)
    // List<List>
}


// https://stackoverflow.com/questions/8941298/system-out-closed-can-i-reopen-it
class UnclosableOutputStream extends FilterOutputStream
{
    public UnclosableOutputStream(OutputStream out)
    {
        super(out);
    }

    @Override
    public void close() throws IOException
    {
        out.flush();
    }
}