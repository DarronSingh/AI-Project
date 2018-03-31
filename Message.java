public class Message {
	
	String body;
	int recipient;
	Coordinate coordinate;
	
	// broadcast message
	public Message(int recipient, String body, Coordinate coordinate) {
		this.recipient = recipient;
		this.body = body;
		this.coordinate = coordinate;
	}
	
	public String toString() {
		return "Recipient: " + recipient + "\nBody: " + body + "\nCoordinates: " + coordinate.toString();
	}
}
