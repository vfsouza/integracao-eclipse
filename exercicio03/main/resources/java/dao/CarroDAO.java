package dao;

import model.Carro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;


public class CarroDAO {
	private List<Carro> produtos;
	private int maxId = 0;

	private File file;
	private FileOutputStream fos;
	private ObjectOutputStream outputFile;
	
	private Connection conexao;

	public int getMaxId() {
		return maxId;
	}

	public CarroDAO() {
		conexao = null;
	}

	public boolean conectar() {
		String driverName = "org.postgresql.Driver";                    
		String serverName = "localhost";
		String mydatabase = "integracao-eclipse";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta +"/" + mydatabase;
		String username = "postgres";
		String password = "679165";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) { 
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}
	
	public boolean close() {
		boolean status = false;
		
		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public void add(Carro p) {
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO Carros (id, descricao, preco, quantidade, data_fab, data_val) "
					       + "VALUES (" + p.getId() + ", '" + p.getDescricao() + "', '"  
					       + p.getPreco() + "', '" + p.getQuant() + "', '" + p.getDataFabricacao()
							 + "', '" + p.getDataValidade() + "');");
			st.close();
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
	}

	public Carro get(int id) {
		Carro produto = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			Carro carro = st.executeQuery("SELECT id, descricao, preco, quantidade, data_fab, data_val FROM carro WHERE id = " + id);
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return carro;
	}

	public void update(Carro p) {
		try {  
			Statement st = conexao.createStatement();
			String sql = "UPDATE carro SET descricao = '" + p.getDescricao() + "', preco = '"  
				       + p.getPreco() + "', quantidade = '" + p.getQuant() + "', data_fab = '"
						 + p.getDataFabricacao() + "', data_val = '" + p.getDataValidade() + "'"
					    + " WHERE id = " + p.getId();
			st.executeUpdate(sql);
			st.close();
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
	}

	public void remove(Carro p) {
		try {  
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM carro WHERE id = " + p.getId());
			st.close();
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
	}

	public List<Carro> getAll() {
		return produtos;
	}

	private List<Carro> readFromFile() {
		produtos.clear();
		Carro produto = null;
		try (FileInputStream fis = new FileInputStream(file);
				ObjectInputStream inputFile = new ObjectInputStream(fis)) {

			while (fis.available() > 0) {
				produto = (Carro) inputFile.readObject();
				produtos.add(produto);
				maxId = (produto.getId() > maxId) ? produto.getId() : maxId;
			}
		} catch (Exception e) {
			System.out.println("ERRO ao gravar produto no disco!");
			e.printStackTrace();
		}
		return produtos;
	}

	private void saveToFile() {
		try {
			fos = new FileOutputStream(file, false);
			outputFile = new ObjectOutputStream(fos);

			for (Carro produto : produtos) {
				outputFile.writeObject(produto);
			}
			outputFile.flush();
			this.close();
		} catch (Exception e) {
			System.out.println("ERRO ao gravar produto no disco!");
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			this.saveToFile();
			this.close();
		} catch (Exception e) {
			System.out.println("ERRO ao salvar a base de dados no disco!");
			e.printStackTrace();
		}
	}
}