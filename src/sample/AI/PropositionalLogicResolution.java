package sample.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropositionalLogicResolution {

    public boolean getResolutionResult(String entailQuery){

        List<String> knowledgeBaseSentences = new ArrayList<>(KnowledgeBaseSingleton.getInstance().getKnowledgeBaseSentences());
        knowledgeBaseSentences.add("~"+ entailQuery);
        for(int it=0; it<7; it++) {
            int prevIterSentenceSize = knowledgeBaseSentences.size();
            List<Integer> joinedIndices = new ArrayList<>();

            int joinedCount = 0;
            for(int i=0; i<prevIterSentenceSize; i++){
                for(int j=i+1; j<prevIterSentenceSize; j++){

                    if( i == j) continue;
                    List<String> splittedSentence1 = Arrays.asList(knowledgeBaseSentences.get(i).split("\\|"));
                    List<String> splittedSentence2 = Arrays.asList(knowledgeBaseSentences.get(j).split("\\|"));

                    for(String literal: splittedSentence1){

                        if(splittedSentence2.contains("~"+literal)){
                            String newSentence = knowledgeBaseSentences.get(i) +'|'+ knowledgeBaseSentences.get(j);

                            newSentence = eliminateNegatingLiterals(newSentence, literal);
                            if(newSentence.length() == 0) return true;
                            knowledgeBaseSentences.add(newSentence);
                            if(joinedIndices.contains(i)) joinedIndices.add(i);
                            if(joinedIndices.contains(j)) joinedIndices.add(j);
                            joinedCount++;
                        }
                    }

                }
            }
            if(joinedCount==0) return false;
            for(int i=0; i<prevIterSentenceSize; i++)
                if(joinedIndices.contains(i)) knowledgeBaseSentences.remove(i);


        }


        return false;
    }

    private String eliminateNegatingLiterals(String newSentence, String literal) {
        String resultString = newSentence;

        resultString = removeLiteral(resultString, "~"+literal);
        resultString = removeLiteral(resultString, literal);

        return resultString;
    }


    private String removeLiteral(String inputString, String searchPattern) {
        String processedString = "";
        int pos = inputString.indexOf(searchPattern);
        if(pos == 0){
            if(searchPattern.length()+1 > inputString.length()) return "";
            processedString = inputString.substring(searchPattern.length()+1);
        }else{
//            processedString = inputString.replaceFirst("//|"+searchPattern, "");
            processedString = inputString.substring(0, pos-1) + inputString.substring(pos+searchPattern.length());
        }
        return processedString;
    }

}
