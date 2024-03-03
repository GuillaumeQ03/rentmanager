package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ReservationService {

    private final ClientDao clientDao = ClientDao.getInstance();
    private final VehicleDao vehicleDao = VehicleDao.getInstance();
    private final ReservationDao reservationDao;
    public static ReservationService instance;

    private ReservationService() {
        this.reservationDao = ReservationDao.getInstance();
    }

    public static ReservationService getInstance() {
        if (instance == null) {
            instance = new ReservationService();
        }

        return instance;
    }

    public int create(Reservation reservation) throws ServiceException {
        try {
            try {
                clientDao.findById(reservation.getClient_id());
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le client donné n'existe pas.");
            }
            try {
                vehicleDao.findById(reservation.getVehicle_id());
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le véhicule donné n'existe pas.");
            }
            int reservationVehicleId = reservation.getVehicle_id();
            LocalDate dateDebut = reservation.getDebut();
            List<Reservation> allReservations = reservationDao.findAll();
            for (Reservation reservation1 : allReservations) {
                if ((reservation1.getDebut()).equals(dateDebut) && reservation1.getVehicle_id() == reservationVehicleId) {
                    throw new ServiceException("La voiture sélectionnée est déjà réservée sur cette journée. Veuillez choisir une autre date.");
                }
            }
            return reservationDao.create(reservation);
        } catch (DaoException e) {
            e.printStackTrace();
            throw new ServiceException("Impossible de créer une nouvelle réservation.");
        }
    }

    public int delete(Reservation reservation) throws ServiceException {
        try {
            return reservationDao.delete(reservation);
        } catch (DaoException e) {
            throw new ServiceException("Impossible de supprimer la réservation de la base de données.");
        }
    }

    public List<Reservation> findResaByClientId(int clientId) throws ServiceException {
        try {
            return reservationDao.findResaByClientId(clientId);
        } catch (DaoException e) {
            throw new ServiceException("Aucune réservation n'a a été trouvée pour le client donné.");
        }
    }

    public List<Reservation> findResaByVehicleId(int vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleId(vehicleId);
        } catch (DaoException e) {
            throw new ServiceException("Aucune réservation n'a a été trouvée pour le véhicule donné.");
        }
    }

    public List<Reservation> findAll() throws ServiceException {
        try {
            return reservationDao.findAll();
        } catch (DaoException e) {
            throw new ServiceException("Aucune réservation trouvée.");
        }
    }

}
