package com.wmirs.archive.poeaa.patterns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;

/**
 * @Desc
 * @Author WangMu
 * @Create 2022-04-19
 */
public class PoeaaConnectionTest {

    @Test
    public void test() throws SQLException {
        Connection conn = PoeaaConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement("select * from products");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getString("name") + "\t" + rs.getString("type"));
        }
    }
}
