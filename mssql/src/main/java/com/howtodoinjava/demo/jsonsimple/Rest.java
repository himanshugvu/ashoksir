package com.howtodoinjava.demo.jsonsimple;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

	@GetMapping("/")
	public List<String> selectValue() {
		String connectionUrl = "jdbc:sqlserver://acciontest.cyfl1ri6kafq.us-east-1.rds.amazonaws.com;"
				+ "databaseName=sample;user=admin;password=admin123";
		List<String> names = new ArrayList<>();
		try {
			System.out.print("Connecting to SQL Server ... ");
			try (Connection connection = DriverManager.getConnection(connectionUrl)) {
				System.out.println("Done.");
				Statement stmt = connection.createStatement();
				ResultSet rs;
				//Test
				rs = stmt.executeQuery("SELECT * FROM sample.dbo.emp");
				while (rs.next()) {
					names.add( rs.getString(2));
				}
			}
		} catch (Exception e) {
			System.out.println();
			e.printStackTrace();
		}
		return names;
	}
	


	@GetMapping("/insertQuery/{name}/{id}")
	public List<String> insertValue(@PathVariable("name") String name,
	        @PathVariable("id") String id) {
		String connectionUrl = "jdbc:sqlserver://acciontest.cyfl1ri6kafq.us-east-1.rds.amazonaws.com;"
				+ "databaseName=sample;user=admin;password=admin123";

		try {
		    System.out.print("Connecting to SQL Server ... ");
		    try (Connection connection = DriverManager.getConnection(connectionUrl))        {
		        System.out.println("Done.");

		        String insertSql = "INSERT INTO sample.dbo.emp VALUES("+id+",'"+name+"',getdate())";
		        ResultSet resultSet = null;
		        PreparedStatement prepsInsertProduct = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);

		            prepsInsertProduct.execute();
		            // Retrieve the generated key from the insert.
		            resultSet = prepsInsertProduct.getGeneratedKeys();

		            // Print the ID of the inserted row.
		            while (resultSet.next()) {
		                System.out.println("Generated: " + resultSet.getString(1));
		            }
		    }
		} catch (Exception e) {
		    System.out.println();
		    e.printStackTrace();
		}
		return selectValue();
	}
	
	@GetMapping("/spQuery/{name}/{id}")
	public List<String> spQuery(@PathVariable("name") String name,
	        @PathVariable("id") String id) {
		String connectionUrl = "jdbc:sqlserver://acciontest.cyfl1ri6kafq.us-east-1.rds.amazonaws.com;"
				+ "databaseName=sample;user=admin;password=admin123";

		try {
		    System.out.print("Connecting to SQL Server ... ");
		    try (Connection connection = DriverManager.getConnection(connectionUrl))        {
		        System.out.println("Done.");
		        String SQL = "{call sp_insertdata  (?,?)}";

		        CallableStatement cs = connection.prepareCall(SQL);

		        cs.setString(1, id);

		        cs.setString(2, name);
		        cs.execute();
		      }
		} catch (Exception e) {
		    System.out.println();
		    e.printStackTrace();
		}
		return selectValue();
	}

}
