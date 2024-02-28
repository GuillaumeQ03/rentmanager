package com.epf.rentmanager;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import java.time.LocalDate;
import java.util.List;

public class main {

    public static ClientService clientService = ClientService.getInstance();
    public static ReservationService reservationService = ReservationService.getInstance();
    public static VehicleService vehicleService = VehicleService.getInstance();

    public static void main(String[] args) {
        List<Client> listClient;
        List<Vehicle> listVehicle;
        List<Reservation> listReservation;
        LocalDate date1 = LocalDate.of(2003, 9, 3);
        LocalDate date2 = LocalDate.of(2005, 8, 21);
        LocalDate date3 = LocalDate.of(2025, 10, 30);
        Client client1 = new Client(1, "Guillaume", "QUINTIN", "guillaume.quintin@epfedu.fr", date1);
        Vehicle vehicle1 = new Vehicle(2, "Nissan", 4);
        Reservation reservation1 = new Reservation(1, client1, vehicle1, date1, date2);
        int IDClientsCreated;
        int nbClientsDeleted;
        int IDVehiclesCreated;
        int nbVehiclesDeleted;
        int IDReservationsCreated;
        int nbReservationsDeleted;
        try {
            listReservation = reservationService.findAll();
            System.out.println(listReservation);
            listClient = clientService.findAll();
            System.out.println(listClient);

//            IDClientsCreated = clientService.create(client1);
//            System.out.println(IDClientsCreated);
//            listClient = clientService.findAll();
//            System.out.println(listClient);

//            nbClientsDeleted = clientService.delete(client1);
//            System.out.println(nbClientsDeleted);
//            listClient = clientService.findAll();
//            System.out.println(listClient);


            listVehicle = vehicleService.findAll();
            System.out.println(listVehicle);

//            IDVehiclesCreated = vehicleService.create(vehicle1);
//            System.out.println(IDVehiclesCreated);
//            listVehicle = vehicleService.findAll();
//            System.out.println(listVehicle);

//            nbVehiclesDeleted = vehicleService.delete(vehicle1);
//            System.out.println(nbVehiclesDeleted);
//            listVehicle = vehicleService.findAll();
//            System.out.println(listVehicle);


            listReservation = reservationService.findAll();
            System.out.println(listReservation);

//            IDReservationsCreated = reservationService.create(reservation1);
//            System.out.println(IDReservationsCreated);
//            listReservation = reservationService.findAll();
//            System.out.println(listReservation);

//            nbReservationsDeleted = reservationService.delete(reservation1);
//            System.out.println(nbReservationsDeleted);
//            listReservation = reservationService.findAll();
//            System.out.println(listReservation);

//            listReservation = reservationService.findResaByVehicleId(5);
//            System.out.println(listReservation);
//            listReservation = reservationService.findResaByClientId(5);
//            System.out.println(listReservation);
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
