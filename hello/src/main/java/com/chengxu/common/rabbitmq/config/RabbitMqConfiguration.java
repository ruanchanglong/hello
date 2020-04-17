package com.chengxu.common.rabbitmq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;


/**
 * 配置多mq
 *
 * http://117.73.10.145:15672/#/queues
 * ruanchanglong  ruanchanglong
 */
@Slf4j
@Configuration
public class RabbitMqConfiguration {

 /*   @Value("${mq.queue}")
    public String Queue;
    @Value("${mq.exchange}")
    public String Exchange;
    @Value("${mq.routing-key}")
    public String routingkey;*/

    // 多源1,MQ
    @Bean(name = "oneConnectionFactory")
    @Primary
    public ConnectionFactory orderConnectionFactory(
            @Value("${spring.rabbitmq.one.addresses}") String addresses,
            @Value("${spring.rabbitmq.one.username}") String username,
            @Value("${spring.rabbitmq.one.password}") String password)
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
     /*   ConfirmCallback：每一条发到rabbitmq server的消息都会调一次confirm方法。
        如果消息成功到达exchange，则ack参数为true，反之为false；
        cause参数是错误信息；
        CorrelationData可以理解为context，在发送消息时传入的这个参数，此时会拿到。
        ReturnCallback：成功到达exchange，但routing不到任何queue时会调用。*/

        return connectionFactory;
    }

/*    // 多源2,MQ
    @Bean(name = "twoConnectionFactory")
    public ConnectionFactory csfSouthConnectionFactory(
            @Value("${spring.rabbitmq.two.addresses}") String addresses,
            @Value("${spring.rabbitmq.two.username}") String username,
            @Value("${spring.rabbitmq.two.password}") String password,
            @Value("${spring.rabbitmq.two.virtual-host}") String virtualHost) throws IOException {
        return createConnectFactory(addresses, username, password, virtualHost);
    }

    private CachingConnectionFactory createConnectFactory(String addresses, String username, String password, String virtualHost)
    {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(addresses);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        Channel channel = connectionFactory.createConnection().createChannel(false);
        // 声明queue,exchange,以及绑定
        try {
            channel.exchangeDeclare(Exchange , "topic",true);
            channel.queueDeclare(Queue, true, false, false, null);
            channel.queueBind(Queue, Exchange, routingkey);
        } catch (Exception e) {
            log.error("mq declare queue exchange fail ", e);
        } finally {
            try {
                channel.close();
            } catch (Exception e) {
                log.error("mq channel close fail", e);
            }
        }
        return connectionFactory;
    }*/

    /**
     * 配置发送
     * @param //在springboot 整合rabbitmq下  rabbitTemplate 默认是单例形式
     * 如果仅是发送队列和接受队列消息 该单例模式就足够使用了
     * 如果想要 对于 发布端进行消息推送确认，那么单例模式是无法满足的
     * 如果我们有多个队列，并需要对每个队列发送是否成功的消息进行确认
     * 这种情况下，如果是单例模式，那么整个项目中，仅有个一confirm 和 returncallback 实现接口
     * 对于所有的队列消息的确认也只能在这一个接口中得到回复，这样就很难辨别确认的消息响应是具体哪个队列的。
     *
     * 所以这样的情况下，单例是无法满足的，因此需要使用多例模式
     * 另注意  使用多例时   因默认是单例默认是  A调用注入A 每次发送还是A  所以调用方也要设置为多例.
     * @return
     */
    @Bean(name = "oneRabbitTemplate")
    @Primary
    public RabbitTemplate orderRabbitTemplate(
            @Qualifier("oneConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new RabbitTemplate(connectionFactory);
    }

/*
    @Bean(name = "twoRabbitTemplate")
    public RabbitTemplate csfSouthRabbitTemplate(
            @Qualifier("twoConnectionFactory") ConnectionFactory connectionFactory
    ) {
        return new RabbitTemplate(connectionFactory);
    }
*/


    /**
     *  配置接收端属性
     * @param configurer
     * @param connectionFactory
     * @return
     */
    @Bean(name = "oneContainerFactory")
    public SimpleRabbitListenerContainerFactory orderContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            @Qualifier("oneConnectionFactory") ConnectionFactory connectionFactory
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }


    @Bean
    public Queue delayOrderQueue() {
        Map<String, Object> params = new HashMap<>();
        // x-dead-letter-exchange 声明了队列里的死信转发到的DLX名称，
        params.put("x-dead-letter-exchange", "exchange1.dlx");
        // x-dead-letter-routing-key 声明了这些死信在转发时携带的 routing-key 名称。
        params.put("x-dead-letter-routing-key", "key1.dlx");
        return new Queue("DELAY_QUEUE", true, false, false, params);
    }


    /**
     * 需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。
     * 这是一个完整的匹配。如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，
     * 不会转发dog.puppy，也不会转发dog.guard，只会转发dog。
     * @return DirectExchange
     */
    //exchange： 交换机  routingkey: 路由key
    @Bean
    public DirectExchange orderDelayExchange() {
        return new DirectExchange("DELAY_EXCHANGE");
    }
    @Bean
    public Binding dlxBinding() {
        return BindingBuilder.bind(delayOrderQueue()).to(orderDelayExchange()).with("DELAY_ROUTING_KEY");
    }


    //queue: 队列
    @Bean
    public Queue orderQueue() {
        return new Queue("queue", true);
    }

    /**
     * 将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。
     * 符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”。
     **/
    @Bean
    public TopicExchange orderTopicExchange() {
        return new TopicExchange("exchange1.dlx");
    }

    @Bean
    public Binding orderBinding() {
        // TODO 如果要让延迟队列之间有关联,这里的 routingKey 和 绑定的交换机很关键
        return BindingBuilder.bind(orderQueue()).to(orderTopicExchange()).with("key1.dlx");
    }
}
