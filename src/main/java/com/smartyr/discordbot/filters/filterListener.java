package com.smartyr.discordbot.filters;

import java.util.ArrayList;
import java.util.List;

import com.smartyr.antlr.FilterBaseListener;
import com.smartyr.antlr.FilterLexer;
import com.smartyr.antlr.FilterParser;
import org.antlr.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class filterListener extends FilterBaseListener {
     private List<String> result = new ArrayList<>();
    @Override
    public void exitExpr(FilterParser.ExprContext ctx) {
        super.exitExpr(ctx);

        if(ctx.COMPARISON() != null){
//            System.out.println(ctx.PROPERTY().getText());
//            System.out.println(ctx.COMPARISON().getText());
//            System.out.println(ctx.VALUE().getText());
            String c = String.format(
                    "%s %s %s",
                    ctx.PROPERTY().getText(),
                    ctx.COMPARISON().getText(),
                    ctx.VALUE().getText());

            String b = String.format("{ \"%s\": { \"%s\": %s } }",
                    ctx.PROPERTY().getText(),
                    ctx.COMPARISON().getText(),
                    ctx.VALUE().getText());

            result.add(b);
        }
        if(ctx.AND() != null){
            buildAnd();
        }

    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.out.println("hit");
        super.visitErrorNode(node);
        result.clear();
        result.add("");
    }

    private void buildAnd(){
        String left = result.get(0);
        String right = result.get(1);
        result =  new ArrayList<>();
        String c = String.format("{ \"and\": [%s , %s] }", left, right);
        result.add(c);
    }


    public String generateQueryString(String myString){
        CodePointCharStream chars = CharStreams.fromString(myString);
        FilterLexer lexer = new FilterLexer(chars);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        FilterParser parse = new FilterParser(tokens);
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, parse.filter());
        return result.get(0);
    }
}