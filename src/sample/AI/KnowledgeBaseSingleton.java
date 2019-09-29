package sample.AI;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeBaseSingleton {
    private static KnowledgeBaseSingleton ourInstance = new KnowledgeBaseSingleton();
    private static List<String> knowledbaseSentence = new ArrayList<>();
    public int[][] visitedCell = new int[10][10];
    public static KnowledgeBaseSingleton getInstance() {
        return ourInstance;
    }

    public List<String> getKnowledgeBaseSentences(){
        return knowledbaseSentence;
    }

    public void addSentenceToKnowledgeBase(String sentence){
        if(!knowledbaseSentence.contains(sentence))
            knowledbaseSentence.add(sentence);
    }


    public void printKB(){
        for(String str: knowledbaseSentence)
            System.out.println(str);

        System.out.println("---------------------------------");
    }


    private KnowledgeBaseSingleton() {
        for(int i=0; i<10; i++)
            for(int j=0; j<10; j++)
                visitedCell[i][j] = 0;
//        visitedCell[0][0] = 0;
    }
}
