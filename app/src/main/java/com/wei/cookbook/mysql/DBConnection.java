package com.wei.cookbook.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBConnection {
    Connection conn = null;
    PreparedStatement preStmt = null;
    ResultSet resultSet = null;

    //连接数据库
    public Connection getConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:mysql://127.0.0.1:3306/Data?useSSL=false";
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, "root", "");
            System.out.println("数据库连接建立！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void close(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
                System.out.println("数据库连接终止！！！");
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    //插入操作
    public boolean insert(String id, String title, String author, String J_photo, String abstract0,
                          String raw_material_info, String ingredients_info, String taste, String technique,
                          String time, String difficulty, String kitchen_utensils, String category,
                          String tips, String step) {
        String sql = "insert into recipe_info (id,title,author,J_photo,abstract,raw_material_info," +
                "ingredients_info,taste,technique,time,difficulty,kitchen_utensils,category,tips,step)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        conn = this.getConnection();      //调用连接数据库方法，连接数据库
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, id);
            preStmt.setString(2, title);
            preStmt.setString(3, author);
            preStmt.setString(4, J_photo);
            preStmt.setString(5, abstract0);
            preStmt.setString(6, raw_material_info);
            preStmt.setString(7, ingredients_info);
            preStmt.setString(8, taste);
            preStmt.setString(9, technique);
            preStmt.setString(10, time);
            preStmt.setString(11, difficulty);
            preStmt.setString(12, kitchen_utensils);
            preStmt.setString(13, category);
            preStmt.setString(14, tips);
            preStmt.setString(15, step);
            preStmt.executeUpdate();
            preStmt.close();
            /* db.close();//关闭连接*/
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("插入失败~！！！！！");
            return false;
        }
        return true;
    }
    //按照菜谱ID删除操作
    public boolean delete(String id){
        String sql="delete from recipe_info where id=?";
        conn = this.getConnection();
        try {
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1, id);
            preStmt.executeUpdate();
            preStmt.close();
            //db.close();//关闭连接
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("删除失败~！！！！！");
            return false;
        }
        System.out.println("删除成功！！！");
        return true;
    }

    //按照id查找信息
    public ResultSet query(String id){
        conn=this.getConnection();
        String sql;
        sql = "select * from recipe_info where id =?";
        try {
            //非条件查询直接赋值
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1,id);
            resultSet = preStmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("用户名查询错误~！！！！！！！！！！！！");
        }
        return resultSet;
    }

    //按照title模糊查找信息
    public ResultSet obscureQuery(String title){
        conn=this.getConnection();
        String sql;
        sql = "select * from recipe_info where title like ?";
        try {
            //非条件查询直接赋值
            preStmt = conn.prepareStatement(sql);
            preStmt.setString(1,"%"+ title +"%");
            resultSet = preStmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("用户名查询错误~！！！！！！！！！！！！");
        }
        return resultSet;
    }


    //显示信息
    public void printList(ResultSet rs)
    {
        String  id, title,author, J_photo, abstract0, raw_material_info, ingredients_info, taste, tchnique,
                time, difficulty, kitchen_utensils, category, tips,step;
        List<Map> list = new ArrayList<Map>();
        try {
            while(rs.next())
            {
                id = rs.getString("id");
                title = rs.getString("title");
                author = rs.getString("author");
                J_photo = rs.getString("J_photo");
                abstract0 = rs.getString("abstract");
                raw_material_info = rs.getString("raw_material_info");
                ingredients_info = rs.getString("ingredients_info");
                taste= rs.getString("taste");
                tchnique = rs.getString("technique");
                time = rs.getString("time");
                difficulty = rs.getString("difficulty");
                kitchen_utensils = rs.getString("kitchen_utensils");
                category = rs.getString("category");
                tips = rs.getString("tips");
                step = rs.getString("step");

                Map mp = new HashMap();
                mp.put("id", id);
                mp.put("title", title);
                mp.put("author",author);
                mp.put("J_photo",J_photo);
                mp.put("abstract0", abstract0);
                mp.put("raw_material_info",raw_material_info);
                mp.put("ingredients_info", ingredients_info);
                mp.put("taste", taste);
                mp.put("tchnique",tchnique);
                mp.put("time", time);
                mp.put("diffculty", difficulty);
                mp.put("kitchen_utensils", kitchen_utensils);
                mp.put("category",category);
                mp.put("tips", tips);
                mp.put("step", step);
                list.add(mp);
            }
            System.out.println(list);
            System.out.println("suc");
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("用户查询赋值出错~！！！！！！！！！！");
            e.printStackTrace();
        }
    }
}
