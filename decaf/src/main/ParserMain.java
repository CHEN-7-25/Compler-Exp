package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import parser.Parser;
import lexer.Lexer;

/**
 * 解析器的主启动类。
 * 包含应用程序入口，负责串联词法分析器(Lexer)和语法分析器(Parser)以运行实验程序。
 */
public class ParserMain {

	/**
	 * 主函数入口。
	 * 读取测试源码文件，依次初始化词法器和语法器，最后触发由顶向下的语法分析（program）。
	 */
	public static void main(String[] args) throws IOException {
		File file = new File("test/test1.txt");
		Reader reader = null;
		reader = new InputStreamReader(new FileInputStream(file));
		Lexer lex = new Lexer(reader);
		Parser parser = new Parser(lex);
		parser.program();
		System.out.print("\n");
	}

}
