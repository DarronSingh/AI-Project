
// agents will create message
	// either direct or broadcast
		// direct message
			// can ignore body
			// recipient will be agentID of the recipient
			// c will be the coordinate to send
			// messages will be added to each agent's inbox queue
		// broadcast message
			// body contains message to broadcast
			// recipient will be -1
			// c will be coordinate of sending agent

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
}
