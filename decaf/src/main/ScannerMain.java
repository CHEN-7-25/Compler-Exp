package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import lexer.Lexer;
import lexer.Token;
import lexer.Tag;

public class ScannerMain {

    // 辅助方法：将数字形式的 Tag 转换为直观的字符串 Kind 名称
    private static String getKindName(Token tok) {
        switch (tok.tag) {
            case Tag.AND:   return "AND";
            case Tag.BASIC: return "BASIC";
            case Tag.BREAK: return "BREAK";
            case Tag.DO:    return "DO";
            case Tag.ELSE:  return "ELSE";
            case Tag.EQ:    return "EQ";
            case Tag.FALSE: return "FALSE";
            case Tag.GE:    return "GE";
            case Tag.ID:    return "ID";
            case Tag.IF:    return "IF";
            case Tag.INDEX: return "INDEX";
            case Tag.LE:    return "LE";
            case Tag.MINUS: return "MINUS";
            case Tag.NE:    return "NE";
            case Tag.NUM:   return "NUM";
            case Tag.OR:    return "OR";
            case Tag.REAL:  return "REAL";
            case Tag.TEMP:  return "TEMP";
            case Tag.TRUE:  return "TRUE";
            case Tag.WHILE: return "WHILE";
            case Tag.FOR:   return "FOR";
            default:
                // 所有单字符运算符和标点符号（如 '+', '-', '*', '/', '=', '<', '>', '{', '}', '(', ')', ';' 等）
                // 它们的 tag 对应其 ASCII 码值，在这里统一归类输出为 SYM
                return "SYMBOL";
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\02_Study\\Experiments\\编译技术\\01_Codes\\decaf\\test\\test1.txt");
        Reader reader = new InputStreamReader(new FileInputStream(file));

        Lexer lex = new Lexer(reader);

        System.out.println("====== Scanner (Kind, Value) 序列输出 ======");

        try {
            while (true) {
                Token tok = lex.scan();

                // 文件结束符判定
                if (tok.tag == 65535) {
                    break;
                }

                String kind = getKindName(tok);
                String value = tok.toString();

                // 格式化输出为 (Kind, Value)
                System.out.println("(" + kind + ", " + value + ")");
            }
        } catch (Exception e) {
            System.out.println("词法分析遇到异常。");
        }

        System.out.println("=========================================");
    }
}