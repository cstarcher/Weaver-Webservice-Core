/* 
 * CoreAuthController.java 
 * 
 * Version: 
 *     $Id$ 
 * 
 * Revisions: 
 *     $Log$ 
 */
package edu.tamu.weaver.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.tamu.weaver.aspect.annotation.ApiData;
import edu.tamu.weaver.aspect.annotation.ApiParameters;
import edu.tamu.weaver.model.ApiResponse;
import edu.tamu.weaver.util.AuthUtility;
import edu.tamu.weaver.util.EmailSender;
import edu.tamu.weaver.util.JwtUtility;

@Deprecated
public abstract class CoreAuthController {

    protected final static String EMAIL_VERIFICATION_TYPE = "EMAIL_VERIFICATION";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthUtility authUtility;

    @Autowired
    protected JwtUtility jwtUtility;

    @Autowired
    protected EmailSender emailSender;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    public abstract ApiResponse registration(@ApiData Map<String, String> dataMap, @ApiParameters Map<String, String[]> parameters);

    public abstract ApiResponse login(@ApiData Map<String, String> dataMap);

}
