package bd.pablo.roidmc.machine.api;

import org.bukkit.Material;

public class MachineDrop {

    private Material material;
    private short data;
    private double price;
    private boolean selling;

    public MachineDrop(Material material, short data, double price) {
        this.material = material;
        this.data = data;
        this.price = price;
    }

    public MachineDrop(Material material, double price) {
        this(material, (short) 0,price);
    }

    public double getTotal(long amount){
        return getTotal(amount,0);
    }

    public double getTotal(long amount,int incrementPercentage){
        double percentage = ((double)incrementPercentage/100)+1;
        return (amount*price)*percentage;
    }

    public Material getMaterial() {
        return material;
    }

    public short getData() {
        return data;
    }

    public double getPrice() {
        return price;
    }

    public boolean isSelling() {
        return selling;
    }

    public MachineDrop setSelling(boolean selling) {
        this.selling = selling;
        return this;
    }

    public synchronized boolean defineInSelling(){
        boolean current = !selling;
        this.selling = true;
        return current;
    }

}
