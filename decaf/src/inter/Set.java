package inter;

import symbols.Type;

/**
 * 抽象语法树节点：标量变量赋值语句 (id = expr)。
 */
public class Set extends Stmt {

   public Id id;
   public Expr expr;

   /**
    * 构造形如 id = expr 的赋值节点，并做类型检查。
    */
   public Set(Id i, Expr x) {
      id = i;
      expr = x;
      if (check(id.type, expr.type) == null)
         error("type error");
   }

   /**
    * 语义分析：校验赋值语句的左右类型是否匹配或兼容。
    * 
    * @param p1 左侧变量（Identifier）的类型
    * @param p2 右侧表达式（Expression）的类型
    * @return 如果类型兼容则返回推导后的类型；否则返回 null，触发类型不匹配的语义错误
    */
   public Type check(Type p1, Type p2) {
      if (Type.numeric(p1) && Type.numeric(p2))
         return p2;
      else if (p1 == Type.Bool && p2 == Type.Bool)
         return p2;
      else
         return null;
   }

   /**
    * 中间代码生成：计算右侧表达式的值并赋值给左侧变量。
    */
   public void gen(int b, int a) {
      emit(id.toString() + " = " + expr.gen().toString()); // [TAC生成] 发射赋值指令，此处调用 expr.gen() 进行代码生成
      // System.out.println("assignment");
   }
}