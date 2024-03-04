package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
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
            int reservationVehicleId = reservation.getVehicle_id();
            int reservationClientId = reservation.getClient_id();
            LocalDate dateDebut = reservation.getDebut();

            try {
                clientDao.findById(reservationClientId);
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le client donné n'existe pas.");
            }
            try {
                vehicleDao.findById(reservationVehicleId);
            } catch (DaoException e) {
                throw new ServiceException("Impossible de créer la réservation, le véhicule donné n'existe pas.");
            }
            List<Reservation> allVehicleReservations = reservationDao.findResaByVehicleId(reservationVehicleId);
            for (Reservation reservation1 : allVehicleReservations) {
                if ((reservation1.getDebut()).equals(dateDebut)) {
                    // Besoin de fixer la condition car celle-ci ne prend pas en compte si un vehicule est reserve sur plusieurs jours.
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

    public List<Reservation> findResaByVehicleIdAndClientId(int clientId, int vehicleId) throws ServiceException {
        try {
            return reservationDao.findResaByVehicleIdAndClientId(clientId, vehicleId);
        } catch (DaoException e) {
            throw new ServiceException("Aucune réservation n'a a été trouvée pour le véhicule et le client donnés.");
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
