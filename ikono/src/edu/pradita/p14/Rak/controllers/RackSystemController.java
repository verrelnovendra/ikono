package controllers;

import database.RackDAO;
import models.Rack;
import java.util.List;

public class RackSystemController {
    private RackDAO rackDAO;

    public RackSystemController() {
        this.rackDAO = new RackDAO();
    }

    public List<Rack> getAllRacks() {
        return rackDAO.getAllRacks();
    }

    public boolean addRack(String name, String location) {
        Rack newRack = new Rack(0, name, location); // rackId will be auto-generated
        return rackDAO.addRack(newRack);
    }
}
