package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exception.DaoException;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {

	private ClientDao() {}
//	private static ClientDao instance = null;
//	public static ClientDao getInstance() {
//		if(instance == null) {
//			instance = new ClientDao();
//		}
//		return instance;
//	}
	
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	
	public int create(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(CREATE_CLIENT_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, client.getNom());
			ps.setString(2, client.getPrenom());
			ps.setString(3, client.getEmail());
			ps.setDate(4, Date.valueOf(client.getDateNaissance()));
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException("Aucune clé autogénérée de retourné.");
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}
	
	public int delete(Client client) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_CLIENT_QUERY)) {
			ps.setInt(1, client.getId());
			int nbDeletedRows = ps.executeUpdate();
			if (nbDeletedRows == 0) {
				throw new DaoException("Aucun client n'a été supprimé.");
			} else {
				return nbDeletedRows;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Client findById(int id) throws DaoException {
		Client res = new Client();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_CLIENT_QUERY)) {
			ps.setInt(1, id);
			ResultSet resultset = ps.executeQuery();
			if (resultset.next()){
				res.setId(resultset.getInt(1));
				res.setNom(resultset.getString(2));
				res.setPrenom(resultset.getString(3));
				res.setEmail(resultset.getString(4));
				res.setDateNaissance(resultset.getDate(5).toLocalDate());
			} else {
				throw new DaoException("Le client recherché n'existe pas.");
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}

	public List<Client> findAll() throws DaoException {
		List<Client> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_CLIENTS_QUERY)) {
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Client client = new Client();
				client.setId(resultset.getInt(1));
				client.setNom(resultset.getString(2));
				client.setPrenom(resultset.getString(3));
				client.setEmail(resultset.getString(4));
				client.setDateNaissance(resultset.getDate(5).toLocalDate());
				res.add(client);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return res;
	}

}
