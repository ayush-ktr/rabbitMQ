package rabbitMQ;

import java.util.Scanner;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NormalExchangeProducer {

	public static void main(String[] args) throws Exception {
		
		String EXCHANGE_NAME = "myExchange";
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    try (Connection connection = factory.newConnection();
	         Channel channel = connection.createChannel()) {
	    	//Create an exchange
	    	// exchange type - fanout - broadcast to all, direct - broadcast to specific routingKey types
	        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	        
	        try (Scanner input = new Scanner(System.in)) {
				String message;
				do {
					System.out.println("Enter message: ");
					message = input.nextLine();
					// Publish message to the exchange
					channel.basicPublish(EXCHANGE_NAME, "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
				} while (!message.equalsIgnoreCase("Quit"));
			}
	    }

	}

}
