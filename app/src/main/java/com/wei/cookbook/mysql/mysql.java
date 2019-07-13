package com.wei.cookbook.mysql;
import java.sql.ResultSet;

public class mysql {
    public static void main(String[] srgs){
        DBConnection dbConnection = new DBConnection();  //实例化
        /*boolean bll = false;
        bll = dbConnection.delete("315388");
        dbConnection.insert("00000001","1","1","1","1","1","1","1",
                "1","1","1","1","1","1","1");
        ResultSet resultSet = dbConnection.query("216150");*/
        ResultSet resultSet = dbConnection.obscureQuery("鸡");
        dbConnection.printList(resultSet);
    }
}
