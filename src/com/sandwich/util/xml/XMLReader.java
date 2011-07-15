package com.sandwich.util.xml;

import java.io.File;
import java.util.List;

public interface XMLReader {

	void read(File xmlFile, List<DeferredInvocation> method);

}
