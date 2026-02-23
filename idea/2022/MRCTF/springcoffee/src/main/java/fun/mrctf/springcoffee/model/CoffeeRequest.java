package fun.mrctf.springcoffee.model;

/* loaded from: springcoffee-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/fun/mrctf/springcoffee/model/CoffeeRequest.class */
public class CoffeeRequest {
    public double espresso;
    public double hotWater;
    public double milkFoam;
    public double steamMilk;
    public String extraFlavor;

    public void setEspresso(final double espresso) {
        this.espresso = espresso;
    }

    public void setHotWater(final double hotWater) {
        this.hotWater = hotWater;
    }

    public void setMilkFoam(final double milkFoam) {
        this.milkFoam = milkFoam;
    }

    public void setSteamMilk(final double steamMilk) {
        this.steamMilk = steamMilk;
    }

    public void setExtraFlavor(final String extraFlavor) {
        this.extraFlavor = extraFlavor;
    }

    public double getEspresso() {
        return this.espresso;
    }

    public double getHotWater() {
        return this.hotWater;
    }

    public double getMilkFoam() {
        return this.milkFoam;
    }

    public double getSteamMilk() {
        return this.steamMilk;
    }

    public String getExtraFlavor() {
        return this.extraFlavor;
    }
}
