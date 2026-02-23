package com.example;

import oracle.jdbc.rowset.OracleCachedRowSet;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        OracleCachedRowSet oracleCachedRowSet = new OracleCachedRowSet();
        oracleCachedRowSet.setDataSourceName("rmi://localhost:1097/remoteobj");

        oracleCachedRowSet.getConnection();
    }
}