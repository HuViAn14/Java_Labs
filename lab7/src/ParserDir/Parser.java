package src.ParserDir;

import src.ErrorHandling.ErrorHandler;
import src.ErrorHandling.ErrorType;
import src.ErrorHandling.ErrorHandler.InterpreterException;

public class Parser {

    final String EOP = "\0";
    ErrorHandler errorHandler;
    private char[] expresion;    // масив з програмою
    private int expresionIdx;    // поточний індекс в програмі
    private String token;   // містить поточку лексему
    private TokenType tokenType;    // містить тип поточної лексеми
    
    public Parser()
    {
        errorHandler=new ErrorHandler();
    }

    // Точка входу
    public double evaluate(char[] exp) throws InterpreterException {
        expresion = exp;
        double result = 0.0;
        getToken();

        if (token.equals(EOP))
            errorHandler.handleErr(ErrorType.NOEXP); // немає виразу

        // починаємо аналіз виразу
        result = evaluateAdditionSubtraction();

        return result;
    }


    // Додавання та віднімання
    private double evaluateAdditionSubtraction() throws InterpreterException {
        char op;
        double result;
        double partialResult;

        result = evaluateDivisionMultiplication();

        while ((op = token.charAt(0)) == '+' || op == '-') {
            getToken();

            partialResult = evaluateDivisionMultiplication();

            switch (op) {
                case '-':
                    result = result - partialResult;
                    break;
                case '+':
                    result = result + partialResult;
                    break;
            }
        }
        return result;
    }

    // множення та ділення
    private double evaluateDivisionMultiplication() throws InterpreterException {
        char op;
        double result;
        double partialResult;

        result = evaluatePower();

        while ((op = token.charAt(0)) == '*' || op == '/' || op == '%') {

            getToken();

            partialResult = evaluatePower();

            switch (op) {
                case '*':
                    result = result * partialResult;
                    break;
                case '/':
                    if (partialResult == 0.0)
                        errorHandler.handleErr(ErrorType.DIVBYZERO);

                    result = result / partialResult;
                    break;
                case '%':
                    if (partialResult == 0.0)
                        errorHandler.handleErr(ErrorType.DIVBYZERO);

                    result = result % partialResult;
                    break;
            }
        }
        return result;
    }

    // піднесення до степені
    private double evaluatePower() throws InterpreterException {
        double result;
        double partialResult;
        double ex;
        int t;

        result = evaluateUnaryPlusMinus();

        if (token.equals("^")) {
            getToken();

            partialResult = evaluatePower();

            ex = result;

            if (partialResult == 0.0)
                result = 1.0;
            else 
                for (t = (int) partialResult - 1; t > 0; t--) 
                    result = result * ex;
        }

        return result;
    }

    // унірні плюс та мінус
    private double evaluateUnaryPlusMinus() throws InterpreterException {
        double result;
        String op;

        op = "";

        if ((tokenType == TokenType.DELIMITER) && token.equals("+") || token.equals("-")) {
            op = token;
            getToken();
        }

        result = evaluateTrigonometry();

        if (op.equals("-"))
            result = -result;

        return result;
    }

     // Тригонометричні функції
     private double evaluateTrigonometry() throws InterpreterException {
        double result;
        String func = "";

        if (tokenType == TokenType.FUNCTION) {
            func = token;
            getToken();
        }

        result = evaluateParantles();

        switch (func) {
            case "Sin":
                result = Math.sin(Math.toRadians(result));
                break;
            case "Cos":
                result = Math.cos(Math.toRadians(result));
                break;
            case "Tan":
                result = Math.tan(Math.toRadians(result));
                break;
            case "Ctg":
                result = 1.0 / Math.tan(Math.toRadians(result));
                break;
        }

        return result;
    }

    private double evaluateParantles() throws InterpreterException {
        double result;

        if (token.equals("(")) {
 
            getToken();
 
            result = evaluateAdditionSubtraction();
 
            if (!token.equals(")")) {
                errorHandler.handleErr(ErrorType.UNBALPARENS);
            }
 
            getToken();
        } else {
            result = atom();
        }
 
 
 
        return result;
 
    }

    private double atom() throws InterpreterException {

        double result = 0.0;

        switch (tokenType) {
            case NUMBER:
                try {
                    result = Double.parseDouble(token);
                } catch (NumberFormatException exc) {

                    errorHandler.handleErr(ErrorType.SYNTAX);
                }
                getToken();
                break;
            default:
                errorHandler.handleErr(ErrorType.SYNTAX);
                break;
        }

        return result;
    }

    // Отримати наступний елемент
    private void getToken() throws InterpreterException {
        char ch;
        tokenType = TokenType.NONE;
        token = "";

        if (expresionIdx == expresion.length) {       // чи не досягнуто кінець програми?
            token = EOP;
            return;
        }

        // пропускаємо пробіли
        while (expresionIdx < expresion.length && isSpace(expresion[expresionIdx]))
            expresionIdx++;

        // Кінець програми
        if (expresionIdx == expresion.length) {
            token = EOP;

            tokenType = TokenType.DELIMITER;

            return;
        }

        if (isDelim(expresion[expresionIdx])) {                   // Оператор
            token += expresion[expresionIdx];

            expresionIdx++;

            tokenType = TokenType.DELIMITER;
        } else if (Character.isDigit(expresion[expresionIdx])) {  // число
            while (!isDelim(expresion[expresionIdx])) {
                token += expresion[expresionIdx];

                expresionIdx++;

                if (expresionIdx >= expresion.length) 
                    break;
            }
            tokenType = TokenType.NUMBER;
        }else if (isFunction()) {  // функція
            while (expresionIdx < expresion.length && Character.isLetter(expresion[expresionIdx])) {
                token += expresion[expresionIdx];
                expresionIdx++;
            }
            tokenType = TokenType.FUNCTION;
        }
         else {                                        // невідомий символ
            token = EOP;

            return;
        }
    }

    // Перевіряє чи символ є розділювачем
    private boolean isDelim(char c) {
        if (("+-/*%^=()".indexOf(c) != -1)) 
            return true;

        return false;
    }

    // Перевіряє чи лексема є функцією
    private boolean isFunction() {
        String[] functions = {"Sin", "Cos", "Tan", "Ctg"};
        for (String func : functions) {
            if (expresion.length - expresionIdx >= func.length()) {
                if (new String(expresion, expresionIdx, func.length()).equalsIgnoreCase(func)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Перевіряє чи символ пробіл чи табуляці
    boolean isSpace(char c) {
        if (c == ' ' || c == '\t') {
            return true;
        }
        return false;
    }   
}
