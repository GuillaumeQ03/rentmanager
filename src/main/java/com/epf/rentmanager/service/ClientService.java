package com.epf.rentmanager.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;

public class ClientService {

	private final ReservationDao reservationDao = ReservationDao.getInstance();
	private final ClientDao clientDao;
	public static ClientService instance;
	
	private ClientService() {
		this.clientDao = ClientDao.getInstance();
	}
	
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}
	
	public int create(Client client) throws ServiceException {
		try {
			String nom = client.getNom();
			String prenom = client.getPrenom();
			LocalDate dateNaissance = client.getDateNaissance();
			String email = client.getEmail();
			System.out.println(email);

			List<Client> allClients = clientDao.findAll();
			System.out.println(allClients);
			for (Client clientResearched : allClients) {
				System.out.println(clientResearched.getEmail());
				if (Objects.equals(clientResearched.getEmail(), email)) {
					throw new ServiceException("Cet email est déjà utilisé. Merci d'en choisir un autre.");
				}
			}

			if (nom == null || prenom == null || nom.length() < 3 || prenom.length() < 3 || ((LocalDate.now()).minusYears(18)).isBefore(dateNaissance)) {
				throw new ServiceException("Le client doit posséder un nom, un prénom et avoir plus de 18 ans.");
			} else {
				client.setNom(client.getNom().toUpperCase());
				return clientDao.create(client);
			}
		} catch  (DaoException e) {
			throw new ServiceException("Impossible de créer un nouveau client.");
		}
	}

	public int delete(Client client) throws ServiceException {
		/**
		 * Cette fonction regarde dans un premier temps s'il existe des reservations actives avec le client donné.
		 * Cette fonction supprime dans un second temps toutes les réservations liées au client donné.
		 * Cette fonction supprime dans un troisième temps le client donné.
		 */
		try {
			List<Reservation> clientReservations = reservationDao.findResaByClientId(client.getId());
			if (!clientReservations.isEmpty()) {
				for (Reservation reservation : clientReservations) {
					if (reservation.getDebut().isBefore(LocalDate.now()) && reservation.getFin().isAfter(LocalDate.now())) {
						throw new ServiceException("Le client fait partie d'une réservation en cours. Il est donc impossible de le supprimer pour le moment.");
					}
				}
				int deleted_reservations = 0;
				for (Reservation reservation : clientReservations) {
					deleted_reservations += reservationDao.delete(reservation);
				}
				if (deleted_reservations != clientReservations.size()) {
					throw new ServiceException("Toutes les réservations du client n'ont pas pu être supprimées de la base de données.");
				}
			}
		} catch (DaoException e) {
			throw new ServiceException("Problème(s) rencontré(s) lors du checking des réservations actives du client.");
		}
		try {
			return clientDao.delete(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException("Impossible de supprimer le client de la base de données.");
		}
	}

	public Client findById(int id) throws ServiceException {
		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			throw new ServiceException("Aucun client trouvé.");
		}
	}

	public List<Client> findAll() throws ServiceException {
		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			throw new ServiceException("Aucun client trouvé.");
		}
	}
	
}
