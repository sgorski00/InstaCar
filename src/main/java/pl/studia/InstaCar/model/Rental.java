package pl.studia.InstaCar.model;

public interface Rental {
    boolean isAvailable();
    void rent();
    void returnRental();
}
