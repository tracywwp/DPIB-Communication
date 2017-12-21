package com.spdb.ib.dpib.cxf.message.service;

import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface MessageCxfService {
	public String messageParsing(@WebParam(name = "request") String request);
}
