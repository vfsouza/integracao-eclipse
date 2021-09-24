package exercicio2;

import java.io.*;

public class Principal {
	
	public static void main(String[] args) throws IOException {
		
		DAO dao = new DAO();
		dao.conectar();
		
		Usuarios usuario = new Usuarios();
		
		int option;
		
		do{
			
			int code = 0;
			System.out.println("\n====== Data Base CRUD ======");
			System.out.println("1) Listar\n2) Inserir\n3) Deletar\n4) Update\n5) Sair");
			option = MyIO.readInt();
			
			
			switch (option){
			
			case 1:
				Usuarios[] usuarios = dao.getUsuarios();
					
				System.out.println("\n====== Users List ======");		
				for(int i = 0; i < usuarios.length; i++) {
					System.out.println(usuarios[i].toString());
				}
				break;
					
			case 2:
				String name;
				String password;
				char gen;
					
				System.out.println("\n====== Insert a User ======");
				System.out.print("Code: ");
				code = MyIO.readInt();
					
				System.out.print("Name: ");
				name = MyIO.readLine();
					
				System.out.print("Password: ");
				password = MyIO.readLine();
				
				System.out.print("Genre (M or F): ");
				gen = MyIO.readChar();
				
				usuario = new Usuarios(code, name, password, gen);
				if(dao.inserirUsuario(usuario) == true) {
					System.out.println("Inserção com sucesso -> " + usuario.toString());
				}
				break;
					
			case 3:
				MyIO.println("\n====== Delete a User ======");
				MyIO.println("\nCode: ");
				code = MyIO.readInt();
				
				dao.excluirUsuario(code);
				break;
					
			case 4:
				String newPass;
				
				MyIO.println("\n====== Update a User ======");			
				MyIO.println("New password: ");
				newPass = MyIO.readLine();
				
				usuario.setSenha(newPass);
				dao.atualizarUsuario(usuario);
				break;
				
			case 5:
				MyIO.println("Saindo...");
				break;
			}
		} while(option != 5);
		
		System.out.print("\n====== End of Code ======");
		dao.close();
	}
}
