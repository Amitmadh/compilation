public class TokenNames {
  /* terminals */
  public static final int LPAREN = 1;
  public static final int RPAREN = 2;
  public static final int LBRACK= 3;
  public static final int RBRACK= 4;
  public static final int LBRACE= 5;
  public static final int RBRACE= 6;
  public static final int PLUS = 7;
  public static final int MINUS = 8;
  public static final int TIMES = 9;
  public static final int DIVIDE = 10;
  public static final int COMMA = 11;
  public static final int DOT = 12;
  public static final int SEMICOLON = 13;
  public static final int TYPE_INT = 14;
  public static final int TYPE_STRING = 15;
  public static final int TYPE_VOID = 16;
  public static final int ASSIGN = 17;
  public static final int EQ = 18;
  public static final int LT = 19;
  public static final int GT = 20;
  public static final int ARRAY = 21;
  public static final int CLASS = 22;
  public static final int RETURN = 23;
  public static final int WHILE = 24;
  public static final int IF = 25;
  public static final int ELSE = 26;
  public static final int NEW = 27;
  public static final int EXTENDS = 28;
  public static final int NIL = 29;
  public static final int INT = 30;
  public static final int STRING = 31;
  public static final int ID = 32;
  public static final int EOF = 0;
    
  public static String getTokenName(int n) {
    switch (n) {
      case 0:
        return "EOF";
      case 1:
        return "LPAREN";
      case 2:
        return "RPAREN";
      case 3:
        return "LBRACK";
      case 4:
        return "RBRACK";
      case 5:
        return "LBRACE";
      case 6:
        return "RBRACE";
      case 7:
        return "PLUS";
      case 8:
        return "MINUS";
      case 9:
        return "TIMES";
      case 10:
        return "DIVIDE";
      case 11:
        return "COMMA";
      case 12:
        return "DOT";
      case 13:
        return "SEMICOLON";
      case 14:
        return "TYPE_INT";
      case 15:
        return "TYPE_STRING";
      case 16:
        return "TYPE_VOID";
      case 17:
        return "ASSIGN";
      case 18:
        return "EQ";
      case 19:
        return "LT";
      case 20:
        return "GT";
      case 21:
        return "ARRAY";
      case 22:
        return "CLASS";
      case 23:
        return "RETURN";
      case 24:
        return "WHILE";
      case 25:
        return "IF";
      case 26:
        return "ELSE";
      case 27:
        return "NEW";
      case 28:
        return "EXTENDS";
      case 29:
        return "NIL";
      case 30:
        return "INT";
      case 31:
        return "STRING";
      case 32:
        return "ID";
      default:
        return "ERROR";
    }
  }

}
