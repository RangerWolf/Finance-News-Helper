package utils;
/*
 * The MIT License
 *
 * Copyright 2014 Bhathiya.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */



/**
 * https://github.com/JaDogg/SimpleMatch/blob/master/src/main/java/info/simpll/simplematch/SimpleMatch.java
 * simple pattern matcher : match * -> one or more of any , ? -> single any
 *
 * @author Bhathiya Perera
 */
public class SimpleMatch {

    //state enums
    private static enum State {

        JUST_STARTED, NORMAL, EAGER, END
    }

    //constants
    private static final char MATCH_ALL = '*';
    private static final char MATCH_ONE = '?';

    private final int ptnOutBound; // pattern out bound
    private final int strOutBound; // string out bound
    private final String pattern; // pattern
    private final String matchString; // string to match

    private int ptnPosition; // position of pattern
    private int strPosition; // position of string
    private State state = State.JUST_STARTED; // state
    private boolean matchFound = false; // is match

    public SimpleMatch(String pattern, String matchStr) {

        if (pattern == null || matchStr == null) {
            throw new IllegalArgumentException(
                    "Pattern and String must not be null");
        }

        this.pattern = pattern;
        this.matchString = matchStr;
        int pl = pattern.length();
        int sl = matchStr.length();
        if (pl == 0 || sl == 0) {
            throw new IllegalArgumentException(
                    "Pattern and String must have at least one character");
        }
        ptnOutBound = pl - 1;
        strOutBound = sl - 1;
        ptnPosition = 0;
        strPosition = 0;

    }

    private void calcState() {
        //calculate state
        if (state == State.END) {
            return;
        }

        if (!patternCheckBound() || !matchStrCheckBound()) {
            state = State.END;
        } else if (patternChar() == MATCH_ALL) {
            if (!patternNextCheckBound()) {
                state = State.END;
                matchFound = true;
            } else {
                state = State.EAGER;
            }
        } else {
            state = State.NORMAL;
        }
    }

    private void eat() {
        //eat a character
        if (state == State.END) {
            return;
        }

        matchFound = false;

        if (state == State.EAGER) {

            int curStrPosition = strPosition;
            int curPtnPosition = ptnPosition;
            strPosition++;
            ptnPosition++;
            if (match()) {
                state = State.END;
                matchFound = true;
                return;
            } else {
                strPosition = curStrPosition;
                ptnPosition = curPtnPosition;
                state = State.EAGER;
            }
            strPosition++;
        } else if (state == State.NORMAL) {
            if (matchOne()) {
                strPosition++;
                ptnPosition++;
                matchFound = true;
            } else {
                state = State.END;
            }
        }
    }

    private boolean matchOne() {
        // match one
        char pc = patternChar();
        return (pc == MATCH_ONE || pc == matchStrChar());
    }

    private char patternChar() {
        // pattern current char
        return pattern.charAt(ptnPosition);
    }

    private char matchStrChar() {
        // str current char
        return matchString.charAt(strPosition);
    }

    private boolean patternCheckBound() {
        //pattern position bound check
        return ptnPosition <= ptnOutBound;
    }

    private boolean patternNextCheckBound() {
        //pattern next position bound check
        return (ptnPosition + 1) <= ptnOutBound;
    }

    private boolean matchStrCheckBound() {
        //string bound check
        return strPosition <= strOutBound;
    }

    /**
     * Match and return result
     *
     * @return true if match
     */
    public boolean match() {
        if (ptnOutBound > strOutBound) {
            return false;
        }
        while (state != State.END) {
            calcState();
            eat();
        }
        return matchFound;
    }

    /**
     * Match and return result
     *
     * @param pattern pattern
     * @param matchStr string to match
     * @return true if match
     * @throws IllegalArgumentException
     */
    public static boolean match(String pattern, String matchStr) throws
            IllegalArgumentException {
        return new SimpleMatch(pattern, matchStr).match();
    }
    
    public static void main(String[] args) {
		
    	System.out.println(SimpleMatch.match("http://*.tgbus.com/*", "http://3ds.tgbus.com/news/newslist/201408/20140829105321.shtml"));
    	
	} 

}