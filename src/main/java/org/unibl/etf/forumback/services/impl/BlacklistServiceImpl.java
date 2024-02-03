package org.unibl.etf.forumback.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.models.entities.BlacklistEntity;
import org.unibl.etf.forumback.repositories.BlacklistRepository;
import org.unibl.etf.forumback.services.BlacklistService;
import org.unibl.etf.forumback.services.JwtService;

@Service
public class BlacklistServiceImpl implements BlacklistService {

    private final BlacklistRepository blacklistRepository;

    public BlacklistServiceImpl(BlacklistRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Override
    public void addToBlacklist(String token) {
        BlacklistEntity blacklistEntity = new BlacklistEntity();
        blacklistEntity.setId(null);
        blacklistEntity.setToken(token);
        blacklistRepository.saveAndFlush(blacklistEntity);
    }

    @Override
    public boolean existsByToken(String token) {
        return blacklistRepository.existsByToken(token);
    }


}
