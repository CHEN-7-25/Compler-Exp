package inter;

import symbols.Type;

/**
 * 抽象语法树节点：双分支 If-Else 控制流语句。
 */
public class Else extends Stmt {

   Expr expr;
   Stmt stmt1, stmt2;

   /**
    * 构造双分支 If-Else 语句节点，并进行语义类型检查。
    */
   public Else(Expr x, Stmt s1, Stmt s2) {
      expr = x;
      stmt1 = s1;
      stmt2 = s2;
      if (expr.type != Type.Bool)
         expr.error("boolean required in if");
   }

   /**
    * 中间代码生成：生成双分支 If-Else 结构的控制流。
    * 
    * @param b 当前语句块的进入标号
    * @param a 当前语句块的跳出标号
    */
   public void gen(int b, int a) {
      int label1 = newlabel(); // [TAC生成] label1 对应 if 成功分支 (stmt1)
      int label2 = newlabel(); // [TAC生成] label2 对应 else 分支 (stmt2)
      expr.jumping(0, label2); // 条件判断：如果是 true 直接进入 stmt1，如果是 false 跳转到 label2
      emitlabel(label1);
      stmt1.gen(label1, a);
      emit("goto L" + a); // stmt1 执行完后直接跳出结构
      emitlabel(label2);
      stmt2.gen(label2, a); // stmt2 执行逻辑
   }
}
