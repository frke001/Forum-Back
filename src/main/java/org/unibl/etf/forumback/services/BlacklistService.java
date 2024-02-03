package org.unibl.etf.forumback.services;

public interface BlacklistService {

    void addToBlacklist(String token);

    boolean existsByToken(String token);


}
