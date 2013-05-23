/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.opendomotic.service;

import com.opendomotic.model.Teste;
import java.util.List;
import java.util.Random;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.UserTransaction;

/**
 *
 * @author jaques
 */
@Stateless
public class TesteService {
    
    @PersistenceContext
    private EntityManager em;
    
    public void adicionar() {
        Teste t = new Teste();
        t.setNome("Teste"+new Random().nextInt(99));
        em.persist(t);
    }
    
    public void excluir() {
        em.createQuery("delete from Teste").executeUpdate();
    }
    
    public List<Teste> getList() {
        TypedQuery<Teste> query = em.createQuery(
            "SELECT t FROM Teste t ORDER BY t.id", Teste.class);
        return query.getResultList();
    }        
    
}
