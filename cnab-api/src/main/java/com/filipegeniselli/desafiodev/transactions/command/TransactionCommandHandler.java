package com.filipegeniselli.desafiodev.transactions.command;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionCommandHandler implements TransactionCommandService {

    private final StorageService storageService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String topicName;

    @Autowired
    public TransactionCommandHandler(StorageService storageService,
                                     KafkaTemplate<String, String> kafkaTemplate,
                                     @Value("${cnabparser.topics.cnabprocessing-topic}")
                                     String topicName) {
        this.storageService = storageService;
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    @Override
    @Transactional
    public Integer handle(UploadCnabFileCommand command) {
        Integer processId = this.storageService.storeAndCreateProcessRecord(command.file());
        this.kafkaTemplate.send(this.topicName, String.valueOf(processId));

        return processId;
    }

}
