package com.sandwich.util.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

public class DeferredInvocationTest {

	@Test(expected=IllegalArgumentException.class)
	public void testConstructionNullParam() throws Exception {
		DeferredInvocation.getInstance(null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructionReceiverParam() throws Exception {
		DeferredInvocation.getInstance(this);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConstructionReceiverAndOneParam() throws Exception {
		DeferredInvocation.getInstance(this, "");
	}
	
	@Test(expected=RuntimeException.class)
	public void testConstructionReceiverAndOneParamInvalidMethodName() throws Exception {
		DeferredInvocation.getInstance(this, "", "meh");
	}
	
	@Test
	public void testConstructionReceiverAndTwoParams() throws Exception {
		Method method = getClass().getMethod("someMethodThatTakesString", String.class);
		DeferredInvocation i = DeferredInvocation.getInstance(this, "1", method.getName());
		assertEquals(Arrays.asList("1"), i.getElements());
		assertEquals(method, i.getMethod());
		Iterator<String> iter = i.getPath().iterator();
		assertEquals("1", iter.next());
		assertFalse(iter.hasNext());
		assertSame(this, i.getReceiver());
	}
	
	@Test
	public void testConstructionReceiverAndTwoParams_wSeparatorFirst() throws Exception {
		Method method = getClass().getMethod("someMethodThatTakesString", String.class);
		DeferredInvocation i = DeferredInvocation.getInstance(this, "/1", method.getName());
		assertEquals(Arrays.asList("1"), i.getElements());
		assertEquals(method, i.getMethod());
		Iterator<String> iter = i.getPath().iterator();
		assertEquals("1", iter.next());
		assertFalse(iter.hasNext());
		assertSame(this, i.getReceiver());
	}
	
	@Test
	public void testConstructionReceiverAndTwoParams_wSeparatorBeweenPathComponents_noParams() throws Exception {
		Method method = getClass().getMethod("someMethodThatTakesString", String.class);
		DeferredInvocation i = DeferredInvocation.getInstance(this, "/1/2", method.getName());
		assertEquals(Arrays.asList("2"), i.getElements());
		assertEquals(method, i.getMethod());
		Iterator<String> iter = i.getPath().iterator();
		assertEquals("1", iter.next());
		assertFalse(iter.hasNext());
		assertSame(this, i.getReceiver());
	}
	
	@Test
	public void testConstruction_oneParam() throws Exception {
		Method method = getClass().getMethod("someMethodThatTakesString", String.class);
		DeferredInvocation i = DeferredInvocation.getInstance(this, "/1/2", method.getName(), "3");
		assertEquals(Arrays.asList("3"), i.getElements());
		assertEquals(method, i.getMethod());
		Iterator<String> iter = i.getPath().iterator();
		assertEquals("1", iter.next());
		assertEquals("2", iter.next());
		assertFalse(iter.hasNext());
		assertSame(this, i.getReceiver());
	}
	
	@Test
	public void testConstruction_twoParams() throws Exception {
		Method method = getClass().getMethod("someMethodThatTakesStrings", String.class, String.class);
		DeferredInvocation i = DeferredInvocation.getInstance(this, "/1/2", method.getName(), "3", "4");
		assertEquals(Arrays.asList("3", "4"), i.getElements());
		assertEquals(method, i.getMethod());
		Iterator<String> iter = i.getPath().iterator();
		assertEquals("1", iter.next());
		assertEquals("2", iter.next());
		assertFalse(iter.hasNext());
		assertSame(this, i.getReceiver());
	}
	
	public void someMethodThatTakesString(String s){}
	public void someMethodThatTakesStrings(String s, String s2){}
	
}
