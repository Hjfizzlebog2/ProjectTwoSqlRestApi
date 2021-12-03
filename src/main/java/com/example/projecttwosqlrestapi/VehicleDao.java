package com.example.projecttwosqlrestapi;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

/**
 * Data Access Object - provides some specific operations
 * Access data for the Vehicle entity
 * Repository annotation allows Spring to find and configure the DAO
 * Transactional annotation will cause Spring to call begin() and commit()
 * at the start/end of the method. If exception occurs it will also call rollback().
 */
@Repository
@Transactional
public class VehicleDao {

    //PersistenceContext annotation used to specify there is a database source.
    //EntityManager is used to create and remove persistent entity instances,
    //to find entities by their primary key, and to query over entities.
    @PersistenceContext
    private EntityManager entityManager;

    // Insert vehicle into the database
    public void create(Vehicle newVehicle) {
        entityManager.persist(newVehicle);
    }

    // Return the vehicle with the passed in id
    public Vehicle getById(int id) {
        return entityManager.find(Vehicle.class,id);
    }

    // Delete the requested vehicle
    public void delete(Vehicle vehicleToDelete) {
        entityManager.remove(vehicleToDelete);
    }

}
