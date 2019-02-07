package fluke.treetweaker.zenscript;

import static stanhebben.zenscript.ZenModule.extractClassName;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.runtime.IScriptIterator;
import crafttweaker.runtime.IScriptProvider;
import crafttweaker.runtime.ScriptFile;
import crafttweaker.runtime.providers.ScriptProviderCascade;
import crafttweaker.runtime.providers.ScriptProviderDirectory;
import crafttweaker.zenscript.GlobalRegistry;
import fluke.treetweaker.TreeTweaker;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import stanhebben.zenscript.ZenParsedFile;
import stanhebben.zenscript.ZenTokener;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.definitions.Import;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.expression.ParsedExpressionCall;
import stanhebben.zenscript.parser.expression.ParsedExpressionMember;
import stanhebben.zenscript.parser.expression.ParsedExpressionValue;
import stanhebben.zenscript.parser.expression.ParsedExpressionVariable;
import stanhebben.zenscript.statements.Statement;
import stanhebben.zenscript.statements.StatementVar;

public class SaplingScriptParser 
{
	private static IScriptProvider scriptsGlobal;
	
	public static void fetchScripts()
	{
		//This seems like a dumb idea, yet here we are
		Field statementVarName = ReflectionHelper.findField(StatementVar.class, "name");
		statementVarName.setAccessible(true);
		Field statementVarInitializer = ReflectionHelper.findField(StatementVar.class, "initializer");
		statementVarInitializer.setAccessible(true);
		Field parsedExpressionCallReceiver = ReflectionHelper.findField(ParsedExpressionCall.class, "receiver");
		parsedExpressionCallReceiver.setAccessible(true);
		Field parsedExpressionCallArguments = ReflectionHelper.findField(ParsedExpressionCall.class, "arguments");
		parsedExpressionCallArguments.setAccessible(true);
		Field parsedExpressionValue = ReflectionHelper.findField(ParsedExpressionValue.class, "value");
		parsedExpressionValue.setAccessible(true);
		Field expressionStringValue = ReflectionHelper.findField(ExpressionString.class, "value");
		expressionStringValue.setAccessible(true);
		Field parsedExpressionMemberValue = ReflectionHelper.findField(ParsedExpressionMember.class, "value");
		parsedExpressionMemberValue.setAccessible(true);
		Field parsedExpressionMember = ReflectionHelper.findField(ParsedExpressionMember.class, "member");
		parsedExpressionMember.setAccessible(true);
		Field parsedExpressionVariableName = ReflectionHelper.findField(ParsedExpressionVariable.class, "name");
		parsedExpressionVariableName.setAccessible(true);
		
		
		File globalDir = new File("scripts");
		scriptsGlobal = new ScriptProviderDirectory(globalDir);
		IScriptProvider cascaded = new ScriptProviderCascade(scriptsGlobal);
		Iterator<IScriptIterator> scripts = cascaded.getScripts();


		List<ScriptFile> fileList = new ArrayList<>();
        HashSet<String> collected = new HashSet<>();
        
        // Collecting all scripts
        
        while(scripts.hasNext()) {
            IScriptIterator script = scripts.next();
            
            if(!collected.contains(script.getGroupName())) {
                collected.add(script.getGroupName());
                
                while(script.next()) {
                    fileList.add(new ScriptFile(CraftTweakerAPI.tweaker, script.copyCurrent(), false));
                }
            }
        }
        
        Map<String, byte[]> classes = new HashMap<>();
        IEnvironmentGlobal environmentGlobal = GlobalRegistry.makeGlobalEnvironment(classes);//pls dont break loader.getMainName());
        for(ScriptFile scriptFile : fileList) 
        {
        	//TODO skip on client side?
        	System.out.println(scriptFile.toString());
        	try (Reader reader = new InputStreamReader(new BufferedInputStream(scriptFile.open()), StandardCharsets.UTF_8))
        	{
        		String filename = scriptFile.getEffectiveName();
        		String className = extractClassName(filename);
        		//ZenTokener parser = new ShowMeYourTokens(reader, environmentGlobal.getEnvironment(), scriptFile.getEffectiveName(), scriptFile.areBracketErrorsIgnored());
        		ZenTokener parser = new ZenTokener(reader, environmentGlobal.getEnvironment(), scriptFile.getEffectiveName(), scriptFile.areBracketErrorsIgnored());
        		ZenParsedFile zenParsedFile = new ZenParsedFile(scriptFile.getEffectiveName(), className, parser, environmentGlobal);
        		
        		
        		
        		boolean isTreeFile = false;
        		for(Import zsImport : zenParsedFile.getImports())
        		{
        			System.out.println(zsImport.getName());
        			if(zsImport.getName().contains("TreeFactory"))
        				isTreeFile = true;
        		}
        		System.out.println("mmkay");
        		if(isTreeFile)
        		{
        			HashMap<String, String> treeNameMap = new HashMap<>();
        			for(Statement statement : zenParsedFile.getStatements())
        			{
        				if(statement instanceof StatementVar)
        				{
        					System.out.println(statementVarName.get(statement));
        					//treeNameMap.put(statementVarName.get(statement), "whatever they called the tree")
        				}
        			}
        		}
        		
        	} catch(Exception e)
        	{
        		TreeTweaker.logger.catching(e);
        	}
        	
        }
	}

}
