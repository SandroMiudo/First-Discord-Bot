package de.self.project.bot.services;

import de.self.project.bot.db.ProtocolEntity;
import de.self.project.bot.logic.Protocol;

public interface ProtocolRepo {
    void addProtocol(Protocol protocol);
    ProtocolEntity getProtocol();
}
