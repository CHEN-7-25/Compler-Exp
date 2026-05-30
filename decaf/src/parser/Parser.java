package parser;

import inter.Access;
import inter.And;
import inter.Arith;
import inter.Break;
import inter.Constant;
import inter.Do;
import inter.Else;
import inter.Expr;
import inter.Id;
import inter.If;
import inter.Not;
import inter.Or;
import inter.Rel;
import inter.Seq;
import inter.Set;
import inter.SetElem;
import inter.Stmt;
import inter.Unary;
import inter.While;
import inter.For;

import java.io.IOException;

import symbols.Array;
import symbols.Env;
import symbols.Type;
import lexer.Lexer;
import lexer.Tag;
import lexer.Token;
import lexer.Word;
import lexer.Num;

/**
 * 语法及语义分析器 (Recursive Descent Parser)
 * 功能描述：基于自顶向下的递归下降分析法。
 * 负责驱动词法分析器获取 Token 流，校验语法合法性；
 * 并在推导过程中同步维护符号表作用域 (Env)，构建抽象语法树 (AST)，最终触发中间代码 (TAC) 的生成。
 */
public class Parser {

	private Lexer lex; // 绑定的底层词法分析器实例
	private Token look; // 向前看符号 (Lookahead Token)，存放当前正待匹配的词法记号
	Env top = null; // 符号表环境：指向当前代码块所在的作用域上下文
	int used = 0; // 存储空间偏移量：记录当前作用域下声明的变量所占用的总字节数

	public Parser(Lexer l) throws IOException {
		lex = l;
		move();
	}

	void move() throws IOException {
		look = lex.scan();
	} // 初始化：预读第一个 Token 放入 look 缓冲区

	/**
	 * 语法错误抛出机制
	 * 利用底层词法分析器记录的行号，实现精准的物理行号定位报错
	 */
	void error(String s) {
		throw new Error("near line " + lex.line + ": " + s);
	}

	/**
	 * 断言与匹配核心机制
	 * 
	 * @param t 语法推导当前所期望出现的词法 Tag
	 */
	void match(int t) throws IOException {
		if (look.tag == t)// 匹配成功，消费该 Token 并向前推进
			move();
		else// 匹配失败，抛出语法错误
			error("syntax error");
	}

	/**
	 * 语法分析总入口 (Start Symbol)
	 * 推导规则：program -> block
	 * 功能描述：启动解析过程，并为整棵抽象语法树分配起始与结束的三地址控制流标号
	 */
	public void program() throws IOException { // program -> block
		// [语法推导] : 解析顶层语句块，构建根节点
		Stmt s = block();
		// [TAC生成准备] : 为整个程序分配全局的进入(L1)和跳出(L2)标号
		int begin = s.newlabel();
		int after = s.newlabel();
		// [中间代码发射] : 打印 L1:，启动 AST 的遍历，最后打印 L2:
		s.emitlabel(begin); // 打印L1，实验3的语法树输出不需要可以将其注释掉
		s.gen(begin, after);
		s.emitlabel(after); // 打印L2，实验3的语法树输出不需要可以将其注释掉
	}

	/**
	 * 语句块与作用域管理解析
	 * 推导规则：block -> { decls stmts }
	 */
	Stmt block() throws IOException { // block -> { decls stmts }
		match('{');
		// [作用域管理] : 进入新大括号，创建新的子环境，并将其 prev 指针指向上一层 (top)，形成链式作用域
		Env savedEnv = top;
		top = new Env(top);
		// [语义状态保存] : 压栈保存外层的偏移量，防止局部变量污染外部空间
		decls();
		Stmt s = stmts();
		// [作用域管理] : 退出大括号，销毁当前局部环境，恢复上一层作用域和偏移量
		match('}');
		top = savedEnv;
		return s;
	}

	/**
	 * 解析变量声明序列 (Declarations)。
	 * 推导规则：decls -> decls decl | ε, 并且 decl -> type ID ;
	 * 功能描述：不断读取基本类型开头的变量声明，将其加入当前环境符号表，并累计占用的内存偏移空间。
	 */
	void decls() throws IOException {

		while (look.tag == Tag.BASIC) { // D -> type ID ;
			Type p = type();
			Token tok = look;
			match(Tag.ID);
			match(';');
			Id id = new Id((Word) tok, p, used);
			top.put(tok, id);
			used = used + p.width;
		}
	}

	/**
	 * 解析数据类型 (Type)。
	 * 推导规则：type -> basic | basic [ num ] ...
	 * 功能描述：解析变量的基本数据类型，如果是数组则进一步解析其维度。
	 */
	Type type() throws IOException {

		Type p = (Type) look; // expect look.tag == Tag.BASIC
		match(Tag.BASIC);
		if (look.tag != '[')
			return p; // T -> basic
		else
			return dims(p); // return array type
	}

	/**
	 * 解析数组维度 (Dimensions)。
	 * 推导规则：dims -> [ num ] | [ num ] dims
	 * 功能描述：递归解析多维数组的大小，构造对应的 Array 类型对象。
	 */
	Type dims(Type p) throws IOException {
		match('[');
		Token tok = look;
		match(Tag.NUM);
		match(']');
		if (look.tag == '[')
			p = dims(p);
		return new Array(((Num) tok).value, p);
	}

