package sample.AI;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;

import java.util.Arrays;
import java.util.List;

public class LogicTest {
    List<Integer> currAgentPos = Arrays.asList(0, 0) ;


    public static void main(String[] args)
    {
       KnowledgeBaseSingleton knowledgeBaseSingleton = KnowledgeBaseSingleton.getInstance();
       knowledgeBaseSingleton.addSentenceToKnowledgeBase("~P21|B11");
       knowledgeBaseSingleton.addSentenceToKnowledgeBase("~B11|P12|P21");
       knowledgeBaseSingleton.addSentenceToKnowledgeBase("~P12|B11");
       knowledgeBaseSingleton.addSentenceToKnowledgeBase("~B11");
//       knowledgeBaseSingleton.addSentenceToKnowledgeBase("~B12");

       PropositionalLogicResolution propositionalLogicResolution = new PropositionalLogicResolution();
        System.out.println(propositionalLogicResolution.getResolutionResult("~P12"));
    }

    public void testLogic(){
        FormulaFactory f = new FormulaFactory();
        PropositionalParser p = new PropositionalParser(f);
        Formula formula = null;
        try{
            formula= p.parse("A => (B|C)");
            final Formula cnf = formula.cnf();

            System.out.println(cnf.toString());
        } catch(ParserException e) {
            e.printStackTrace();
        }
    }

//    public List<String> generatePossibleMoves(){
//
//        if("stench"){
//
//        }
//        else if("breeze"){
//
//        }
//        else if("glitter"){
//
//        }
//        else
//        {
//
//        }
//
//
//        return null;
//    }

}
