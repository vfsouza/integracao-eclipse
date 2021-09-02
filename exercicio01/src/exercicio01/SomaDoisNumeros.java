package exercicio01;
import java.util.*;

class SomaDoisNumeros {
	// Objeto Scanner para entrada de dados
	public static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		int num1, num2, soma;
		
		// Entrada do primeiro número da soma
		System.out.println("Digite um numero");
		num1 = sc.nextInt();
		
		// Entrada do segundo número da soma
		System.out.println("Digite outro numero");
		num2 = sc.nextInt();
		
		soma = num1 + num2;
		
		// Saída da soma dos dois números
		System.out.println("Soma: " + soma);
	}
}
