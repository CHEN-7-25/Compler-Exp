package org.qogir.compiler.grammar.regularGrammar;

import org.qogir.compiler.FA.State;
import org.qogir.compiler.util.graph.LabeledDirectedGraph;

public class ThompsonConstruction {

    public TNFA translate(RegexTreeNode node, RegexTreeNode root) {
        if (node == null) return null;

        int type = node.getType();
        char val = node.getValue();

        // 1. 基础字符：创建包含两个状态（Start, Accept）的片段
        if (type == 0) {
            TNFA tnfa = new TNFA();
            tnfa.getTransitTable().addEdge(tnfa.getStartState(), tnfa.getAcceptingState(), val);
            return tnfa;
        }

        // 2. 连接操作 (A·B)
        if (type == 1) {
            RegexTreeNode child = (RegexTreeNode) node.getFirstChild();
            // 直接以第一个子项作为起点，避免冗余的 new TNFA() 消耗 ID
            TNFA totalNFA = translate(child, root);

            child = (RegexTreeNode) child.getNextSibling();
            while (child != null) {
                TNFA nextNFA = translate(child, root);

                // 核心修改：将前一个片段的接受状态重置为普通状态
                totalNFA.getAcceptingState().setType(State.MIDDLE);

                totalNFA.getTransitTable().merge(nextNFA.getTransitTable());
                // 添加 ε 边连接两个片段
                totalNFA.getTransitTable().addEdge(totalNFA.getAcceptingState(), nextNFA.getStartState(), 'ε');

                // 更新总体的接受状态为后一个片段的接受状态
                totalNFA.setAcceptingState(nextNFA.getAcceptingState());
                child = (RegexTreeNode) child.getNextSibling();
            }
            return totalNFA;
        }

        // 3. 选择 (|) 和 闭包 (*)：这里需要创建新的外层节点
        TNFA tnfa = new TNFA(); // 此时创建的两个状态将作为该层级的唯一起始和终点
        LabeledDirectedGraph<State> graph = tnfa.getTransitTable();
        State start = tnfa.getStartState();
        State accept = tnfa.getAcceptingState();

        if (type == 2) { // Union (|)
            RegexTreeNode child = (RegexTreeNode) node.getFirstChild();
            while (child != null) {
                TNFA childNFA = translate(child, root);
                // 接入前重置子项的终态类型
                childNFA.getAcceptingState().setType(State.MIDDLE);

                graph.merge(childNFA.getTransitTable());
                graph.addEdge(start, childNFA.getStartState(), 'ε');
                graph.addEdge(childNFA.getAcceptingState(), accept, 'ε');
                child = (RegexTreeNode) child.getNextSibling();
            }
        }
        else if (type == 3) { // Kleene Closure (*)
            RegexTreeNode child = (RegexTreeNode) node.getFirstChild();
            TNFA childNFA = translate(child, root);
            // 接入前重置子项的终态类型
            childNFA.getAcceptingState().setType(State.MIDDLE);

            graph.merge(childNFA.getTransitTable());
            graph.addEdge(start, childNFA.getStartState(), 'ε'); // 闭包进入
            graph.addEdge(start, accept, 'ε');                    // 闭包跳过
            graph.addEdge(childNFA.getAcceptingState(), childNFA.getStartState(), 'ε'); // 闭包重复
            graph.addEdge(childNFA.getAcceptingState(), accept, 'ε'); // 闭包退出
        }

        return tnfa;
    }
}