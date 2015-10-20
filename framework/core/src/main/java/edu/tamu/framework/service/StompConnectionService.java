package edu.tamu.framework.service;

import org.springframework.stereotype.Service;

@Service
public class StompConnectionService {

	private static int totalActiveConnections = 0;
	
	public synchronized void incrementActiveConnection() {
		totalActiveConnections++;
	}
	
	public synchronized void decrementActiveConnection() {
		totalActiveConnections--;
	}
	
	public synchronized int getActiveConnections() {
		return totalActiveConnections;
	}

}