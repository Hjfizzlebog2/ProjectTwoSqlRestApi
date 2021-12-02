package com.example.projecttwosqlrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.CharEncoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.*;

@RestController
public class VehicleController {

    /**
     * Adds a Vehicle to the text file.
     * @param newVehicle Vehicle to be added
     * @return Vehicle that is added
     * @throws IOException when error with file handling
     */
    @RequestMapping(value = "/addVehicle",method = RequestMethod.POST)
    public Vehicle addVehicle(@RequestBody Vehicle newVehicle) throws IOException {
        //ObjectMapper provides functionality for reading and writing JSON
        ObjectMapper mapper = new ObjectMapper();

        //Create a FileWrite to write to inventory.txt and APPEND mode is true
        FileWriter output = new FileWriter("./inventory.txt",true);

        //Serialize object to JSON and write to file
        mapper.writeValue(output,newVehicle);

        //Append a new line character to the file
        //Above FileWriter "output" is automatically closed by the mapper
        FileUtils.writeStringToFile(new File("./inventory.txt"),
                System.lineSeparator(), CharEncoding.UTF_8, true);
        return newVehicle;
    }

    /**
     * Gets Vehicle with given ID
     *
     * @param id the id of the vehicle to be searched for
     * @return vehicle with given ID
     * @throws IOException when error with file handling
     */
    @RequestMapping(value = "/getVehicle/{id}", method = RequestMethod.GET)
    public Vehicle getVehicle(@PathVariable("id") int id) throws IOException {
        Vehicle currentVehicle = null;
        ObjectMapper mapper = new ObjectMapper();

        Scanner fileScan = new Scanner(new File("./inventory.txt"));
        while (fileScan.hasNextLine()) {
            String currentLine = fileScan.nextLine();
            Vehicle vehicleInFile = mapper.readValue(currentLine,Vehicle.class);
            if (vehicleInFile.getId() == id) {
                currentVehicle = vehicleInFile;
            }
        }
        return currentVehicle;
    }

    /**
     * Updates the vehicle with matching ID to newer edition of Vehicle
     *
     * @param newVehicle the Vehicle to be updated
     * @return the newer edition of the Vehicle
     * @throws IOException when error in file handling
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
     * Deletes Vehicle from text file
     *
     * @param id id of Vehicle to be deleted
     * @return ResponseEntity indicating if deletion is successful
     * @throws IOException when error in file handling
     */
    //NOTE: CURRENT METHOD IS SLOW, BUT IT WORKS. CAN REFACTOR LATER
    // In order to delete, just read file line by line and do nothing if it is the vehicle you want to delete
    @RequestMapping(value = "/deleteVehicle/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteVehicle(@PathVariable("id") int id) throws IOException {
        File inventoryFile = new File("./inventory.txt");
        File tempInventoryFile = new File("./tempInventory.txt");

        // MAKE TEMP COPY OF FILE
        FileUtils.copyFile(inventoryFile, tempInventoryFile);

        // CLEAR OLD FILE
        PrintWriter writer = new PrintWriter(inventoryFile);
        writer.print("");
        writer.close();

        // START TO ADD BACK LINES TO FILE ONE BY ONE
        FileWriter output = new FileWriter(tempInventoryFile,true);
        Scanner tempInventoryFileScanner = new Scanner(tempInventoryFile);
        ObjectMapper mapper = new ObjectMapper();
        Vehicle currentVehicle = null;
        boolean deletionIsSuccessful = false;
        while (tempInventoryFileScanner.hasNextLine()) {
            String currentLine = tempInventoryFileScanner.nextLine(); // get current line
            currentVehicle = mapper.readValue(currentLine,Vehicle.class); // got current vehicle
            if (deletionIsSuccessful || currentVehicle.getId() != id) { // as long as vehicle id does not match
                //Serialize object to JSON and write to file
                mapper.writeValue(output,currentVehicle); // write vehicle to file in JSON format

                //Append a new line character to the file
                //Above FileWriter "output" is automatically closed by the mapper
                FileUtils.writeStringToFile(new File("./inventory.txt"),
                        System.lineSeparator(),
                        CharEncoding.UTF_8,
                        true);
            } else {
                deletionIsSuccessful = true;
                // by not writing data, we are therefore deleting it from the file
            }
        }

        // DELETE TEMP COPY
        FileUtils.deleteQuietly(tempInventoryFile);

        //RETURN ResponseEntity DEPENDING ON DELETION STATUS
        if (deletionIsSuccessful) {
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
     * @return List of 10 most recent Vehicles
     * @throws IOException when error in file handling
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