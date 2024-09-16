package de.self.project.bot.db;

import de.self.project.bot.logic.Protocol;
import de.self.project.bot.services.ProtocolRepo;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

@Repository
public class ProtocolRepoImpl implements ProtocolRepo {

    private DaoProtocolRepo daoProtocolRepo;

    public ProtocolRepoImpl(DaoProtocolRepo daoProtocolRepo) {
        this.daoProtocolRepo = daoProtocolRepo;
    }

    @Override
    public void addProtocol(Protocol protocol) {
        try {
            ProtocolEntity protocolEntity = new ProtocolEntity(protocol.getTime(),
                    protocol.getProtocolTypName(),protocol.getRawContentOfFileAttach());
            daoProtocolRepo.save(protocolEntity);
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProtocolEntity getProtocol() {
        ProtocolEntity found = daoProtocolRepo.findByTimeIsLessThanEqual(LocalDateTime.now());
        if(found != null){
            daoProtocolRepo.delete(found);
        }
        return found;
    }
}
