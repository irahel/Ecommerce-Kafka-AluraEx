package br.com.alura.ecommerce;

import br.com.alura.ecommerce.consumer.ConsumerService;
import br.com.alura.ecommerce.consumer.ServiceRunner;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public class EmailService implements ConsumerService<Email> {
    private static final int THREADS = 5;

    public static void main(String[] args) {
        new ServiceRunner<>(EmailService::new).start(THREADS);
    }

    public String getConsumerGroup() {
        return EmailService.class.getSimpleName();
    }

    public String getTopic() {
        return "ECOMMERCE_SENDEMAIL";
    }

    public void parse(ConsumerRecord<String, Message<Email>> record) {

        System.out.println("----------------------");
        System.out.println("Sending Email");
        System.out.println(record.key());
        System.out.println(record.value());
        System.out.println(record.partition());
        System.out.println(record.offset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email sent successfully");
    }

}
