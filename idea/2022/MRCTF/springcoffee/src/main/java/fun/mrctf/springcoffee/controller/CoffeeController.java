package fun.mrctf.springcoffee.controller;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import fun.mrctf.springcoffee.model.CoffeeRequest;
import fun.mrctf.springcoffee.model.ExtraFlavor;
import fun.mrctf.springcoffee.model.Message;
import fun.mrctf.springcoffee.model.Mocha;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Base64;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/* loaded from: springcoffee-0.0.1-SNAPSHOT.jar:BOOT-INF/classes/fun/mrctf/springcoffee/controller/CoffeeController.class */
public class CoffeeController extends BaseController {
    @RequestMapping({"/coffee/index"})
    public Message index() {
        return new Message(200, "what do your want? please order");
    }

    @RequestMapping({"/coffee/order"})
    public Message order(@RequestBody CoffeeRequest coffee) {
        if (coffee.extraFlavor != null) {
            ByteArrayInputStream bas = new ByteArrayInputStream(Base64.getDecoder().decode(coffee.extraFlavor));
            Input input = new Input(bas);
            ExtraFlavor flavor = (ExtraFlavor) this.kryo.readClassAndObject(input);
            return new Message(200, flavor.getName());
        }
        if (coffee.espresso > 0.5d) {
            return new Message(200, "DOPPIO");
        }
        if (coffee.hotWater > 0.5d) {
            return new Message(200, "AMERICANO");
        }
        if (coffee.milkFoam <= 0.0d || coffee.steamMilk <= 0.0d) {
            if (coffee.espresso > 0.0d) {
                return new Message(200, "Espresso");
            }
            return new Message(200, "empty");
        }
        if (coffee.steamMilk > coffee.milkFoam) {
            return new Message(200, "CAPPUCCINO");
        }
        return new Message(200, "Latte");
    }

    @RequestMapping({"/coffee/demo"})
    public Message demoFlavor(@RequestBody String raw) throws Exception {
        System.out.println(raw);
        JSONObject serializeConfig = new JSONObject(raw);
        if (serializeConfig.has("polish") && serializeConfig.getBoolean("polish")) {
            this.kryo = new Kryo();
            for (Method setMethod : this.kryo.getClass().getDeclaredMethods()) {
                if (setMethod.getName().startsWith("set")) {
                    try {
                        Object p1 = serializeConfig.get(setMethod.getName().substring(3));
                        if (!setMethod.getParameterTypes()[0].isPrimitive()) {
                            try {
                                setMethod.invoke(this.kryo, Class.forName((String) p1).newInstance());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            setMethod.invoke(this.kryo, p1);
                        }
                    } catch (Exception e2) {
                    }
                }
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        this.kryo.register(Mocha.class);
        this.kryo.writeClassAndObject(output, new Mocha());
        output.flush();
        output.close();
        return new Message(200, "Mocha!", Base64.getEncoder().encode(bos.toByteArray()));
    }
}
