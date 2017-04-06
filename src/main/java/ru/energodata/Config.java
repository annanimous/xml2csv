/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.energodata;

import java.io.*;

class Config {

    //Postgre params
    private File postgreBinDir;
    private int postgrePort;
    private String postgreHost;
    private String postgreUser;
    private String postgrePassword;
    private String postgreDatabaseName;
    private int postgreMaxConnections;
    //Proxy settings
    private String proxyHost;
    private int proxyPort;

    public int getPostgrePort() {
        return postgrePort;
    }

    public String getPostgreHost() {
        return postgreHost;
    }

    public String getPostgreUser() {
        return postgreUser;
    }

    public String getPostgrePassword() {
        return postgrePassword;
    }

    public String getPostgreDatabaseName() {
        return postgreDatabaseName;
    }

    public int getPostgreMaxConnections() { return postgreMaxConnections; }

    public File getPostgreBinDir() {
        return postgreBinDir;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public String getPostgreConnectionString() {
        return "jdbc:postgresql://" + postgreHost + ':' + postgrePort + '/' + postgreDatabaseName;
    }

    public void load(String fileName) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "windows-1251"));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.contains("=")) {
                String[] pair = line.split("=");
                if (pair.length == 2) {
                    String key = pair[0];
                    String value = pair[1];
                    if (null != key) {
                        switch (key) {
                            case "postgreBinDir":
                                postgreBinDir = new File(value);
                                break;
                            case "postgreHost":
                                postgreHost = value;
                                break;
                            case "postgrePort":
                                postgrePort = Integer.parseInt(value);
                                break;
                            case "postgreUser":
                                postgreUser = value;
                                break;
                            case "postgrePassword":
                                postgrePassword = value;
                                break;
                            case "postgreDatabaseName":
                                postgreDatabaseName = value;
                                break;
                            case "postgreMaxConnections":
                                postgreMaxConnections = Integer.parseInt(value);
                                break;
                            case "proxyHost":
                                proxyHost = value;
                                break;
                            case "proxyPort":
                                proxyPort = Integer.parseInt(value);
                                break;
                        }
                    }
                }
            }
        }
    }
}
