//Nicholas Yiphoiyen 40117237
//Winkel Yin 40128707
//COMP352, Summer 2020
//Assignment #2, Version 2

/*
 * a) The time complexity of the following algorithm is O(n^2). This is because there are n recursive calls depending on the length of the mathematical expression input, and in each of these
 * recursive calls, there is a for loop search through the operator, thus making the time complexity O(n^2)
 * 
 * The space complexity of the following algorithm is O(n). This is again because there are n recursive calls, so each call adds a layer to the stack, and the number of recursive calls
 * depends on the length of the string input. Thus the space complexity is O(n)
 * 
 * b) the type of recursion used is multiple recursion. This is because there are multiple recursive calls depending on the length of the string input and the number of operators.
 *  Each of the different operators called make different recursions calls, so therefore multiple recursion is implemented in the following algorithm.
 *  This type of recursion mostly impacts space memory, since multiple recursion calls are made, adding a layer to the stack everytime this happens. The time complexity itself isn't too affected by 
 *  this type of recursion, but rather by the statements that happen within the method and its calls.
 */



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class test2 {

	/**
	 * 
	 * @param mathematical expression we want to evaluate
	 * @return
	 */
	public static double calculate(String s) {
		String regex = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
		
		// First and last brackets are '(' and ')' meaning potential wrapping brackets
		if(s.charAt(0) == '(' && s.charAt(s.length()-1) == ')') {
			Stack<Character> bracketStack = new Stack<>();
			bracketStack.push('(');
			for (int i = 1; i < s.length()-1; i++) {
				if(s.charAt(i) == '(')
					bracketStack.push('(');
				else if (s.charAt(i) == ')')
					bracketStack.pop();
				// Found a matching bracket to the first that isn't the last one, first and last aren't matched
				// Proceed to searching for operators
				if(bracketStack.isEmpty())
					break;
			}
			// First and last brackets are matching,
			// call evaluateE with the same string without outer brackets
			if(bracketStack.size() == 1) {
				return calculate(s.substring(1, s.length() - 1));
			}
		}

	    //Base case
	    if (s.matches(regex)) {
	        return Double.parseDouble(s);
	    }
	    
	    int i = 0;
		boolean foundOp = false;
		boolean boolOp = false;

		//search for boolean operator <=, ==, !=, etc...
		for (i = s.length() - 1; i > 0; i--) {
			if (s.charAt(i) == '>' || s.charAt(i) == '<' || s.charAt(i) == '=') {
				foundOp = true;
				boolOp = true;
				break;
			}
		}
	    
	 // search for '+' and '-' first
		if(!foundOp)
			for (i = s.length() - 1; i >= 0; i--) {
				// ignoring '+' and '-' when encountering ')' until finding its matching bracket
				if(s.charAt(i) == ')') {
					Stack<Character> bracketStack = new Stack<>();
					bracketStack.push(')');
					while(!bracketStack.isEmpty()) {
						i--;
						if(s.charAt(i) == ')')
							bracketStack.push(')');
						else if(s.charAt(i) == '(')
							bracketStack.pop();
					}
				}
				if (s.charAt(i) == '+' || s.charAt(i) == '-') {
					foundOp = true;
					break;
				}
			}

	    
	     // no + or - was found, so we search for a * or / now
	    if (!foundOp) {
	        for (i = s.length() - 1; i >= 0; i--) {
				// ignoring '*' and '/' when encountering ')' until finding its matching bracket
				if(s.charAt(i) == ')') {
					Stack<Character> bracketStack = new Stack<>();
					bracketStack.push(')');
					while(!bracketStack.isEmpty()) {
						i--;
						if(s.charAt(i) == ')')
							bracketStack.push(')');
						else if(s.charAt(i) == '(')
							bracketStack.pop();
					}
				}
	            if (s.charAt(i) == '*' || s.charAt(i) == '/') {
	            	foundOp = true;
	                break;
	            }
	        }
	    } 
	    
	    // search for power
	    if(!foundOp) {
		    for (i = 0; i < s.length(); i++) {
				// ignoring '^' when encountering '(' until finding its matching bracket
				if(s.charAt(i) == '(') {
					Stack<Character> bracketStack = new Stack<>();
					bracketStack.push('(');
					while(!bracketStack.isEmpty()) {
						i++;
						if(s.charAt(i) == '(')
							bracketStack.push('(');
						else if(s.charAt(i) == ')')
							bracketStack.pop();
					}
				}
				if ((s.charAt(i) == '^')) {
					foundOp = true;
					break;
				}
		    }
	    }
		//search for negation
		if (!foundOp) {
			for (i = s.length() - 1; i >= 0; i--) {
				if (s.charAt(i) == '_') {
					foundOp = true;
					break;
				}
			}
		}
	    //search for factorial
	    if (!foundOp) {
	    	for (i = s.length() - 1; i >= 0; i--) {
		        if (s.charAt(i) == '!') {
		        	foundOp = true;
		            break;
		        }
		    }
	    }

	    //if no operator was found, an invalid expression was input
	    if (!foundOp) {
			System.out.println("Invalid expression: " + s);
			System.exit(1);
		}
	    String r1;
	    String r2;

	    //separation of the equation depends on the size of the operator(== is two characters compared to * which is only one)
	    double result = 0;
	    String op;
	    if(boolOp) {
			op = s.substring(i - 1, i + 1);
			r1 = s.substring(0, i-1);
			r2 = s.substring(i + 1, s.length());
		}
	    else {
			op = s.substring(i, i + 1);
			r1 = s.substring(0, i);
			r2 = s.substring(i + 1, s.length());
		}

	    //recursive call depending on the operator found
	    switch (op) {
	        case "+":
	            result = calculate(r1) + calculate(r2);
	            break;
	        case "-":
	            result = calculate(r1) - calculate(r2);
	            break;
	        case "*":
	            result = calculate(r1) * calculate(r2);
	            break;
	        case "/":
	            double right = calculate(r2);
	            if (right == 0) //if denominator is zero
	            {
	                System.out.println("Invalid divisor");
	                System.exit(1);
	            } else {
	                result = calculate(r1) / right;
	            }
	            break;
	        case "^":
	        	result = power(calculate(r1), calculate(r2));
	        	break;
	        case "!":
	        	result = factorial(calculate(r1));
	        	break;
			case "_":
				result = -1* calculate(r2);
				break;
			case ">>":
				result = calculate(r1) > calculate(r2) ? 1 : 0;
				break;
			case ">=":
				result = calculate(r1) >= calculate(r2) ? 1 : 0;
				break;
			case "<<":
				result = calculate(r1) < calculate(r2) ? 1 : 0;
				break;
			case "<=":
				result = calculate(r1) <= calculate(r2) ? 1 : 0;
				break;
			case "==":
				result = calculate(r1) == calculate(r2) ? 1 : 0;
				break;
			case "!=":
				result = calculate(r1) != calculate(r2) ? 1 : 0;
				break;
	    }
	    return result;
	}
	
	/**
	 * 
	 * @param base of the expression
	 * @param exp of the expression
	 * @return value of the base^exp
	 */
	public static double power(double base, double exp) {
		double result = base;
		for (int i = 1; i < exp; i++)
			result = result*base;
			
		return result;
	}
	
	/**
	 * 
	 * @param number we want factorial
	 * @return result of the factorial
	 */
	public static double factorial(double n){
		  if (n == 0)    
		    return 1;
		  else    
		    return(n * factorial(n-1));    
		 }    
	
	public static void main(String[] args) {
		
		PrintWriter output = null;
		try {
			 output = new PrintWriter(new FileOutputStream("result.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream("expression.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		output.println("(3+2) = " + calculate("(3+2)"));
		output.println("(3!)^2 = " + calculate("(3!)^2"));
		output.println("(4-5)*2 = " + calculate("(4-5)*2"));
		output.println("18*(19-19) = " + calculate("18*(19-19)"));
		output.println("190*187/122 = " + calculate("190*187/122"));
		output.println("189-13^2 = " + calculate("189-13^2"));
		output.println("98-76+18 = " + calculate("98-76+18"));
		output.println("7*7-2^2 = " + calculate("7*7-2^2"));
		output.println("28-8*2 = " + calculate("28-8*2"));
		output.println("10*(3+2) = " + calculate("10*(3+2)"));
		output.println("(6-3)! = " + calculate("(6-3)!"));
		output.println("8^3 = " + calculate("8^3"));
		output.println("193-293+17 = " + calculate("193-293+17"));
		output.println("-17+14 = " + calculate("-17+14"));
		output.println("24-3 = " + calculate("24-3"));
		
		System.out.println("(3+2) = " + calculate("(3+2)"));
		System.out.println("(3!)^2 = " + calculate("(3!)^2"));
		System.out.println("(4-5)*2 = " + calculate("(4-5)*2"));
		System.out.println("18*(19-19) = " + calculate("18*(19-19)"));
		System.out.println("190*187/122 = " + calculate("190*187/122"));
		System.out.println("189-13^2 = " + calculate("189-13^2"));
		System.out.println("98-76+18 = " + calculate("98-76+18"));
		System.out.println("7*7-2^2 = " + calculate("7*7-2^2"));
		System.out.println("28-8*2 = " + calculate("28-8*2"));
		System.out.println("10*(3+2) = " + calculate("10*(3+2)"));
		System.out.println("(6-3)! = " + calculate("(6-3)!"));
		System.out.println("8^3 = " + calculate("8^3"));
		System.out.println("193-293+17 = " + calculate("193-293+17"));
		System.out.println("-17+14 = " + calculate("-17+14"));
		System.out.println("24-3 = " + calculate("24-3"));
		
		String FromFile = input.next();
		output.println(FromFile + " = " + calculate(FromFile));
		System.out.println(FromFile + " = " + calculate(FromFile));
		System.out.println("written in a text file named \"math.txt\"");
		
		input.close();
		output.close();
		
	}

}
