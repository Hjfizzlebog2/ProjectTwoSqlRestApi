package com.example.projecttwosqlrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
        //Vehicle object to update in file, Object Mapper to read file, and a scanner class
        Vehicle updatedVehicle = null;
        ObjectMapper mapper = new ObjectMapper();
        Scanner fileScan = new Scanner(new File("./inventory.txt"));

        //while file has a next line it will read line and deserialize it
        while (fileScan.hasNextLine()) {
            String currentLine = fileScan.nextLine();
            Vehicle vehicleInFile = mapper.readValue(currentLine,Vehicle.class);

            //if given vehicle parameter id matches the id in file it will update vehicle parameters to new vehicle.
            if (vehicleInFile.getId() == newVehicle.getId()) {
                updatedVehicle = vehicleInFile;
                updatedVehicle.setMakeModel(newVehicle.getMakeModel());
                updatedVehicle.setModelYear(newVehicle.getModelYear());
                updatedVehicle.setRetailPrice(newVehicle.getRetailPrice());

                mapper.writeValue(new File("./inventory.txt"), updatedVehicle);
            }
        }
        return updatedVehicle;
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
     * Gets the 10 most recent Vehicles in the text file
     *
     * @return List of 10 most recent Vehicles from database
     * @throws IOException for error handling
     */
    @RequestMapping(value = "/getLatestVehicles", method=RequestMethod.GET)
    public List<Vehicle> getLatestVehicles() throws IOException {
        Vehicle currentVehicle = null;
        ObjectMapper mapper = new ObjectMapper();
        List<Vehicle> latest = new ArrayList<>();

        Scanner fileScan = new Scanner(new File("./inventory.txt"));

        //Read each vehicle from file
        while (fileScan.hasNextLine()) {
            String currentLine = fileScan.nextLine();
            Vehicle vehicleInFile = mapper.readValue(currentLine,Vehicle.class);

            //Next vehicle in file added to list.
            latest.add(vehicleInFile);

            //When file exceeds 10 vehicles, earlier vehicles are removed.
            if(latest.size() == 10) {
                latest.remove(0);
            }
        }
        return latest;
    }
}