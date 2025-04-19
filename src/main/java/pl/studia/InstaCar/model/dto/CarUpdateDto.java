package pl.studia.InstaCar.model.dto;

import lombok.Data;

@Data
public class CarUpdateDto {
    private String color;
    private String engine;
    private String licensePlate;
    private double price;
    private int productionYear;
    private String vin;
}
