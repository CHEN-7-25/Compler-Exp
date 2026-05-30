package lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

import symbols.Type;

/**
 * 词法分析器 (Scanner / Lexer)
 * 功能描述：负责读取源代码字符流，过滤空白与注释，并将字符组合成具有独立语义的 Token 序列。
 * 同时肩负着拦截非法字符并报错的第一道防线任务。
 */
public class Lexer {
	/**
	 * 静态全局变量，记录当前词法扫描器所处的物理行号。
	 * 用于在词法、语法、语义报错时，提供精准的行号定位。
	 */
	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();
	Reader reader = null;

	void reserve(Word w) {
		words.put(w.lexeme, w);
	}

	/**
	 * 词法分析器构造函数
	 * 功能描述：初始化输入流，并将 Decaf 语言的所有保留字预加载到符号表中
	 * 
	 * @param r 绑定的源代码文件输入流读取器
	 */
	public Lexer(Reader r) {
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));
		reserve(new Word("for", Tag.FOR));

		reserve(Word.True);
		reserve(Word.False);

		reserve(Type.Int);
		reserve(Type.Char);
		reserve(Type.Bool);
		reserve(Type.Float);
		reader = r;
	}

	void readch() throws IOException {
		// peek = (char)System.in.read();
		peek = (char) reader.read();
	}

	/**
	 * 条件探索读取：判断流中的下一个字符是否为期待的字符 c
	 * 
	 * @return 如果匹配则消费该字符并返回 true；否则回退并返回 false
	 */
	boolean readch(char c) throws IOException {
		readch();
		if (peek != c)
			return false;
		peek = ' ';
		return true;
	}

	/**
	 * 核心驱动方法：执行一次词法扫描，返回下一个合法的 Token
	 * 功能描述：基于有限状态机（DFA）原理，对输入的字符流进行切词
	 * 
	 * @return 成功解析出的 Token 对象（如 Word, Num, Real 或单字符界符）
	 */
	public Token scan() throws IOException {
		// [状态机-阶段1] : 过滤无意义的空白字符、制表符，并维护源码行号
		for (;; readch()) {
			if (peek == ' ' || peek == '\t' || peek == '\r')
				continue;
			else if (peek == '\n')
				line = line + 1;// 遇到换行符，行号递增
			else
				break;
		}

		// [状态机-阶段2 (实验新增)] : 拦截并过滤单行注释
		if (peek == '/') {
			if (readch('/')) {
				// 如果连续读到两个 '/'，说明是单行注释，循环读取直到整行结束或文件结束
				while (peek != '\n' && peek != (char) 65535) {
					readch();
				}
				if (peek == '\n') {
					line = line + 1; // 行号递增
					peek = ' '; // 清空 peek 缓冲区
				}
				return scan(); // 关键：跳过注释后，递归调用 scan() 接着寻找下一个真实 Token
			} else {
				// 如果后面跟着的不是 '/'，说明它只是一个单纯的算术除号 '/'
				Token tok = new Token('/');
				return tok;
			}
		}
		// [状态机-阶段3] : 识别复合双字符算符 (如 &&, ||, ==, !=, <=, >=)
		switch (peek) {
			case '&':
				if (readch('&'))
					return Word.and;
				else
					return new Token('&');
			case '|':
				if (readch('|'))
					return Word.or;
				else
					return new Token('|');
			case '=':
				if (readch('='))
					return Word.eq;
				else
					return new Token('=');
			case '!':
				if (readch('='))
					return Word.ne;
				else
					return new Token('!');
			case '<':
				if (readch('='))
					return Word.le;
				else
					return new Token('<');
			case '>':
				if (readch('='))
					return Word.ge;
				else
					return new Token('>');
		}

		// [状态机-阶段4] : 解析数值常量字面量 (支持整数 Num 与浮点数 Real)
		if (Character.isDigit(peek)) {
			int v = 0;
			do {
				v = 10 * v + Character.digit(peek, 10);
				readch();
			} while (Character.isDigit(peek));
			// 判断是否有小数点，若无则返回整数常量 Token；若有则继续解析浮点数部分
			if (peek != '.')
				return new Num(v);
			// 存在小数点，继续解析小数部分并组合为实数/浮点数
			float x = v;
			float d = 10;
			for (;;) {
				readch();
				if (!Character.isDigit(peek))
					break;
				x = x + Character.digit(peek, 10) / d;
				d = d * 10;
			}
			return new Real(x);
		}

		// [状态机-阶段5] : 提取标识符与保留字
		if (Character.isLetter(peek)) {
			StringBuffer b = new StringBuffer();
			do {
				b.append(peek);
				readch();
			} while (Character.isLetterOrDigit(peek));
			String s = b.toString();
			// 去符号表中查表：若存在，说明是保留字或之前见过的标识符
			Word w = (Word) words.get(s);
			if (w != null)
				return w;
			// 不存在，说明是新标识符，创建一个新的 Word Token，并加入符号表
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}

		/* 词法错误检测：给出有意义的错误信息和错误发生的行号。例如字符@并非decaf程序中的合法符号，若这个字符在注释以外出现，则提示一个词法错误。 */
		// 定义 Decaf 中允许的所有合法单字符符号
		String validSymbols = "+-*/%()[]{};,.";

		// 1. 如果是合法的单字符符号，或者是文件结束符(EOF, \uFFFF)，正常交付 Token
		if (validSymbols.indexOf(peek) != -1 || peek == (char) 65535) {
			Token tok = new Token(peek);
			peek = ' ';
			return tok;
		}

		// 2. 如果运行到这里，说明遇到了注释以外的非法字符（例如 @）
		// 抛出包含行号的词法错误信息
		throw new Error("Lexical error near line " + line + ": Illegal character '" + peek + "'");
	}
}
