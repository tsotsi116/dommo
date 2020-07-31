package com.nndi_tech.labs.dommo;

import com.activepersistence.service.Base;
import com.nndi_tech.labs.dommo.dto.VerificationActionDTO;
import com.nndi_tech.labs.dommo.entities.MobileMoneyPayment;
import com.nndi_tech.labs.dommo.entities.User;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

public class MobileMoneyService extends AbstractService<MobileMoneyPayment> {

    @PersistenceContext
    private final EntityManagerFactory entityManagerFactory;

    private final UserService userService;

    public MobileMoneyService(EntityManagerFactory entityManagerFactory, UserService userService) {
        super(MobileMoneyPayment.class);
        this.entityManagerFactory = entityManagerFactory;
        this.userService = userService;
    }

    @Override
    public EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public void verifyOrCancel(MobileMoneyPayment payment, VerificationActionDTO action) {
        User user = userService.findBy("user.username = ?", action.actionBy());
        payment.verifyOrCancel(action, user);
        this.save(payment);
    }

    public void recordUnverifiedPayment(MobileMoneyPayment payment) {
        payment.setVerified(false);
        payment.setCancelled(false);
        this.save(payment);
    }
}
