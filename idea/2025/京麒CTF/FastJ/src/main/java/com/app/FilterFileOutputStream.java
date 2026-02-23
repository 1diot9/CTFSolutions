package com.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/* loaded from: FastJ-1.0-SNAPSHOT-11.jar:BOOT-INF/classes/com/app/FilterFileOutputStream.class */
public class FilterFileOutputStream extends FileOutputStream {
    public FilterFileOutputStream(String name, String prefix) throws FileNotFoundException {
        super(name);
        if (!name.startsWith(prefix)) {
        }
    }
}
