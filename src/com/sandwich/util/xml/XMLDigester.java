package com.sandwich.util.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.sandwich.util.Digester;

public class XMLDigester implements Digester {

	private final Object receiver;
	private final List<DeferredInvocation> methodCalls;
	
	public XMLDigester(Object receiver){
		if(receiver == null){
			throw new NullPointerException("The receiver may not be null");
		}
		this.receiver = receiver;
		methodCalls = new ArrayList<DeferredInvocation>();
	}

	@Override
	public XMLDigester addCallMethod(String...params) {
		methodCalls.add(DeferredInvocation.getInstance(receiver, params));
		return this;
	}

	@Override
	public void parse(String fileName) {
		parse(fileName, new DOMReader());
	}
	
	public void parse(String fileName, XMLReader reader){
		File file = new File(fileName);
		if(file.exists()){
			reader.read(file, methodCalls);
		}else{
			throw new RuntimeException(new FileNotFoundException(file.getAbsolutePath()+" was not found"));
		}
	}
	
	public String toString(){
		return getClass().getSimpleName() + "[" + "Receiver: "+receiver + " [" + methodCalls.toString() + "]]";
	}
	
}
