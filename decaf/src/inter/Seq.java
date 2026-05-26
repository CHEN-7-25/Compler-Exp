package inter;

/**
 * 类 Seq 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Seq extends Stmt {

	   Stmt stmt1; Stmt stmt2;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	   public Seq(Stmt s1, Stmt s2) { stmt1 = s1; stmt2 = s2; }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public void gen(int b, int a) {
		if ( stmt1 != Stmt.Null ) stmt1.gen(b, a);
		if ( stmt2 != Stmt.Null ) stmt2.gen(b, a);
	}
	}

