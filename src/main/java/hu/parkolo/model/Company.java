package hu.parkolo.model;

public class Company {

    private int id;
    private String name;
    private int parkingSpaceNum;
    private int disabilityPspaceNum;
    private double pricePerHour;
    private int free;

    public Company(String name, int parkingSpaceNum,
                   int disabilityPspaceNum, double pricePerHour) {

        this.name = name;
        this.parkingSpaceNum = parkingSpaceNum;
        this.disabilityPspaceNum = disabilityPspaceNum;
        this.pricePerHour = pricePerHour;
        this.free = 0;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParkingSpaceNum() {
        return parkingSpaceNum;
    }

    public int getDisabilityPspaceNum() {
        return disabilityPspaceNum;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public int getFree() {
        return free;
    }

    public void setId(int id) {
        this.id = id;
    }
}
