package org.unibl.etf.forumback.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.unibl.etf.forumback.models.entities.SIEMEntity;
import org.unibl.etf.forumback.repositories.SIEMRepository;
import org.unibl.etf.forumback.services.SIEMService;

import java.util.Date;

@Service
@Transactional
public class SIEMServiceImpl implements SIEMService {

    private final SIEMRepository siemRepository;

    public SIEMServiceImpl(SIEMRepository siemRepository) {
        this.siemRepository = siemRepository;
    }

    @Override
    public void log(String text) {
        SIEMEntity siemEntity = new SIEMEntity();
        siemEntity.setId(null);
        siemEntity.setText(text);
        siemEntity.setCreationDate(new Date());
        siemRepository.saveAndFlush(siemEntity);
    }
}
