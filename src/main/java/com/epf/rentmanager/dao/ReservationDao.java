package com.epf.rentmanager.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ReservationDao {

	private final VehicleDao vehicleDao = VehicleDao.getInstance();
	private ClientDao clientDao;
	private static ReservationDao instance = null;
	private ReservationDao() {}
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_AND_CLIENT_QUERY = "SELECT id, debut, fin FROM Reservation WHERE vehicle_id=? AND client_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";

	public int create(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(CREATE_RESERVATION_QUERY, Statement.RETURN_GENERATED_KEYS)) {
			ps.setInt(1, reservation.getClient_id());
			ps.setInt(2, reservation.getVehicle_id());
			ps.setDate(3, Date.valueOf(reservation.getDebut()));
			ps.setDate(4, Date.valueOf(reservation.getFin()));
			ps.executeUpdate();
			ResultSet resultSet = ps.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1);
			} else {
				throw new DaoException("Aucune clé autogénérée de retourné.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}
	
	public int delete(Reservation reservation) throws DaoException {
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(DELETE_RESERVATION_QUERY)) {
			ps.setInt(1, reservation.getId());
			int nbDeletedRows = ps.executeUpdate();
			if (nbDeletedRows == 0) {
				throw new DaoException("Aucune réservation n'a été supprimée.");
			} else {
				return nbDeletedRows;
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
	}

	public List<Reservation> findResaByClientId(int clientId) throws DaoException {
		List<Reservation> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY)) {
			ps.setInt(1, clientId);
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Reservation reservation = new Reservation();
				reservation.setId(resultset.getInt(1));
				reservation.setClient(clientDao.findById(clientId));
				reservation.setVehicle(vehicleDao.findById(resultset.getInt(2)));
				reservation.setDebut(resultset.getDate(3).toLocalDate());
				reservation.setFin(resultset.getDate(4).toLocalDate());
				res.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}
	
	public List<Reservation> findResaByVehicleId(int vehicleId) throws DaoException {
		List<Reservation> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY)) {
			ps.setInt(1, vehicleId);
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Reservation reservation = new Reservation();
				reservation.setId(resultset.getInt(1));
				reservation.setClient(clientDao.findById(resultset.getInt(2)));
				reservation.setVehicle(vehicleDao.findById(vehicleId));
				reservation.setDebut(resultset.getDate(3).toLocalDate());
				reservation.setFin(resultset.getDate(4).toLocalDate());
				res.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}

	public List<Reservation> findResaByVehicleIdAndClientId(int clientId, int vehicleId) throws DaoException {
		List<Reservation> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_AND_CLIENT_QUERY)) {
			ps.setInt(1, vehicleId);
			ps.setInt(2, clientId);
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Reservation reservation = new Reservation();
				reservation.setId(resultset.getInt(1));
				reservation.setClient(clientDao.findById(clientId));
				reservation.setVehicle(vehicleDao.findById(vehicleId));
				reservation.setDebut(resultset.getDate(2).toLocalDate());
				reservation.setFin(resultset.getDate(3).toLocalDate());
				res.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}

	public List<Reservation> findAll() throws DaoException {
		List<Reservation> res = new ArrayList<>();
		try (Connection connection = ConnectionManager.getConnection(); PreparedStatement ps = connection.prepareStatement(FIND_RESERVATIONS_QUERY)) {
			ResultSet resultset = ps.executeQuery();
			while (resultset.next()){
				Reservation reservation = new Reservation();
				reservation.setId(resultset.getInt(1));
				reservation.setClient(clientDao.findById(resultset.getInt(2)));
				reservation.setVehicle(vehicleDao.findById(resultset.getInt(3)));
				reservation.setDebut(resultset.getDate(4).toLocalDate());
				reservation.setFin(resultset.getDate(5).toLocalDate());
				res.add(reservation);
			}
		} catch (SQLException e) {
			throw new DaoException();
		}
		return res;
	}

}
