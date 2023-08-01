package hotel1;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    private String name;
    private List<Room> rooms;
    private List<Reservation> reservations;

    public Hotel(String name) {
        this.name = name;
        this.rooms = new ArrayList<>();
        this.reservations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

	public Room[] getRooms1() {
		// TODO Auto-generated method stub
		return null;
	}

	public Room[] getRooms11() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addReservation1(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}

    // Other hotel management methods can be added here
}
