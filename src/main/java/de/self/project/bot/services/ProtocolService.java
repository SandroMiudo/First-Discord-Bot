package de.self.project.bot.services;

import de.self.project.bot.logic.Protocol;
import org.springframework.stereotype.Service;

@Service
public class ProtocolService {

    private ProtocolRepo protocolRepo;

    public ProtocolService(ProtocolRepo protocolRepo){
        this.protocolRepo = protocolRepo;
    }

    public void addProtocol(Protocol protocol){
        protocolRepo.addProtocol(protocol);
    }
}
