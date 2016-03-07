/* 
 * HttpRequest.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.framework.model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Http request. Created and stored in memory when a new request goes through
 * the interceptor. Is retrieved and removed when the aspect point cuts an
 * endpoint.
 * 
 * @author <a href="mailto:jmicah@library.tamu.edu">Micah Cooper</a>
 * @author <a href="mailto:jcreel@library.tamu.edu">James Creel</a>
 * @author <a href="mailto:huff@library.tamu.edu">Jeremy Huff</a>
 * @author <a href="mailto:jsavell@library.tamu.edu">Jason Savell</a>
 * @author <a href="mailto:wwelling@library.tamu.edu">William Welling</a>
 *
 */
public class HttpRequest {

	private HttpServletRequest request;

	private HttpServletResponse response;

	private String user;

	private String destination;

	public HttpRequest(HttpServletRequest request, HttpServletResponse response, String user, String destination) {
		this.request = request;
		this.response = response;
		this.user = user;
		this.destination = destination;
	}

	/**
	 * Gets request.
	 * 
	 * @return HttpServletRequest
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Sets request.
	 * 
	 * @param request
	 *            HttpServletRequest
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Gets response.
	 * 
	 * @return HttpServletResponse
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Sets response.
	 * 
	 * @param response
	 *            HttpServletResponse
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Gets user.
	 * 
	 * @return String
	 */
	public String getUser() {
		return user;
	}

	/**
	 * Sets user.
	 * 
	 * @param user
	 *            String
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * Gets destination.
	 * 
	 * @return String
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Sets destination.
	 * 
	 * @param destination
	 *            String
	 */
	public void setDestination(String destination) {
		this.destination = destination;
	}

}