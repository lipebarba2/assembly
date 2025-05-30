package com.lorandi.assembly.resource;

import com.lorandi.assembly.event.consumer.MessengerConsumerService;
import com.lorandi.assembly.event.producer.MessengerPublisherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messenger")
@RequiredArgsConstructor
public class MessengerResource {
    private final MessengerPublisherService publisherService;
    private final MessengerConsumerService consumerService;
    private final RabbitTemplate rabbitTemplate;
    @PostMapping("/publish/{message}")
    @Operation(summary = "Search survey by id",
            responses = {@ApiResponse(responseCode = "200", description = "Resource successfully retrieved" )})
    public void send(@PathVariable String message)  {
        publisherService.send(message);
    }

    @PostMapping("/{message}")
    @Operation(summary = "Search survey by id",
            responses = {@ApiResponse(responseCode = "200", description = "Resource successfully retrieved" )})
    public String sendDirect(@PathVariable String message)  {

        rabbitTemplate.convertAndSend("Direct-Exchange", "Aprovado", message);
        rabbitTemplate.convertAndSend("Direct-Exchange", "Reprovado", message);
        rabbitTemplate.convertAndSend("Direct-Exchange", "Empate", message);
        return "Message sent to the RabbitMQ Successfully";

    }

    @PostMapping("/consumer")
    @Operation(summary = "Search survey by id",
            responses = {@ApiResponse(responseCode = "200", description = "Resource successfully retrieved" )})
    public void consumer() {
        consumerService.directConsumer();
    }

}
