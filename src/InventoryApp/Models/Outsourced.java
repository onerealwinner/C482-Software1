package InventoryApp.Models;

/**
 * Model of Outsourced Part
 * @author Daniel Mudge
 */
public class Outsourced extends Part{

    /**
     * CompanyName of the Part
     */
    private String companyName;

    /**
     * Constructor for Outsourced
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName set the companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
