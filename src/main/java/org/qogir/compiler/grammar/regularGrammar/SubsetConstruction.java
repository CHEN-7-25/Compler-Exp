package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabelEdge;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

import java.util.*;

/**
 * The subset construction Algorithm for converting an NFA to a DFA.
 * The subset construction Algorithm takes an NFA N as input and output a DFA D accepting the same language as N.
 * The main mission is to eliminate ε-transitions and multi-transitions in NFA and construct a transition table for D.
 * The algorithm can be referred to {@see }
 */
public class SubsetConstruction {

    /**
     * Eliminate all ε-transitions reachable from a single state in NFA through the epsilon closure operation.
     * @param s a single state of NFA
     * @param tb the transition table of NFA
     * @return a set of state reachable from the state s on ε-transition
     * @author xuyang
     */
    private HashMap<Integer, State> epsilonClosures(State s, LabeledDirectedGraph<State> tb){
        if (!tb.vertexSet().contains(s)) { //if vertex s not in the transition table
            return null;
        }

        HashMap<Integer, State> closure = new HashMap<>();

        //Add your implementation *Done*
        Stack<State> stack = new Stack<>();

        closure.put(s.getId(), s);
        stack.push(s);

        while (!stack.isEmpty()) {
            State u = stack.pop();
            for (LabelEdge edge : tb.edgeSet()) {
                // 仅沿着 ε 边进行深度/广度优先搜索
                if (tb.getEdgeSource(edge).equals(u) && edge.getLabel() == 'ε') {
                    State v = (State) edge.getTarget();
                    if (!closure.containsKey(v.getId())) {
                        closure.put(v.getId(), v);
                        stack.push(v);
                    }
                }
            }
        }

        return closure;
    }

    /**
     * Eliminate all ε-transitions reachable from a  state set in NFA through the epsilon closure operation
     * @param ss a state set of NFA
     * @param tb the transition table of NFA
     * @return a set of state reachable from the state set on ε-transition
     * @author xuyang
     */

    public HashMap<Integer, State> epsilonClosure(HashMap<Integer, State> ss, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();
        for(State s : ss.values()){
            nfaStates.putAll(epsilonClosures(s,tb));
        }
        return nfaStates;
    }

    /**
     *
     * @param s
     * @param ch
     * @param tb
     * @return
     */
    private HashMap<Integer,State> moves(State s, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();

        //Add your implementation
        for (LabelEdge edge : tb.edgeSet()) {
            if (tb.getEdgeSource(edge).equals(s) && edge.getLabel().equals(ch)) {
                State v = (State) edge.getTarget();
                nfaStates.put(v.getId(), v);
            }
        }
        return nfaStates;
    }

    public HashMap<Integer,State> move(HashMap<Integer, State> ss, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> nfaStates = new HashMap<>();
        for(State s : ss.values()){
            nfaStates.putAll(moves(s,ch,tb));
        }
        return nfaStates;
    }

    public HashMap<Integer,State> epsilonClosureWithMove(HashMap<Integer, State> sSet, Character ch, LabeledDirectedGraph<State> tb){
        HashMap<Integer,State> states = new HashMap<>();
        states.putAll(epsilonClosure(move(sSet, ch, tb),tb));
        return states;
    }

    public RDFA subSetConstruct(TNFA tnfa){

        //Add your implementation
        LabeledDirectedGraph<State> nfaTable = tnfa.getTransitTable();
        ArrayList<Character> alphabet = tnfa.getAlphabet();

        // 1. 获取 DFA 的起始状态集合 (NFA 起始状态的 ε-closure)
        HashMap<Integer, State> startSet = new HashMap<>();
        startSet.put(tnfa.getStartState().getId(), tnfa.getStartState());
        HashMap<Integer, State> d0Set = epsilonClosure(startSet, nfaTable);

        // 2. 创建 DFA 起始状态并建立映射
        State d0 = new State(State.START);
        if (containsAcceptState(d0Set, tnfa.getAcceptingState())) {
            d0.setType(State.ACCEPTANDSTART); // 若包含 NFA 终态，则标记为 DFA 终态
        }

        RDFA dfa = new RDFA(d0);
        dfa.setAlphabet(alphabet);
        dfa.setStateMappingBetweenDFAAndNFA(d0, d0Set);

        // 3. 迭代构建 DFA 状态
        Queue<State> workList = new LinkedList<>();
        workList.add(d0);
        ArrayList<State> dStates = new ArrayList<>();
        dStates.add(d0);

        while (!workList.isEmpty()) {
            State T = workList.poll();
            HashMap<Integer, State> TSet = dfa.getStateMappingBetweenDFAAndNFA().get(T);

            for (Character a : alphabet) {
                // 计算 U = ε-closure(move(T, a))
                HashMap<Integer, State> USet = epsilonClosure(move(TSet, a, nfaTable), nfaTable);
                if (USet.isEmpty()) continue;

                // 检查该状态集是否已存在
                State U = findExistingState(dStates, USet, dfa);
                if (U == null) {
                    U = new State(State.MIDDLE);
                    if (containsAcceptState(USet, tnfa.getAcceptingState())) {
                        U.setType(State.ACCEPT);
                    }
                    dStates.add(U);
                    dfa.getTransitTable().addVertex(U);
                    dfa.setStateMappingBetweenDFAAndNFA(U, USet);
                    workList.add(U);
                }
                dfa.getTransitTable().addEdge(T, U, a);
            }
        }
        return dfa;
    }

    // The next methods are added by me

    private boolean containsAcceptState(HashMap<Integer, State> set, State nfaAcceptState) {
        return set.containsKey(nfaAcceptState.getId());
    }

    private State findExistingState(ArrayList<State> states, HashMap<Integer, State> set, RDFA dfa) {
        for (State s : states) {
            if (dfa.getStateMappingBetweenDFAAndNFA().get(s).keySet().equals(set.keySet())) {
                return s;
            }
        }
        return null;
    }
}
