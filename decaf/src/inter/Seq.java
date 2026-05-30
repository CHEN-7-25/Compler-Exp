package inter;

/**
 * 抽象语法树节点：语句序列（Sequence）。
 * 用于将两条语句连接为一个复合语句块。
 */
public class Seq extends Stmt {

	Stmt stmt1;
	Stmt stmt2;

	/**
	 * 构造语句序列节点。
	 * 
	 * @param s1 第一条语句
	 * @param s2 第二条语句
	 */
	public Seq(Stmt s1, Stmt s2) {
		stmt1 = s1;
		stmt2 = s2;
	}

	/**
	 * 中间代码生成：串联执行两条语句。
	 * 
	 * @param b 当前语句块的进入标号
	 * @param a 当前语句块的跳出标号
	 */
	public void gen(int b, int a) {
		if (stmt1 == Stmt.Null)
			stmt2.gen(b, a);
		else if (stmt2 == Stmt.Null)
			stmt1.gen(b, a);
		else {
			int label = newlabel(); // [TAC生成] 第一条语句执行完毕后，进入第二条语句的过渡标号
			stmt1.gen(b, label);
			emitlabel(label);
			stmt2.gen(label, a);
		}
		// 以下注释的代码用于输出语法树结构，便于调试和理解生成过程（实验3）
		// if ( stmt1 != Stmt.Null ) stmt1.gen(b, a);
		// if ( stmt2 != Stmt.Null ) stmt2.gen(b, a);
	}
}
