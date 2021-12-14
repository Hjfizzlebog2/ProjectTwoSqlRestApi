package com.example.projecttwosqlrestapi;


import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;


@RestController
public class VehicleController {

    @Autowired
    private VehicleDao vehicleDao;

    /**
     * Adds a Vehicle to the database.
     * @param newVehicle Vehicle to be added
     * @return Vehicle that is added
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/addVehicle",method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        vehicleDao.create(newVehicle);
        return newVehicle;
    }

    /**
     * Gets Vehicle with given ID
     *
     * @param id the id of the vehicle to query from database
     * @return vehicle with given ID
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
            return vehicleDao.getById(id);

    }

    /**
     * Updates the vehicle with matching ID to newer edition of Vehicle
     *
     * @param newVehicle the Vehicle to be updated
     * @return the newer edition of the Vehicle
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/updateVehicle", method = RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        vehicleDao.update(newVehicle);
        return newVehicle;
    }

    /**
     * Deletes Vehicle from database
     *
     * @param id id of Vehicle to be deleted from database
     * @return ResponseEntity indicating if deletion is successful
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        if (vehicleDao.contains(id)) {
            vehicleDao.delete(vehicleDao.getById(id));
            return new ResponseEntity<>("Deletion successful.",
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Deletion unsuccessful." +
                    " ID not in inventory.", HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Gets the 10 most recent Vehicles from the database
     *
     * @return List of 10 most recent Vehicles from database
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/getLatestVehicles", method=RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException {
        List<Vehicle> dataFromDataBase = vehicleDao.getAllData();
        return dataFromDataBase.subList(dataFromDataBase.size() - 10,dataFromDataBase.size());
    }
}