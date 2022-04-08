package InventoryApp.Models;

/**
 * Model of InHouse Part
 * @author Daniel Mudge
 */
public class InHouse extends Part{

    /**
     * The MachineId of the Part
     */
    private int machineId;

    /**
     * Constructor for InHouse Part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return machineId
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId set the machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}
