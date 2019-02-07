package fluke.treetweaker.zenscript;

import java.io.IOException;
import java.io.Reader;

import stanhebben.zenscript.IZenCompileEnvironment;
import stanhebben.zenscript.ZenTokener;
import stanhebben.zenscript.parser.Token;

public class ShowMeYourTokens extends ZenTokener
{

	public ShowMeYourTokens(Reader contents, IZenCompileEnvironment environment, String fileNameFallback,
			boolean ignoreBracketErrors) throws IOException 
	{
		super(contents, environment, fileNameFallback, ignoreBracketErrors);
	}
	
	@Override
    public Token process(Token token) {
        //System.out.println(token.getValue());
		Token tk = super.process(token);
        System.out.println(tk.toString());
        return tk;
        // token;
    }

}
