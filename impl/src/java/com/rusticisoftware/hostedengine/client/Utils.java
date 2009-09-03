package com.rusticisoftware.hostedengine.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class Utils {
	public static String getXmlString (Document xmlDoc) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		//initialize StreamResult with File object to save to file
		StreamResult result = new StreamResult(new StringWriter());
		DOMSource source = new DOMSource(xmlDoc);
		transformer.transform(source, result);
		
		return result.getWriter().toString();
	}
    
    public static String getXmlString (Node xmlNode) throws TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        //initialize StreamResult with File object to save to file
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(xmlNode);
        transformer.transform(source, result);
        
        return result.getWriter().toString();
    }
    

    public static Document parseXmlString (String xmlString) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = dbf.newDocumentBuilder();
        return docBuilder.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8")));
    }
    
    public static String xmlEncode (String str)
    {
        return str.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace("<", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&apos;");
    }
    
  //Copy inStream to outStream using Java's built in buffering, closing outStream when complete
    public static boolean bufferedCopyStream(InputStream inStream, OutputStream outStream) throws Exception {
        return bufferedCopyStream(inStream, outStream, true);
    }
    
    //Copy inStream to outStream using Java's built in buffering
    public static boolean bufferedCopyStream(InputStream inStream, OutputStream outStream, boolean closeStream) throws Exception {
        BufferedInputStream bis = new BufferedInputStream( inStream );
        BufferedOutputStream bos = new BufferedOutputStream ( outStream );
        while(true){
            int data = bis.read();
            if (data == -1){
                break;
            }
            bos.write(data);
        }
        bos.flush();
        if(closeStream){
            bos.close();
        }
        return true;
    }
    
  //Return String representation of current date, in UTC timezone
    public static String getFormattedTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        formatter.getCalendar().setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }
    
    public static Date parseFormattedTime(String formattedTime) throws ParseException{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.parse(formattedTime);
    }
    
    public static String getEncodedParam(String paramName, String paramVal) throws Exception {
        return URLEncoder.encode(paramName, "UTF-8") + "=" + URLEncoder.encode(paramVal, "UTF-8");
    }
    
    public static boolean isNullOrEmpty(String val){
        return (val == null || val == "");
    }
    
    public static String getNonXmlPayloadFromResponse(Document response){
        return response.getElementsByTagName("rsp").item(0).getTextContent();
    }
}
