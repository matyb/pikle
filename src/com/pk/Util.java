/*
 * Created on Jul 23, 2004
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.pk;

/**
 * @author Isabelle
 * 
 */
public class Util
{
    public static String lPad(String argString, int size, String argPadChar)
    {
        if (argString == null)
        {
            return null;
        }
        int padLen = argPadChar.length();
        int strLen = argString.length();
        int numberOfpads = size - strLen;
        if (numberOfpads <= 0)
        {
            return argString; 
        }

        if (numberOfpads == padLen)
        {
            return argPadChar.concat(argString);
        } else if (numberOfpads < padLen)
        {
            return argPadChar.substring(0, numberOfpads).concat(argString);
        } else
        {
            char[] padding = new char[numberOfpads];
            char[] padChars = argPadChar.toCharArray();
            for (int i = 0; i < numberOfpads; i++)
            {
                padding[i] = padChars[i % padLen];
            }
            return new String(padding).concat(argString);
        }
    }
	public static String escapeHTML(String s)
	{
		StringBuffer sb = new StringBuffer();
		int n = s.length();
		for (int i = 0; i < n; i++)
			{
			char c = s.charAt(i);
			switch (c)
				{
				case '<' :
					sb.append("&lt;");
					break;
				case '>' :
					sb.append("&gt;");
					break;
				case '&' :
					sb.append("&amp;");
					break;
				case '"' :
					sb.append("&quot;");
					break;
				case '�' :
					sb.append("&agrave;");
					break;
				case '�' :
					sb.append("&Agrave;");
					break;
				case '�' :
					sb.append("&acirc;");
					break;
				case '�' :
					sb.append("&Acirc;");
					break;
				case '�' :
					sb.append("&auml;");
					break;
				case '�' :
					sb.append("&Auml;");
					break;
				case '�' :
					sb.append("&aring;");
					break;
				case '�' :
					sb.append("&Aring;");
					break;
				case '�' :
					sb.append("&aelig;");
					break;
				case '�' :
					sb.append("&AElig;");
					break;
				case '�' :
					sb.append("&ccedil;");
					break;
				case '�' :
					sb.append("&Ccedil;");
					break;
				case '�' :
					sb.append("&eacute;");
					break;
				case '�' :
					sb.append("&Eacute;");
					break;
				case '�' :
					sb.append("&egrave;");
					break;
				case '�' :
					sb.append("&Egrave;");
					break;
				case '�' :
					sb.append("&ecirc;");
					break;
				case '�' :
					sb.append("&Ecirc;");
					break;
				case '�' :
					sb.append("&euml;");
					break;
				case '�' :
					sb.append("&Euml;");
					break;
				case '�' :
					sb.append("&iuml;");
					break;
				case '�' :
					sb.append("&Iuml;");
					break;
				case '�' :
					sb.append("&ocirc;");
					break;
				case '�' :
					sb.append("&Ocirc;");
					break;
				case '�' :
					sb.append("&ouml;");
					break;
				case '�' :
					sb.append("&Ouml;");
					break;
				case '�' :
					sb.append("&oslash;");
					break;
				case '�' :
					sb.append("&Oslash;");
					break;
				case '�' :
					sb.append("&szlig;");
					break;
				case '�' :
					sb.append("&ugrave;");
					break;
				case '�' :
					sb.append("&Ugrave;");
					break;
				case '�' :
					sb.append("&ucirc;");
					break;
				case '�' :
					sb.append("&Ucirc;");
					break;
				case '�' :
					sb.append("&uuml;");
					break;
				case '�' :
					sb.append("&Uuml;");
					break;
				case '�' :
					sb.append("&reg;");
					break;
				case '�' :
					sb.append("&copy;");
					break;
				case '�' :
					sb.append("&euro;");
					break;
				case '\'' :
					sb.append("&#39;");
					break;
					// be carefull with this one (non-breaking whitee space)
					//case ' ': sb.append("&nbsp;");break;         

				default :
					sb.append(c);
					break;
			}
		}
		return sb.toString();
	}
}