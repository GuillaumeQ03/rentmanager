package com.epf.rentmanager.model;

import java.time.LocalDate;

public class Reservation {

    private int id;
    private Client client;
    private Vehicle vehicle;
    private LocalDate debut;
    private LocalDate fin;

    // constructeurs
    public Reservation(int id, Client client, Vehicle vehicle, LocalDate debut, LocalDate fin) {
        this.id = id;
        this.client = client;
        this.vehicle = vehicle;
        this.debut = debut;
        this.fin = fin;
    }

    public Reservation() {}

    // getters
    public int getId() {
        return id;
    }

    public int getClient_id() {
        return client.getId();
    }

    public int getVehicle_id() {
        return vehicle.getId();
    }

    public LocalDate getDebut() {
        return debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client_id=" + client.getId() +
                ", vehicle_id=" + vehicle.getId() +
                ", debut=" + debut +
                ", fin=" + fin +
                '}';
    }
}
