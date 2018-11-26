package misc;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Hayden Fields
 */
public class CommandLineArguments
{
    //https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
    public static void main (String[] args)
    {
        Options options = new Options();

        Option classPath = new Option("c", "classFilePath", true, "File path to class that is serialized to json.");
        classPath.setRequired(true);
        options.addOption(classPath);

        Option fieldType = new Option("t", "fieldType", true, "Type of field that was added to the class specfied by -c.");
        fieldType.setRequired(true);
        options.addOption(fieldType);
        
        Option fieldName = new Option("n", "fieldName", true, "Name of field that was added to the class specfied by -c.");
        fieldName.setRequired(true);
        options.addOption(fieldName);
        
        Option addToClass = new Option("a", "addToClass", false, "If set will add the field specified by -t and -n to the class specified by -c.");
        addToClass.setRequired(false);
        options.addOption(addToClass);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, args);
            
        }
        catch (ParseException e)
        {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            return;
        }
        System.out.println("");
    }
}
