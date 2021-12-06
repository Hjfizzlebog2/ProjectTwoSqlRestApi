package com.example.projecttwosqlrestapi;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This is a super class called Vehicle. It holds all the basic properties of any
 * vehicle type like the make, model, model year, retail price etc.
 */
@Entity
@Table(name = "vehicles")
public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1864726920921172988L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private String makeModel;

    @Column
    private int modelYear;

    @Column
    private int retailPrice;

    // constructor
    public Vehicle(int id,String makeModel,int modelYear, int retailPrice) {
        this.id = id;
        this.makeModel = makeModel;
        this.modelYear = modelYear;
        this.retailPrice = retailPrice;
    }

    public Vehicle() {

    }

    // getter for id
    public int getId() {
        return id;
    }

    // getter for makeModel
    public String getMakeModel() {
        return makeModel;
    }

    // getter for model year
    public int getModelYear() {
        return modelYear;
    }

    // getter for price
    public int getRetailPrice() {
        return retailPrice;
    }

    // setter method for makeModel of vehicle
    public void setMakeModel(String makeModel) {
        this.makeModel = makeModel;
    }

    // setter method for model year of vehicle
    public void setModelYear(int modelYear) {
        this.modelYear = modelYear;
    }

    // setter method for price of vehicle
    public void setRetailPrice(int price) {
        this.retailPrice = price;
    }

    /**
     * This method prints out the correct string data to console when
     * using the second constructor as well as returns the string
     */
    public String printVehicle2(){
        String vehicleStr = id + " " + makeModel + " " + modelYear + " " +
                "$" + retailPrice;

        System.out.println(vehicleStr);

        return vehicleStr;
    }

    // This is a toString method to print to the user the id,makeModel,modelYear, and retailPrice of the vehicle
    public String toString() {
        return id + " " + makeModel + " " + modelYear + " " + "$" + retailPrice;
    }

}