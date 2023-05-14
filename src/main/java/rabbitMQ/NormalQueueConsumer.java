package rabbitMQ;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class NormalQueueConsumer {

	public static void main(String[] args) throws Exception {
		String QUEUE = "MyFirstQueue";
		
		ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    boolean durable = true;
		// If Duable, in case of server failure of rabbitMq, messages will be persisted
	    channel.queueDeclare(QUEUE, durable, false, false, null);
	    
	    int prefetchCount = 1;
	    // No to take more than one message at a time
	    // once the message is processed and ack then only provide another
	    channel.basicQos(prefetchCount);
	    
	    
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
	    channel.basicConsume(QUEUE, autoAck, deliverCallback, cancelCallBack);

	    

	}

}
