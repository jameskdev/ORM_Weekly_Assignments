package org.xm;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String jdbcUrl = "";
        String jdbcUserName = "";
        String jdbcPassword = "";
        System.out.println("Please enter your DB URL: ");
        jdbcUrl = sc.nextLine();
        System.out.println("Please enter your DB Username: ");
        jdbcUserName = sc.nextLine();
        System.out.println("Please enter your DB Password: ");
        jdbcPassword = sc.nextLine();
        HikariConfig config = new HikariConfig();
        //config.setJdbcUrl("jdbc:mariadb://127.0.0.1:3306/");
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(jdbcUserName);
        config.setPassword(jdbcPassword);
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        //config.setUsername("root");
        //config.setPassword("0000");

        try (HikariDataSource ds = new HikariDataSource(config)) {
            System.out.println("Connection Established. You can now execute SQL queries: ");
            Connection cs = ds.getConnection();
            while (true) {
                String sql = sc.nextLine();
                if (sql.equalsIgnoreCase("exitnow")) {
                    break;
                }
                try {
                    Statement sqlstat = cs.createStatement();
                    sqlstat.execute(sql);
                    int uc = sqlstat.getUpdateCount();
                    if (uc > -1) {
                        System.out.println(uc + " Rows affected!");
                    }
                    ResultSet rs = sqlstat.getResultSet();
                    if (rs != null) {
                        StringBuilder sb = new StringBuilder();
                        ResultSetMetaData rm = rs.getMetaData();
                        int cols = rm.getColumnCount();
                        for (int i = 1; i <= cols; i++) {
                            sb.append(rm.getColumnName(i));
                            if (i < cols) {
                                sb.append("|");
                            }
                        }
                        sb.append("\n");
                        while(rs.next()) {
                            for (int i = 1; i <= cols; i++) {
                                sb.append(rs.getString(i));
                                if (i < cols) {
                                    sb.append("|");
                                }
                            }
                            sb.append("\n");
                        }
                        System.out.println(sb.toString());
                        rs.close();
                        sqlstat.close();
                    }

                } catch (SQLException sqex) {
                    System.out.println("Your last SQL command: " + sql + " is not valid!");
                    System.out.println("Current SQL State is: " + sqex.getSQLState() + " and message is: " + sqex.getMessage());
                }
            }
            cs.close();
        } catch (SQLException ex) {
            System.out.println("Connection Failed");
        }
        System.out.println("Bye, Bye, Bye!");
    }
}