	/**
	 * 解析语句序列 (Statements)。
	 * 推导规则：stmts -> stmt stmts | ε
	 */
	Stmt stmts() throws IOException {
		if (look.tag == '}')
			return Stmt.Null;
		else
			return new Seq(stmt(), stmts());
	}

	/**
	 * 控制流语句递归下降核心解析分流
	 * 推导规则：stmt -> expr | if | while | do | break | block | for
	 */
	Stmt stmt() throws IOException {
		Expr x;
		Stmt s, s1, s2;
		// [控制流上下文] : 保存当前的循环包围层，用于 break 语句的跳出目标恢复
		Stmt savedStmt; // save enclosing loop for breaks
		switch (look.tag) {
			case ';':
				move();
				return Stmt.Null;// 空语句

			// [语法推导] : 解析 if(expr) stmt [else stmt]
			case Tag.IF:
				match(Tag.IF);
				match('(');
				x = bool();
				match(')');
				s1 = stmt();
				if (look.tag != Tag.ELSE)
					return new If(x, s1);
				match(Tag.ELSE);
				s2 = stmt();
				return new Else(x, s1, s2);

			// [语法推导] : 解析 for(s1; expr; s2) stmt3；实验3, 4新增的 for 语句解析逻辑
			case Tag.FOR:
				For fornode = new For();
				savedStmt = Stmt.Enclosing;
				Stmt.Enclosing = fornode; // 登记当前的封闭循环，允许内部使用 break

				match(Tag.FOR);
				match('(');

				// 1. 解析初始化语句 (允许为空，例如 for (; i < 10;) )
				s1 = Stmt.Null;
				if (look.tag == Tag.ID) {
					s1 = forassign(); // 妙用：forassign 专门解析不带分号的赋值
				}
				match(';');

				// 2. 解析循环条件 (Decaf规范要求必须有测试条件)
				x = bool();
				match(';');

				// 3. 解析步进语句 (允许为空)
				s2 = Stmt.Null;
				if (look.tag == Tag.ID) {
					s2 = forassign();
				}
				match(')');

				// 4. 解析循环体
				Stmt s3 = stmt();

				// 组装 AST 节点
				fornode.init(s1, x, s2, s3);
				Stmt.Enclosing = savedStmt; // 恢复上一层的循环环境
				return fornode;

			// [语法推导] : 解析 while(expr) stmt
			case Tag.WHILE:
				While whilenode = new While();
				savedStmt = Stmt.Enclosing;
				Stmt.Enclosing = whilenode;
				match(Tag.WHILE);
				match('(');
				x = bool();
				match(')');
				s1 = stmt();
				whilenode.init(x, s1);
				Stmt.Enclosing = savedStmt; // reset Stmt.Enclosing
				return whilenode;

			// [语法推导] : 解析 do stmt while(expr);
			case Tag.DO:
				Do donode = new Do();
				savedStmt = Stmt.Enclosing;
				Stmt.Enclosing = donode;
				match(Tag.DO);
				s1 = stmt();
				match(Tag.WHILE);
				match('(');
				x = bool();
				match(')');
				match(';');
				donode.init(s1, x);
				Stmt.Enclosing = savedStmt; // reset Stmt.Enclosing
				return donode;

			// [语法推导] : 解析 break;
			case Tag.BREAK:
				match(Tag.BREAK);
				match(';');
				return new Break();

			case '{':
				return block();

			default:
				return assign();
		}
	}

	/**
	 * For 循环头部专用的赋值语句解析
	 * 特点：与 assign 相比，不要求末尾有分号，专为 for(i=0;...) 语法定制
	 */
	Stmt forassign() throws IOException {
		Stmt stmt;
		Token t = look;
		match(Tag.ID);
		// [语义检查] : 同理，必须确保 for 循环里的迭代变量已经在外层声明过
		Id id = top.get(t);
		if (id == null)
			error(t.toString() + " undeclared");

		if (look.tag == '=') { // S -> id = E ;
			move();
			stmt = new Set(id, bool());// Set 节点内嵌左右侧类型匹配检查
		} else { // S -> L = E ;
			Access x = offset(id);
			match('=');
			stmt = new SetElem(x, bool());
		}
		return stmt;
	}

	/**
	 * 赋值语句解析与语义检查
	 * 推导规则：assign -> ID = expr ;
	 */
	Stmt assign() throws IOException {
		Stmt stmt;
		Token t = look;
		match(Tag.ID);
		// [语义检查] : 变量使用前查表。在当前及所有外层符号表链中寻找该 ID
		Id id = top.get(t);
		if (id == null)// 若未找到，精准触发“标识符未定义”的语义错误
			error(t.toString() + " undeclared");

		if (look.tag == '=') { // 构造赋值 AST 节点，Set 内部会调用 check() 进行左右类型一致性校验 S -> id = E ;
			move();
			stmt = new Set(id, bool());
		} else { // 处理数组赋值的逻辑 S -> L = E ;
			Access x = offset(id);
			match('=');
			stmt = new SetElem(x, bool());
		}
		match(';'); // 普通赋值语句硬性要求以分号结尾，不以分号结尾的赋值语句会被当作表达式解析，导致语法错误
		return stmt;
	}

