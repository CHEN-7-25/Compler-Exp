package inter;

public class Stmt extends Node {

  public Stmt() {
  }

  /**
   * 空语句的单例。
   */
  public static Stmt Null = new Stmt();

  /**
   * 生成语句的中间代码。
   * 
   * @param b 语句的开始标号（begin）
   * @param a 语句结束后的出口标号（after）
   */
  public void gen(int b, int a) {
  } // called with labels begin and after

  /**
   * 保存当前语句块的出口标号。
   */
  int after = 0; // saves label after

  /**
   * 静态全局环境指针，始终指向当前正在解析的最内层循环结构。
   * 核心语义：专门用于给嵌套内部的 break 语句提供强行跳出时的目标上下文。
   */
  public static Stmt Enclosing = Stmt.Null; // used for break stmts
}
