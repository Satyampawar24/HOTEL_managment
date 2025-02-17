import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;
import java.sql.Statement;
import java.sql.ResultSet;


public class jdbc {
   private static  String url="jdbc:mysql://localhost:3306/hostpital";
   private static  String username="root";
    private static String password="satyampawar24";

        public static void main(String[] args) throws ClassNotFoundException, SQLException{

        try {
            // Correct class name syntax
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        }
        try{
            Connection connection=DriverManager.getConnection(url,username,password);
            System.out.println("connection stablish successfully");
           while (true){
               System.out.println();
               System.out.println("HOTAL MANAGMENT SYSTEM");
               Scanner scanner=new Scanner(System.in);
               System.out.println("1. reserve a room ");
               System.out.println("2. view reservation");
               System.out.println("3. GET ROOM NUMBER");
               System.out.println("4. UPDATE RESERVATIONS");
               System.out.println("5. DELETE RESERVATIONS");
               System.out.println("0. EXIT");
               System.out.println("CHOOSE AN OPTIONS");
               int choice =scanner.nextInt();
               switch (choice){
                   case 1:
                       reserveRoom(connection,scanner);
                       break;
                   case 2:
                       viewReservation(connection);
                       break;
                   case 3:
                       getRoomNumber(connection,scanner);
                       break;
                   case 4:
                       updateReservation(connection,scanner);
                       break;
                   case 5:
                       deleteReservation(connection,scanner);
                       break;
                   case 0:
                       exit();
                       scanner.close();
                       return;
                   default:
                       System.out.println("invalid input <><><> try again.");

               }

           }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
    }

    public static void reserveRoom(Connection connection,Scanner scanner) {
        try {
            System.out.println("Enter the guest name: ");
            String gusetname = scanner.next();
            scanner.nextLine();
            System.out.println("Enter Room number: ");
            int roomnumber = scanner.nextInt();
            System.out.println("Enter mobile number: ");
            String mobilenumber = scanner.next();

            String sql = "insert into hotel (guest_name,room_number,mobile_number)" + "values('" + gusetname + "'," + roomnumber + ",'" + mobilenumber + "')";
            try (Statement statement = connection.createStatement()) {
                int affectedRooms = statement.executeUpdate(sql);
                if (affectedRooms > 0) {
                    System.out.println("ROOM BOOK SUCCESSFUL!!!");
                } else {
                    System.out.println("ROOM BOOK FAILED!!!!.");
                }
            }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


private static void viewReservation(Connection connection )throws SQLException{
    String sql=" select reservation_id,guest_name,room_number,mobile_number,reservation_date from hotel";
    try (Statement statement=connection.createStatement();
    ResultSet resultSet=statement.executeQuery(sql)){

        System.out.println("Current Reservations:");
        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
        System.out.println("| Reservation ID | Guest           | Room Number   | mobile Number        | Reservation Date        |");
        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");
while (resultSet.next()){
    int reservationId=resultSet.getInt("reservation_id");
    String guestname=resultSet.getString("guest_name");
    int roomnumber=resultSet.getInt("room_number");
    String mobilenumber=resultSet.getString("mobile_number");
    String  reservationdate=resultSet.getTimestamp("reservation_date").toString();
    System.out.printf("|%-16d|%-17s|%-15d|%-22s|%-25s|\n",
    reservationId,guestname,roomnumber,mobilenumber,reservationdate);
}

        System.out.println("+----------------+-----------------+---------------+----------------------+-------------------------+");

    }
        }
private static  void getRoomNumber(Connection connection,Scanner scanner) {
    try {
        System.out.print("enter the reservation id");
        int reservationid = scanner.nextInt();
        System.out.print("enter the guest name");
        String guestname = scanner.next();

        String sql = "select room_number from hotel " + "where reservation_id=" + reservationid + " and guest_name='" + guestname + "'";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            if (resultSet.next()) {
                int roomnumber = resultSet.getInt("room_number");
                System.out.println(" room number for reservation id " + reservationid + " and guest " + guestname + " is: " + roomnumber);

            } else {
                System.out.println("reservation not founf for the given ID and guset name. ");
            }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        }
private static void updateReservation(Connection connection,Scanner scanner){
            try{
                System.out.print("enter reservation id to update: ");
                int reservationid=scanner.nextInt();
                scanner.nextLine();
                if(!reservationExists(connection,reservationid)){
                    System.out.println("resvation not found for the given Id. ");
                    return;

                }
                System.out.print("enter the guest name: ");
                String newGuestname=scanner.next();
                System.out.print("enter new room number: ");
                int newroomnumber=scanner.nextInt();
                System.out.println("enter new mobile number: ");
                String newmobilenumber=scanner.next();

                String sql="update hotel set guest_name='"+newGuestname+"', "+"room_number=" +newroomnumber+","+
                        "mobile_number='"+newmobilenumber+"' "+"where reservation_id="+reservationid;
                try(Statement statement=connection.createStatement()){
                    int affectedroom=statement.executeUpdate(sql);
                    if(affectedroom>0){
                        System.out.println("reservation updates successfully! ");

                    }else {
                        System.out.println("reservation updated failed! ");
                    }
                }


            }catch (SQLException e){
             e.printStackTrace();
            }
 }
 private static void deleteReservation(Connection connection ,Scanner scanner){
            try {
                System.out.println("enter the reservation Id to delete: ");
                int reservationid = scanner.nextInt();
                if (!reservationExists(connection, reservationid)) {
                    System.out.println("resevation not found for the given Id. ");
                    return;
                }
                String sql = "delete from hotel where reservation_id=" + reservationid;
                try (Statement statement = connection.createStatement()) {
                    int affectedRows = statement.executeUpdate(sql);
                    if (affectedRows > 0) {
                        System.out.println("Reservation delete succesfully. ");
                    } else {
                        System.out.println("reservation delete failed. ");
                    }
                }
            }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                }

            private static boolean reservationExists(Connection connection , int reservationid){
            try {
                String sql="select reservation_id from hotel where reservation_id= "+reservationid;
            try (Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(sql)) {
                return resultSet.next();
            }
            }
            catch (SQLException e){
                e.printStackTrace();
                return false;
            }

            }
public static void exit()throws InterruptedException{
    System.out.println("Exiting program");

}
 }
