package utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.BlockPattern;

public class BlockPatternVerifyUtils {

	public static Boolean verify(List<BlockPattern> patternList, String targetStr) {
		Boolean shouldBeFilteredOut = false;
		for(BlockPattern bp : patternList) {
			String pattern = bp.getPattern();
			BlockPattern.Method method = bp.getMethod();
			
			if(method == BlockPattern.Method.SIMPLE_MATCH) {
				if(SimpleMatch.match(pattern, targetStr)) {
					shouldBeFilteredOut = true;
					break;
				}
			} else if(method == BlockPattern.Method.REGEX) {
				Pattern p = Pattern.compile(bp.getPattern());
				Matcher matcher = p.matcher(targetStr);
				if(matcher.matches() == true) {
					shouldBeFilteredOut = true;
					break;
				}
			} else if(method == BlockPattern.Method.FULL_TEXT_SEARCH) {
				if(targetStr.contains(pattern)) {
					shouldBeFilteredOut = true;
					break;
				}
			}
			
		}
		return shouldBeFilteredOut;
	}
	
}
