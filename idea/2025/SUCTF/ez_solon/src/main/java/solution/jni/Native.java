package solution.jni;

//这里的javah -jni solution.jni.Native一直执行不成功，遂废弃，写到项目根目录下了
public class Native {
    public native String exec(String cmd);
}
