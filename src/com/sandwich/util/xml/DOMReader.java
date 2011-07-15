package com.sandwich.util.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMReader implements XMLReader {

	@Override
	public void read(File xmlFile, List<DeferredInvocation> methodCalls) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			for(DeferredInvocation method : methodCalls){
				List<String[]> params = getChildNodesParams(doc.getDocumentElement(), method);
				for(String[] paramsArray : params){
					method.getMethod().invoke(method.getReceiver(), (Object[])paramsArray);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private List<String[]> getChildNodesParams(Element element, DeferredInvocation method) {
		List<List<String>> allParams = new ArrayList<List<String>>();
		for(String path : method.getPath()){
			String nodeName = element.getNodeName();
			if(element.getNodeType() == Element.ELEMENT_NODE && nodeName.equals(path)){
				if(method.getPath().indexOf(path) != (method.getPath().size() - 1)){
					element = getNextElement(element, method, path);
				}else{
					addAllParamsFromElementAndSiblings(element, method, allParams, path);
				}
			}
		}
		List<String[]> params = new ArrayList<String[]>();
		for(List<String> listOfParams : allParams){
			params.add(listOfParams.toArray(new String[listOfParams.size()]));
		}
		return params;
	}

	private void addAllParamsFromElementAndSiblings(Element element,
			DeferredInvocation method, List<List<String>> allParams, String path) {
		Node n = element;
		do{
			if(n.getNodeType() == Element.ELEMENT_NODE){
				List<String> params = new ArrayList<String>();
				NodeList childNodes = n.getChildNodes();
				for(int i = 0; i < childNodes.getLength(); i++){
					Node no = childNodes.item(i);
					if(no.getNodeType() != Element.ELEMENT_NODE){
						continue;
					}
					for(String elementName : method.getElements()){
						if(elementName.equals(no.getNodeName())){
							params.add(no.getTextContent());
							break;
						}
					}
				}
				allParams.add(params);
			}
			n = n.getNextSibling();
		}while(n != null && (n.getNodeType() != Element.ELEMENT_NODE || path.equals(n.getNodeName())));
	}

	private Element getNextElement(Element element, DeferredInvocation method,
			String path) {
		String nodeName;
		NodeList nodeList = element.getElementsByTagName(method.getPath().get(method.getPath().indexOf(path) + 1));
		for(int i = 0; i < nodeList.getLength(); i++){
			Element n = (Element)nodeList.item(i);
			nodeName = n.getNodeName();
			if(n.getNodeType() == Element.ELEMENT_NODE && nodeName.equals(method.getPath().get(method.getPath().indexOf(path) + 1))){
				element = n;
				break;
			}
		}
		return element;
	}
	
}
