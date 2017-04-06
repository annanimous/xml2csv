/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.energodata;

import org.postgresql.ds.PGPoolingDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class PostgreProcessor {

    private Connection con;
    public void initialize(Config config) throws SQLException, ClassNotFoundException {

        Class.forName("org.postgresql.Driver");
        //con = DriverManager.getConnection("jdbc:postgresql://" + postgreHost + ':' + postgrePort + '/' + databaseName, postgreUser, postgrePassword);
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName(config.getPostgrePassword());
        source.setServerName(config.getPostgreHost());
        source.setDatabaseName(config.getPostgreDatabaseName());
        source.setUser(config.getPostgreUser());
        source.setPassword(config.getPostgrePassword());
        source.setMaxConnections(config.getPostgreMaxConnections());
        con = source.getConnection();

    }

    public void deinitialize() throws SQLException {
        if (con != null) {
            con.close();
        }
    }
    public List<String> query (String returnColumn, String table, String searchString) throws SQLException {
        List<String> returnString = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs;
        if (searchString.contains(" "))
        {
            String socr = searchString.split(" ")[0];
            searchString = searchString.substring(searchString.indexOf(" ")+1);
            rs = stmt.executeQuery("SELECT " + returnColumn + " FROM " + table + " WHERE name = '" + searchString + "' AND socr ='" + socr + "'");
        }
        else rs = stmt.executeQuery("SELECT " + returnColumn + " FROM " + table + " WHERE name = '" + searchString + "'");

        while (rs.next()) {
            if (rs.getString(returnColumn) != null ) returnString.add(rs.getString(returnColumn));
        }
            return returnString;
    }
    public List<String> getBuilding(String searchString, String searchCode) throws SQLException {
        List<String> returnString = new ArrayList<>();
        Statement stmt = con.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT index FROM street_building WHERE build_number = '" + searchString + "'" + "AND parent_code = '" + searchCode + "'");

        while (rs.next()) {
            if (rs.getString("index") != null ) returnString.add(rs.getString("index"));
        }
        return returnString;
    }
    public String getStreetIndex(String searchString) throws SQLException {
        String returnString = "";
        Statement stmt = con.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT index FROM street_zip WHERE code = '" + searchString + "'");

        while (rs.next()) {
            if (Objects.equals(returnString, "")) {
                returnString = rs.getString("index");
            }
            else
            returnString = returnString + ", " + rs.getString("index");
        }
        return returnString;
    }
    public String getBuildingIndex(String searchString) throws SQLException {
        String returnString = "";
        Statement stmt = con.createStatement();
        ResultSet rs;
        rs = stmt.executeQuery("SELECT index FROM street_building WHERE parent_code = '" + searchString + "'");

        while (rs.next()) {
            if (Objects.equals(returnString, "")) {
                returnString = rs.getString("index");
            }
            else if (!returnString.contains(rs.getString("index")))
                returnString = returnString + ", " + rs.getString("index");
        }
        return returnString;
    }
    }
