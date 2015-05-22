package edu.tamu.app.model.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

import edu.tamu.app.config.TestDataSourceConfiguration;
import edu.tamu.app.model.repo.UserRepo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestDataSourceConfiguration.class})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
public class UserTest {
	
	@Autowired
	private UserRepo userRepo;
	
	@Before
	public void setUp() {
	}
	
	@Test
	public void testCreateAndDelete() {
		
		userRepo.create(123456789l);		
		UserImpl testUser1 = userRepo.getUserByUin(123456789l);				
		assertTrue("Test User1 was not added.", testUser1.getUin().equals(123456789l));
		
		userRepo.delete(testUser1);				
		assertEquals("Test User1 was not removed.", 0, userRepo.findAll().size());
	}
	
	@Test
	public void testDuplicateUser() {
		
		userRepo.create(123456789l);		
		UserImpl testUser1 = userRepo.getUserByUin(123456789l);				
		assertTrue("Test User1 was not added.", testUser1.getUin().equals(123456789l));
		
		userRepo.create(123456789l);
		
		List<UserImpl> allUsers = (List<UserImpl>) userRepo.findAll();		
		assertEquals("Duplicate UIN found.", 1, allUsers.size());
	}
			
	@After
	public void cleanUp() {
		for(UserImpl user : userRepo.findAll())
			userRepo.delete(user);
	}
}
