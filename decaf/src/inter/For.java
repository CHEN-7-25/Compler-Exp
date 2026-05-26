package inter;

import symbols.Type;
import lexer.Token;

public class For extends Stmt {

    Expr expr; // 循环的判定条件，如 i < 10
    Stmt stmt1, stmt2, stmt3; // stmt1:初始化(i=0), stmt2:步进更新(i=i+1), stmt3:循环体

    public For() { 
        expr = null; stmt1 = null; stmt2 = null; stmt3 = null; 
    }

    public void init(Stmt s1, Expr x, Stmt s2, Stmt s3) {
        this.stmt1 = s1;
        this.expr = x;
        this.stmt2 = s2;
        this.stmt3 = s3;
        // 校验循环条件是否为布尔类型
        if (expr.type != Type.Bool) {
            expr.error("boolean required in for");
        }
    }

    @Override
    public void gen(int b, int a) {
        System.out.println("stmt : for begin");
        if (stmt1 != Stmt.Null) stmt1.gen(b, a); // 1. 打印初始化赋值 (如 i = 0)
        if (stmt3 != Stmt.Null) stmt3.gen(b, a); // 2. 递归打印循环体
        if (stmt2 != Stmt.Null) stmt2.gen(b, a); // 3. 打印迭代更新赋值 (如 i = i + 1)
        System.out.println("stmt : for end");
    }
}