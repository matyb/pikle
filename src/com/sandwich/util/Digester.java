package com.sandwich.util;

public interface Digester {
	Digester addCallMethod(String...params);
	void parse(String fileName);
}
