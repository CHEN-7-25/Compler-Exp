package lexer;

import java.io.IOException;
import java.io.Reader;
import java.util.Hashtable;

import symbols.Type;

/**
 * 类 Lexer 功能说明：
 * 核心作用：提供前端编译所需的抽象表示与操作
 */
public class Lexer {
	public static int line = 1;
	char peek = ' ';
	Hashtable words = new Hashtable();
	Reader reader = null;
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	void reserve(Word w)	{ words.put(w.lexeme, w); }
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Lexer(Reader r)	{
		reserve(new Word("if", Tag.IF));
		reserve(new Word("else", Tag.ELSE));
		reserve(new Word("while", Tag.WHILE));
		reserve(new Word("do", Tag.DO));
		reserve(new Word("break", Tag.BREAK));
		reserve(new Word("for", Tag.FOR));
		
		reserve(Word.True); reserve(Word.False);
		
		reserve(Type.Int); reserve(Type.Char);
		reserve(Type.Bool); reserve(Type.Float);
		reader = r;
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	void readch() throws IOException {
		//peek = (char)System.in.read();
		peek = (char)reader.read();
	}
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	boolean readch(char c) throws IOException{
		readch();
		if(peek!=c) return false;
		peek = ' ';
		return true;
	}
	
    /**
     * 方法 None 功能：
     * 输入：参数列表
     * 输出：返回值或无
     * 关键逻辑：执行相关编译解析步骤
     */
	public Token scan() throws IOException{
		for(;;readch()){
			if(peek==' '||peek=='\t'||peek=='\r') continue;
			else if(peek=='\n') line = line + 1;
			else break;
		}
		switch(peek){
		case '&':
			if(readch('&')) return Word.and; else return new Token('&');
		case '|':
			if(readch('|')) return Word.or; else return new Token('|');
		case '=':
			if(readch('=')) return Word.eq; else return new Token('=');
		case '!':
			if(readch('=')) return Word.ne; else return new Token('!');
		case '<':
			if(readch('=')) return Word.le; else return new Token('<');
		case '>':
			if(readch('=')) return Word.ge; else return new Token('>');
		}
		
		if(Character.isDigit(peek)){
			int v = 0;
			do{
				v=10*v+Character.digit(peek, 10); readch();
			}while(Character.isDigit(peek));
			if(peek!='.') return new Num(v);
			float x = v; float d = 10;
			for(;;){
				readch();
				if(!Character.isDigit(peek)) break;
				x = x + Character.digit(peek, 10)/d; d=d*10;
			}
			return new Real(x);
		}
		
		if(Character.isLetter(peek)){
			StringBuffer b = new StringBuffer();
			do{
				b.append(peek); readch();
			}while(Character.isLetterOrDigit(peek));
			String s=b.toString();
			Word w = (Word)words.get(s);
			if(w!=null) return w;
			w = new Word(s, Tag.ID);
			words.put(s, w);
			return w;
		}

		/* 词法错误检测：给出有意义的错误信息和错误发生的行号。例如字符@并非decaf程序中的合法符号，若这个字符在注释以外出现，则提示一个词法错误。*/	
		// 定义 Decaf 中允许的所有合法单字符符号
		String validSymbols = "+-*/%()[]{};,.";
		
		// 1. 如果是合法的单字符符号，或者是文件结束符(EOF, \uFFFF)
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
