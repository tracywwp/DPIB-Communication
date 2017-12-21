package com.spdb.ib.dpib.cxf.message.service.impl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.cxf.transport.http.AbstractHTTPDestination;

import com.spdb.ib.dpib.cxf.message.service.MessageCxfService;

@SuppressWarnings("restriction")
@WebService
public class MessageCxfServiceImpl implements MessageCxfService {

	@Resource
	private WebServiceContext context;

	public String messageParsing(String request) {
		String response = "";
		try {
			MessageContext messageContext = context.getMessageContext();
			HttpServletRequest req = (HttpServletRequest) messageContext
					.get(AbstractHTTPDestination.HTTP_REQUEST);
			String ip = req.getRemoteAddr();
			if (null != request.trim() && request.trim().length() > 0) {
				response ="";
			} else {
				response = "=====请输入报文数据====";
			}
		} catch (Exception e) {
			response = e.getMessage().toString();
			e.printStackTrace();
		}
		return response;
	}

	
}
