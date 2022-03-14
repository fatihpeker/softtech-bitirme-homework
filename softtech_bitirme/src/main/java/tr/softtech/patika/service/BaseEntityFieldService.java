package tr.softtech.patika.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import tr.softtech.patika.model.BaseEntity;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class BaseEntityFieldService<E extends BaseEntity> {

    //Tüm entity ler için create ve update bilgilerini giriyor
    public void addBaseEntityProperties(E entity){
        String creatorName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (entity.getCreateDate()==null){
            entity.setCreateDate(new Date());
        }
        entity.setUpdateDate(new Date());
        if (entity.getCreatedBy()==null){
            entity.setCreatedBy(creatorName);
        }
        entity.setUpdatedBy(creatorName);
    }

    //Güncel kullanıcı adı security contex ten alınamadığında kullanmak için
    public void addBaseEntityFieldWithoutContextHolder(E entity, String username){
        if (entity.getCreateDate()==null){
            entity.setCreateDate(new Date());
        }
        entity.setUpdateDate(new Date());
        if (entity.getCreatedBy()==null){
            entity.setCreatedBy(username);
        }
        entity.setUpdatedBy(username);
    }

}
