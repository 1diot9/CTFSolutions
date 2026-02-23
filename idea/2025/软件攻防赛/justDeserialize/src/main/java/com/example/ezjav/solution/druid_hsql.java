package com.example.ezjav.solution;

//先通过反序列化实现JNDI。这里可以用JdbcRowSetImpl或者LdapAttribute
//通过本地工厂类，绕过高版本的JNDI。
//通过DruidDataSource连接hsql，预设查询语句，本质上是利用了hsql能够调用任意Java公有静态方法的漏洞
public class druid_hsql {
    public static void main(String[] args) {

    }
}
