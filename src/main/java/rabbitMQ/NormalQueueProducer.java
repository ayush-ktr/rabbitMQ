package rabbitMQ;

import java.util.Scanner;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NormalQueueProducer {

	public static void main(String[] args) throws Exception {

		String QUEUE = "MyFirstQueue";

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		boolean durable = true;
		// If Duable, in case of server failure of rabbitMq, messages will be persisted
		channel.queueDeclare(QUEUE, durable, false, false, null);

		Scanner input = new Scanner(System.in);
		String message;
		do {
			System.out.println("Enter message: ");
			message = input.nextLine();
			// By default the messages are not persists
			channel.basicPublish("", QUEUE, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		} while (!message.equalsIgnoreCase("Quit"));

	}

}
