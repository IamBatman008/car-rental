import java.sql.*;
import java.util.*;

import javax.xml.stream.events.StartElement;

class Ex {
    Exception b = new Exception("NotIntValue");

    void print() {
        System.out.println("NotIntValue");
    }
}

class CarRental extends Ex {
    static void registerUser() throws Exception {
        String dburl = "jdbc:mysql://localhost:3306/carrental";
        String dbuser = "root";
        String dbpass = "";
        String driverName = "com.mysql.jdbc.Driver";
        // Class.forName(driverName);
        Connection con = DriverManager.getConnection(dburl, dbuser, dbpass);
        Statement st = con.createStatement();
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Email:");
        String email = sc.next();
        System.out.println("Enter Username:");
        String username = sc.next();
        String password;
        do {
            System.out.print("Enter Password (must contain exactly 8 characters):");
            password = sc.next();
        } while (password.length() != 8);

        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, password);
        preparedStatement.executeUpdate();
        System.out.println("User registered successfully!");
    }

    static void loginUser() throws Exception {
        Scanner sc = new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        boolean isLoggedIn = false;

        while (!isLoggedIn) {
            System.out.println("Enter username:");
            String un = sc.next();
            String password;
            do {
                System.out.print("Enter Password (must contain exactly 8 characters):");
                password = sc.next();
            } while (password.length() != 8);

            preparedStatement.setString(1, un);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            if (un.equals("admin008") && password.equals("admin123")) {
                System.out.println("---------------");
                System.out.println("HELLO! ADMIN ");
                System.out.println("---------------");
                isLoggedIn = true;
                admin();
            } else if (rs.next()) {
                rs.getString("username");
                rs.getString("password");
                System.out.println("Logged in Successfully");
                isLoggedIn = true; // Set to true to exit the loop
            } else {
                System.out.println("Invalid username or password. Please try again.");
            }
        }
    }

    static void admin() throws Exception {
        Scanner sc = new Scanner(System.in);
        int ch;
        do {
            System.out.println("1. Insert New Car");
            System.out.println("2. Remove Car");
            System.out.println("3. Update Rent Price");
            System.out.println("4. Exit");
            System.out.println("Enter Your choice:");
            ch = sc.nextInt();
            switch (ch) {
                case 1:
                    Connection con1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
                    String query = "insert into car(make,model,year,available,rent_price) values (?,?,?,?,?)";
                    System.out.print("Enter Maker :");
                    String maker = sc.next();
                    System.out.print("Enter Model :");
                    String model = sc.next();
                    System.out.print("Enter Year of car :");
                    int year = sc.nextInt();
                    System.out.print("Enter Price/day :");
                    double price = sc.nextDouble();
                    PreparedStatement ps = con1.prepareStatement(query);
                    ps.setString(1, maker);
                    ps.setString(2, model);
                    ps.setInt(3, year);
                    ps.setBoolean(4, true);
                    ps.setDouble(5, price);
                    int i = ps.executeUpdate();
                    if (i > 0) {
                        System.out.println("INSERTED SUCCESSFULLY ");
                    } else {
                        System.out.println(" INSERTION FAILED ");
                    }
                    break;

                case 2:
                    Connection con2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
                    System.out.print("Enter Car Id you Want To Delete:");
                    int id = sc.nextInt();
                    String query2 = "delete from car where car_id =" + id + "";
                    Statement st2 = con2.createStatement();
                    int r = st2.executeUpdate(query2);
                    if (r > 0) {
                        System.out.println(" REMOVED SUCCESSFULLY");
                    } else {
                        System.out.println(" DELETION FAILED ");
                    }
                    break;

                case 3:
                    Connection con3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
                    System.out.print("Enter Car ID To Change It's Rent Price:");
                    int u_id = sc.nextInt();
                    System.out.print("Enter New Rent Price:");
                    double u_price = sc.nextDouble();
                    String query3 = "update car set rent_price='" + u_price + "'where car_id=" + u_id + "";
                    Statement st3 = con3.createStatement();
                    int h = st3.executeUpdate(query3);
                    if (h > 0) {
                        System.out.println(" UPDATED SUCCESSFULLY");
                    } else {
                        System.out.println(" UPDATION FAILED ");
                    }
                    break;

                case 4:
                    System.out.println("Logged Out");
                    break;
            }
        } while (ch != 4);
    }

    static void menu() throws Exception {
        Scanner sc = new Scanner(System.in);
        int choose;
        do {
            System.out.println("1. Rent Car");
            System.out.println("2. Return Car");
            System.out.println("3. Back To Login Page");
            System.out.print("Enter Your Choice:");
            choose = sc.nextInt();
            switch (choose) {
                case 1:
                    rentCar();
                    break;

                case 2:
                    returnCar();
                    break;

                case 3:
                    System.out.println("----->>>>>>Going Back<<<<<<-----");
                    break;
            }

        } while (choose != 3);
    }

    static void rentCar() throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter your age:");
        int age = sc.nextInt();
        if (age < 18) {
            System.out.println("Sorry, you must be at least 18 years old to rent a car.");
            System.out.println("");
            return;
        }

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
        Statement st = con.createStatement();
        String query = "SELECT * FROM car WHERE Available = true";
        ResultSet rs = st.executeQuery(query);

        System.out.println("Available Cars:");
        while (rs.next()) {
            int carId = rs.getInt("car_id");
            String make = rs.getString("Make");
            String model = rs.getString("Model");
            boolean available = rs.getBoolean("Available");
            double rentPrice = rs.getDouble("rent_price");
            System.out.println("-------------------------------------------------");
            System.out.println("Car ID: " + carId + "");
            System.out.println("Make:" + make + "");
            System.out.println("Model:" + model + "");
            System.out.println("Available:" + available + "");
            System.out.println("Rent Price:" + rentPrice + "");
            System.out.print("");
        }
        rs.close();

        System.out.print("Enter the Car ID you want to rent:");
        int selectedCarId = sc.nextInt();

        System.out.print("Enter your name:");
        String customerName = sc.next();
        System.out.print("Enter your phone number:");
        String phoneNumber = sc.next();

        System.out.print("Enter the number of days to rent the car:");
        int numberOfDays = sc.nextInt();

        String updateQuery = "UPDATE car SET Available = false WHERE car_id = ?";
        PreparedStatement updateStatement = con.prepareStatement(updateQuery);
        updateStatement.setInt(1, selectedCarId);
        updateStatement.executeUpdate();

        String sql = "SELECT rent_price FROM car WHERE car_id = " + selectedCarId + "";
        rs = st.executeQuery(sql);
        double rentPrice = 0.0;
        if (rs.next()) {
            rentPrice = rs.getDouble("rent_price");
        }
        double finalPrice = rentPrice * numberOfDays;

        System.out.println("---------------------------------------");
        System.out.println("Customer Name: " + customerName);
        System.out.println("Phone Number: " + phoneNumber);
        System.out.println("Selected Car ID: " + selectedCarId);
        System.out.println("Number of Days: " + numberOfDays);
        System.out.println("Final Price: " + finalPrice);
        System.out.println("-----------------------------------------");
    }

    static void returnCar() throws Exception {
        Scanner sc = new Scanner(System.in);

        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/carrental", "root", "");
        Statement st = con.createStatement();

        // Display rented cars
        String sql = "SELECT * FROM car WHERE Available = false"; // Only select rented cars
        ResultSet rentedCarsResultSet = st.executeQuery(sql);

        // Check if there are any rented cars
        if (!rentedCarsResultSet.next()) {
            System.out.println("No cars have been rented.");
            return;
        }
        rentedCarsResultSet.close();

        rentedCarsResultSet = st.executeQuery(sql);
        System.out.println("Rented Cars:");
        while (rentedCarsResultSet.next()) {
            int carId = rentedCarsResultSet.getInt("car_id");
            String make = rentedCarsResultSet.getString("Make");
            String model = rentedCarsResultSet.getString("Model");
            double rentPrice = rentedCarsResultSet.getDouble("rent_price");
            System.out.println("-------------------------------------------------");
            System.out.println("Car ID: " + carId);
            System.out.println("Make: " + make);
            System.out.println("Model: " + model);
            System.out.println("Rent Price: " + rentPrice);
            System.out.println("");
        }

        System.out.println("Enter the Car ID you want to return:");
        int selectedCarId = sc.nextInt();

        // Check if the selected car is rented
        String sql2 = "SELECT Available FROM car WHERE car_id = ?";
        PreparedStatement rented = con.prepareStatement(sql2);
        rented.setInt(1, selectedCarId);
        ResultSet rs = rented.executeQuery();

        if (rs.next()) {
            boolean isAvailable = rs.getBoolean("Available");
            if (isAvailable) {
                System.out.println("The selected car is not currently rented.");
            } else {
                // Update the availability of the selected car in the database
                String updateQuery = "UPDATE car SET Available = true WHERE car_id = ?";
                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                updateStatement.setInt(1, selectedCarId);
                int rowsUpdated = updateStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Car returned successfully.");
                } else {
                    System.out.println("Car ID not found or the car was not rented.");
                }
            }
        } else {
            System.out.println("Car ID not found or the car was not rented.");
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("                                 -----------------------------------------------------");
        System.out.println("                                 ----------WELCOME TO FAST AND SAFE CAR RENTAL--------");
        System.out.println("                                 -----------------------------------------------------");
        System.out.println("");
        int choice;
        do {
            System.out.println("1. Login In ");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter Your Choice: ");
            choice = sc.nextInt();
            switch (choice) {
                case 1:
                    loginUser();
                    menu();
                    break;

                case 2:
                    registerUser();
                    break;

                case 3:
                    System.out.println(
                            "        --------------THANK YOU FOR VISITING FAST AND SAFE CAR RENTAL---------------");
                    break;

                default:
                    System.out.println("Enter Valid Number !");
            }
        } while (choice != 3);
    }
}
