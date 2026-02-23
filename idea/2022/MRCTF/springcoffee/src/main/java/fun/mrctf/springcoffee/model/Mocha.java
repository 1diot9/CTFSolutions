package fun.mrctf.springcoffee.model;

/* loaded from: springcoffee-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/fun/mrctf/springcoffee/model/Mocha.class */
public class Mocha implements ExtraFlavor {
    double chocolate = 0.2d;

    @Override // fun.mrctf.springcoffee.model.ExtraFlavor
    public String getName() {
        return "Mocha";
    }
}