	/**
	 * 解析布尔表达式：逻辑或 (||)
	 * 优先级最低。推导规则：bool -> bool || join | join
	 */
	Expr bool() throws IOException {
		Expr x = join();
		while (look.tag == Tag.OR) {
			Token tok = look;
			move();
			x = new Or(tok, x, join());
		}
		return x;
	}

	/**
	 * 解析布尔表达式：逻辑与 (&&)
	 * 优先级次之。推导规则：join -> join && equality | equality
	 */
	Expr join() throws IOException {
		Expr x = equality();
		while (look.tag == Tag.AND) {
			Token tok = look;
			move();
			x = new And(tok, x, equality());
		}
		return x;
	}

	/**
	 * 解析关系表达式部分：等价关系 (==, !=)
	 * 推导规则：equality -> equality == rel | equality != rel | rel
	 */
	Expr equality() throws IOException {
		Expr x = rel();
		while (look.tag == Tag.EQ || look.tag == Tag.NE) {
			Token tok = look;
			move();
			x = new Rel(tok, x, rel());
		}
		return x;
	}

	/**
	 * 解析关系表达式部分：大小关系 (<, <=, >=, >)
	 * 推导规则：rel -> expr < expr | expr <= expr | expr >= expr | expr > expr | expr
	 */
	Expr rel() throws IOException {
		Expr x = expr();
		switch (look.tag) {
			case '<':
			case Tag.LE:
			case Tag.GE:
			case '>':
				Token tok = look;
				move();
				return new Rel(tok, x, expr());
			default:
				return x;
		}
	}

	/**
	 * 解析算术表达式：加减法 (+, -)
	 * 推导规则：expr -> expr + term | expr - term | term
	 */
	Expr expr() throws IOException {
		Expr x = term();
		while (look.tag == '+' || look.tag == '-') {
			Token tok = look;
			move();
			x = new Arith(tok, x, term());
		}
		return x;
	}

	/**
	 * 解析算术项：乘除法 (*, /)
	 * 推导规则：term -> term * unary | term / unary | unary
	 */
	Expr term() throws IOException {
		Expr x = unary();
		while (look.tag == '*' || look.tag == '/') {
			Token tok = look;
			move();
			x = new Arith(tok, x, unary());
		}
		return x;
	}

	/**
	 * 解析一元运算 (-, !)
	 * 推导规则：unary -> - unary | ! unary | factor
	 */
	Expr unary() throws IOException {
		if (look.tag == '-') {
			move();
			return new Unary(Word.minus, unary());
		} else if (look.tag == '!') {
			Token tok = look;
			move();
			return new Not(tok, unary());
		} else
			return factor();
	}

	/**
	 * 解析基本因子 (Factor)
	 * 优先级最高。推导规则：factor -> ( bool ) | loc | num | real | true | false | id
	 * 处理括号表达式、字面量常量、以及变量标识符（包含数组访问）。
	 */
	Expr factor() throws IOException {
		Expr x = null;
		switch (look.tag) {
			case '(':
				move();
				x = bool();
				match(')');
				return x;
			case Tag.NUM:
				x = new Constant(look, Type.Int);
				move();
				return x;
			case Tag.REAL:
				x = new Constant(look, Type.Float);
				move();
				return x;
			case Tag.TRUE:
				x = Constant.True;
				move();
				return x;
			case Tag.FALSE:
				x = Constant.False;
				move();
				return x;
			default:
				error("syntax error");
				return x;
			case Tag.ID:
				String s = look.toString();
				Id id = top.get(look);
				if (id == null)
					error(look.toString() + " undeclared");
				move();
				if (look.tag != '[')
					return id;
				else
					return offset(id);
		}
	}

	/**
	 * 计算数组访问的内存偏移量计算表达树 (Array Access Offset)
	 * 推导规则：loc -> loc [ bool ] | id [ bool ]
	 * 功能描述：支持多维数组下标的访问和偏移地址计算。
	 */
	Access offset(Id a) throws IOException { // I -> [E] | [E] I
		Expr i;
		Expr w;
		Expr t1, t2;
		Expr loc; // inherit id

		Type type = a.type;
		match('[');
		i = bool();
		match(']'); // first index, I -> [ E ]
		type = ((Array) type).of;
		w = new Constant(type.width);
		t1 = new Arith(new Token('*'), i, w);
		loc = t1;
		while (look.tag == '[') { // multi-dimensional I -> [ E ] I
			match('[');
			i = bool();
			match(']');
			type = ((Array) type).of;
			w = new Constant(type.width);
			t1 = new Arith(new Token('*'), i, w);
			t2 = new Arith(new Token('+'), loc, t1);
			loc = t2;
		}

		return new Access(a, loc, type);
	}
}
