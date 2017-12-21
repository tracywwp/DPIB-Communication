package com.spdb.ib.dpib.cxf.client;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class WebServiceClient {
	/**
	 * @Title: callService
	 * @Description: 调用远程的webservice并返回数据
	 * @param wsUrl ws地址
	 * @param method 调用的ws方法名
	 * @param arg 参数
	 * @return String
	 */
	public static String callService(String wsUrl, String method, Object... arg) {
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient(wsUrl);
		Object[] res = null;
		try {
			res = client.invoke(method, arg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (String) res[0];
	}

	public static void main(String[] args) {

		System.out
				.println(WebServiceClient
						.callService(
								"http://localhost:8888/DPIB-ETG-QZ/ws/messageCxf?wsdl",
								"messageParsing",
								"68010010    1109    10010360000000000000000000000000000000000000<?xml version=\"1.0\" encoding=\"UTF-8\"?><Envelope><Header><Fund_Id>6801001</Fund_Id><Trade_Code>1001</Trade_Code><Error_Code></Error_Code><Error_Message></Error_Message></Header><Body><record><A>CBA</A><B>NBA</B><GH>12</GH><CD>36</CD><EF>AC</EF><Member_Id>666</Member_Id><Member_Name>1</Member_Name><Protocol_Code>888011</Protocol_Code><Organization_Code>南京东路</Organization_Code><Register_Date>888019</Register_Date><Address>南京东路100号</Address><Contact_Person>李文理</Contact_Person><Telephone>13987653210</Telephone><Fax>705231315</Fax><Mobile>13987653210</Mobile><Email>liwenl@neusoft.com</Email><Represent_Name>李文理</Represent_Name></record><record><A>程恒锋</A><B>李文理</B><GH>吴吾平</GH><CD>陈慎</CD><EF>任彩玉</EF><Member_Id>20</Member_Id><Member_Name>2</Member_Name><Protocol_Code>888011</Protocol_Code><Organization_Code>北京东路</Organization_Code><Register_Date>888019</Register_Date><Address>南京东路100号</Address><Contact_Person>吴吾平</Contact_Person><Telephone>13012369852</Telephone><Fax>705231316</Fax><Mobile>13012369852</Mobile><Email>wuwp@neusoft.com</Email><Represent_Name>李文理</Represent_Name></record></Body></Envelope>","10.112.71.34"));
	}
}
