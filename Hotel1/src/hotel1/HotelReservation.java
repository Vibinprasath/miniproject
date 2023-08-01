package hotel1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
class HotelReservation {
    private static final String DB_URL = "jdbc:mysql://localhost/htls";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "vibinprasath";

    public static void main(String[] args) {
        Hotel hotel = new Hotel("Awesome Hotel");

        hotel.addRoom(new Room(101, RoomType.SINGLE, 100.00));
        hotel.addRoom(new Room(102, RoomType.DOUBLE, 150.00));
        hotel.addRoom(new Room(103, RoomType.SUITE, 250.00));

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to " + hotel.getName());

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Reserve a room");
            System.out.println("2. View available rooms");
            System.out.println("3. View reservations");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    reserveRoom(hotel, scanner);
                    break;
                case 2:
                    viewAvailableRooms(hotel);
                    break;
//                case 3:
//                    hotel.displayReservations();
//                    break;
                case 4:
                    System.out.println("Thank you for using the hotel reservation system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewAvailableRooms(Hotel hotel) {
        System.out.println("Available Rooms:");
        for (Room room : hotel.getRooms()) {
            if (!room.isBooked()) {
                System.out.println(room.getRoomNumber() + " - " + room.getType() + " - $" + room.getPrice());
            }
        }
    }

    private static void reserveRoom(Hotel hotel, Scanner scanner) {
        System.out.println("Enter guest name: ");
        scanner.nextLine();
        String name = scanner.nextLine();

        System.out.println("Enter guest email: ");
        String email = scanner.nextLine();

        System.out.println("Available Rooms:");
        for (Room room : hotel.getRooms()) {
            if (!room.isBooked()) {
                System.out.println(room.getRoomNumber() + " - " + room.getType() + " - $" + room.getPrice());
            }
        }

        System.out.println("Enter room number to reserve: ");
        int roomNumber = scanner.nextInt();

        Room selectedRoom = null;
        for (Room room : hotel.getRooms()) {
            if (room.getRoomNumber() == roomNumber && !room.isBooked()) {
                selectedRoom = room;
                break;
            }
        }

        if (selectedRoom != null) {
            System.out.println("Enter check-in date (yyyy-mm-dd): ");
            String checkInDateStr = scanner.next();
            LocalDate checkInDate = LocalDate.parse(checkInDateStr);

            System.out.println("Enter check-out date (yyyy-mm-dd): ");
            String checkOutDateStr = scanner.next();
            LocalDate checkOutDate = LocalDate.parse(checkOutDateStr);

            if (checkInDate.isAfter(checkOutDate)) {
                System.out.println("Invalid dates. Check-in date cannot be after check-out date.");
            } else {
                Guest guest = new Guest(name, email);
                Reservation reservation = new Reservation(guest, selectedRoom, checkInDate, checkOutDate);
                hotel.addReservation(reservation);
                selectedRoom.setBooked(true);

                // Insert reservation into the database
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String sql = "INSERT INTO reservations (guest_name, guest_email, room_number, check_in_date, check_out_date) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, name);
                        statement.setString(2, email);
                        statement.setInt(3, roomNumber);
                        statement.setString(4, checkInDateStr);
                        statement.setString(5, checkOutDateStr);
                        statement.executeUpdate();
                    }
                } catch (SQLException e) {
                    System.out.println("Error reserving room: " + e.getMessage());
                    return;
                }

                System.out.println("Reservation successful!");
            }
        } else {
            System.out.println("Invalid room number or room is already booked.");
        }
    }
}