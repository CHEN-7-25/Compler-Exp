package inter;

/**
 * 类 Break 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Break extends Stmt {
   Stmt stmt;

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
   public Break() {
      if( Stmt.Enclosing == Stmt.Null ) error("unenclosed break");
      stmt = Stmt.Enclosing;
   }

    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
    public void gen(int b, int a) {
        System.out.println("break");
    }
}
