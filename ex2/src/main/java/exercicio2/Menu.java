package exercicio2;

import java.util.*;
public class Menu 
{
    public static void main( String[] args )
    {

          ProdutoDAO produtodao=  new ProdutoDAO();
	
		
		  String mensagem = produtodao.conectar(); // Conectar ao banco de dados
	      System.out.println(mensagem); // Mostrar a mensagem de conexão

          Scanner sc = new Scanner(System.in);
          int opcao;
         


        do{
        System.out.println( "---------------------------------" );
        System.out.println( "Digite 1 para incluir um produto" );
        System.out.println( "Digite 2 para Deletar um produto" );
        System.out.println( "Digite 3 para Alterar um produto" );
        System.out.println( "Digite 4 para Listar  os produto" );
        System.out.println( "Digite 0 para encerrar o programa" );
        System.out.println( "---------------------------------" );

        opcao = sc.nextInt();
        sc.nextLine(); 


        if(opcao==1){

            System.out.println("digite o nome do produto");
            String nome =sc.nextLine();

            System.out.println("digite o valo do produto");
            int valor =sc.nextInt();

            Produto produto = new Produto(nome,valor);

            produtodao.incluir(produto);

        }else if(opcao==2){
            System.out.println("digite o nome do produto a ser deletado");
            String nome =sc.nextLine();
            produtodao.deletar(nome);

        }else if(opcao==3){
            System.out.println("digite o nome do produto a ser atualizado");
            String nome =sc.nextLine();
            System.out.println("digite o novo valor do produto a ser atualizado");
            int valor =sc.nextInt();
            produtodao.atualizar(nome,valor);


        }else if(opcao==4){
            produtodao.listar();

        }else if(opcao==0){
            System.err.println("programa encerrado");
        }else{
            System.err.println("opção invalida");
        }
    }while(opcao!=0);



      sc.close();
    }



    
}
