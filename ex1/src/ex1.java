import java.util.Scanner;

class Ex1 {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Digite o primeiro número: ");
        int num1 = sc.nextInt();

        System.out.print("Digite o segundo número: ");
        int num2 = sc.nextInt();

        int soma = num1 + num2;

        System.out.println("A soma dos dois números é: " + soma);
        
        sc.close();
    }
}
