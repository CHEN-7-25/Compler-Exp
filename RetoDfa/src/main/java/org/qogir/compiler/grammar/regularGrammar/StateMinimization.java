package org.qogir.compiler.grammar.regularGrammar;


import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabelEdge;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

import java.util.*;

public class StateMinimization {

    /**
     * Distinguish non-equivalent states in the given DFA.
     *
     * @param dfa the original dfa.
     * @return distinguished equivalent state groups
     */
    public HashMap<Integer, HashMap<Integer, State>> distinguishEquivalentState(RDFA dfa) {
        //Add your implementation
        LabeledDirectedGraph<State> table = dfa.getTransitTable();
        ArrayList<Character> alphabet = dfa.getAlphabet();
        Set<State> states = table.vertexSet();

        // 1. 初始划分：接受状态组和非接受状态组
        HashMap<Integer, State> acceptGroup = new HashMap<>();
        HashMap<Integer, State> nonAcceptGroup = new HashMap<>();
        for (State s : states) {
            if (s.getType() == State.ACCEPT || s.getType() == State.ACCEPTANDSTART) {
                acceptGroup.put(s.getId(), s);
            } else {
                nonAcceptGroup.put(s.getId(), s);
            }
        }

        List<HashMap<Integer, State>> partitions = new ArrayList<>();
        if (!acceptGroup.isEmpty()) partitions.add(acceptGroup);
        if (!nonAcceptGroup.isEmpty()) partitions.add(nonAcceptGroup);

        // 2. 迭代细分
        boolean changed = true;
        while (changed) {
            changed = false;
            List<HashMap<Integer, State>> nextPartitions = new ArrayList<>();
            for (HashMap<Integer, State> group : partitions) {
                List<HashMap<Integer, State>> refined = splitGroup(group, partitions, alphabet, table);
                nextPartitions.addAll(refined);
                if (refined.size() > 1) changed = true;
            }
            partitions = nextPartitions;
        }

        HashMap<Integer, HashMap<Integer, State>> result = new HashMap<>();
        for (int i = 0; i < partitions.size(); i++) result.put(i, partitions.get(i));
        return result;
    }

    public RDFA minimize(RDFA dfa) {

        //Add your implementation
        HashMap<Integer, HashMap<Integer, State>> groups = distinguishEquivalentState(dfa);

        // 映射新状态
        HashMap<Integer, State> groupToNewState = new HashMap<>();
        int startGroupId = -1;
        for (Integer id : groups.keySet()) {
            State rep = groups.get(id).values().iterator().next();
            State newState = new State(rep.getType());
            groupToNewState.put(id, newState);
            if (groups.get(id).containsKey(dfa.getStartState().getId())) startGroupId = id;
        }

        RDFA miniDfa = new RDFA(groupToNewState.get(startGroupId));
        miniDfa.setAlphabet(dfa.getAlphabet());

        // 重构转移关系
        for (Integer id : groups.keySet()) {
            State rep = groups.get(id).values().iterator().next();
            for (Character a : dfa.getAlphabet()) {
                State targetOld = getNextState(rep, a, dfa.getTransitTable());
                if (targetOld != null) {
                    miniDfa.getTransitTable().addEdge(groupToNewState.get(id), groupToNewState.get(getGroupId(targetOld, new ArrayList<>(groups.values()))), a);
                }
            }
        }
        return miniDfa;
    }

//    /**
//     * Used for showing the distinguishing process of state miminization algorithm
//     *
//     * @param stepQueue holds all distinguishing steps
//     * @param GroupSet  is the set of equivalent state groups
//     * @param memo      remarks
//     */
//    public void recordDistinguishSteps(ArrayDeque<String> stepQueue, HashMap<Integer, HashMap<Integer, State>> GroupSet, String memo) {
//        String str = "";
//        str = GroupSetToString(GroupSet);
//        str += ":" + memo;
//        stepQueue.add(str);
//        System.out.println(stepQueue);
//    }
//
//    /**
//     * Display the equivalent state groups
//     *
//     * @param stepQueue
//     */
//    public void showDistinguishSteps(ArrayDeque<String> stepQueue) {
//        int step = 0;
//        String str = "";
//        while (!stepQueue.isEmpty()) {
//            str = stepQueue.poll();
//            System.out.println("Step" + step++ + ":\t" + str + "\r");
//        }
//    }

    private String GroupSetToString(HashMap<Integer,HashMap<Integer, State>> GroupSet){
        String str = "";
        for( Integer g: GroupSet.keySet()){
            String tmp = GroupToString(GroupSet.get(g));
            str += g +  ":" + tmp + "\t" ;
        }
        return str;
    }

    private String GroupToString(HashMap<Integer, State> group){
        String str = "";
        for(Integer k : group.keySet()){
            str += group.get(k).getId() + ":" + group.get(k).getType() + ",";
        }
        if(str.length()!=0) str = str.substring(0,str.length()-1);
        str = "{" + str + "}";
        return str;
    }

    private List<HashMap<Integer, State>> splitGroup(HashMap<Integer, State> group, List<HashMap<Integer, State>> p, ArrayList<Character> alpha, LabeledDirectedGraph<State> tb) {
        List<HashMap<Integer, State>> subGroups = new ArrayList<>();
        List<State> members = new ArrayList<>(group.values());
        while (!members.isEmpty()) {
            State s1 = members.remove(0);
            HashMap<Integer, State> newSub = new HashMap<>();
            newSub.put(s1.getId(), s1);
            Iterator<State> it = members.iterator();
            while (it.hasNext()) {
                State s2 = it.next();
                if (isEquivalent(s1, s2, p, alpha, tb)) {
                    newSub.put(s2.getId(), s2);
                    it.remove();
                }
            }
            subGroups.add(newSub);
        }
        return subGroups;
    }

    private boolean isEquivalent(State s1, State s2, List<HashMap<Integer, State>> p, ArrayList<Character> alpha, LabeledDirectedGraph<State> tb) {
        for (Character a : alpha) {
            State t1 = getNextState(s1, a, tb);
            State t2 = getNextState(s2, a, tb);
            if (getGroupId(t1, p) != getGroupId(t2, p)) return false;
        }
        return true;
    }

    private State getNextState(State s, char a, LabeledDirectedGraph<State> tb) {
        for (LabelEdge edge : tb.edgeSet()) {
            if (tb.getEdgeSource(edge).equals(s) && edge.getLabel() == a) return (State) edge.getTarget();
        }
        return null;
    }

    private int getGroupId(State s, List<HashMap<Integer, State>> p) {
        if (s == null) return -1;
        for (int i = 0; i < p.size(); i++) if (p.get(i).containsKey(s.getId())) return i;
        return -2;
    }
}