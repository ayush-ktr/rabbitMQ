package rabbitMQ;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class NormalExchangeConsumer {

	public static void main(String[] args) throws Exception {
		
		String EXCHANGE_NAME = "myExchange";
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    // Create the object for existing exchange
	    // exchange type - fanout - broadcast to all, direct - broadcast to specific routingKey types
	    channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
	    
	    // Create a new queue and bind the queue to the channel
	    // Hello is the routing key, which will be checked if message needs to be added to the current queue
	    String queueName = channel.queueDeclare().getQueue();
	    channel.queueBind(queueName, EXCHANGE_NAME, "hello");
	    
	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println("Got the message, going to sleep for " + message.length());
	        try {
				Thread.sleep(message.length()*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        System.out.println(" [x] Received '" + message + "'");
	        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	    };
	    
	    CancelCallback cancelCallBack = (consumerTag) -> {
	    	System.out.println(" [x] Cancelled '" + consumerTag + "'");
	    };
	    
	    boolean autoAck = false;
	    channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallBack);


	}

}
