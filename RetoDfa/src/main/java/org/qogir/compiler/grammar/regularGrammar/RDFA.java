package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.FiniteAutomaton;
import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

import java.util.*;

public class RDFA extends FiniteAutomaton {

    /**
     * holds the maps between DFA states and NFA state sets
     */
    private HashMap<State, HashMap<Integer,State>> StateMappingBetweenDFAAndNFA = new HashMap<>();
    public RDFA(){
        super();
        this.StateMappingBetweenDFAAndNFA = new HashMap<>();
        this.transitTable = new LabeledDirectedGraph<>();
    }

    public RDFA(State startState){
        this.startState = startState;
        this.StateMappingBetweenDFAAndNFA = new HashMap<>();
        this.transitTable = new LabeledDirectedGraph<>();
        this.getTransitTable().addVertex(this.startState);
    }

    public void setStateMappingBetweenDFAAndNFA(State s, HashMap<Integer,State> nfaStates){
        this.StateMappingBetweenDFAAndNFA.put(s,nfaStates);
    }

    public HashMap<State, HashMap<Integer, State>> getStateMappingBetweenDFAAndNFA() {
        return StateMappingBetweenDFAAndNFA;
    }

    public String StateMappingBetweenDFAAndNFAToString() {
        StringBuilder str = new StringBuilder();

        // 1. 将 keySet 转为 List 并按照 sid 的数字大小进行排序
        List<State> sortedStates = new ArrayList<>(this.StateMappingBetweenDFAAndNFA.keySet());
        sortedStates.sort(Comparator.comparingInt(s -> Integer.parseInt(s.getSid())));

        for (State s : sortedStates) {
            // 2. 使用 s.toString() 自动输出 sid:type 格式
            str.append("DFA State:").append(s.toString()).append("\tNFA State set:{");

            HashMap<Integer, State> nfaStates = this.StateMappingBetweenDFAAndNFA.get(s);
            List<Integer> nfaIds = new ArrayList<>(nfaStates.keySet());
            Collections.sort(nfaIds); // 对 NFA 状态 ID 排序，方便核对

            for (int i = 0; i < nfaIds.size(); i++) {
                str.append(nfaIds.get(i));
                if (i < nfaIds.size() - 1) str.append(",");
            }
            str.append("}\n");
        }
        return str.toString();
    }
}
