package misc;

import java.util.Arrays;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Hayden Fields
 * 
 * So we need a tested solution to evaluating expressions
 * We also want which command line arguments to support
 * 
 * Required arguments
 * the filepath to the class
 * the type and name of the field that has been added/will be added
 * 
 * optional arguments
 * 
 * 
 * Program will not modify the java class by default to avoid confusing the user
 * If the user does not add a flag for changing the java class will read the class
 * see if the variable is already defined if it is then take note of its location
 * else assume it will be added to the end
 * 
 * -a will added field, getters/setters to the specified object
 * 
 * 
 * Expression as an argument that will use the values already present in the
 * json to generate a value
 * 
 * -e 'x==2?"":3'
 * 
 */
public class CommandLineArguments
{
    /**
     * @param args the command line arguments
     */
    //https://stackoverflow.com/questions/367706/how-do-i-parse-command-line-arguments-in-java
    public static void main(String[] args)
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
        
        Option expression = new Option("e", "expression", false, "The result of thi");
        expression.setRequired(false);
        options.addOption(expression);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try
        {
            cmd = parser.parse(options, args);
            
            System.out.println(Arrays.toString(cmd.getOptions()));
            
            for (Option o : cmd.getOptions())
            {
                System.out.println(cmd.getParsedOptionValue(o.getOpt()));
            }
            
            if (cmd.hasOption('a'))
                System.out.println("yes");;
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

/*

all
-a
-c
-t
-n
boolean flags with hasOption

[[ option: a addToClass  :: If set will add the field specified by -t and -n to the class specified by -c. :: class java.lang.String ], [ option: c classFilePath  [ARG] :: File path to class that is serialized to json. :: class java.lang.String ], [ option: t fieldType  [ARG] :: Type of field that was added to the class specfied by -c. :: class java.lang.String ], [ option: n fieldName  [ARG] :: Name of field that was added to the class specfied by -c. :: class java.lang.String ]]
null
AssociatedTransaction
String
asdf
yes


*/