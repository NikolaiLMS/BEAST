package edu.pse.beast.toolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueArray;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueSingle;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueWrapper;

public class CBMCResultPresentationHelper {
	
	private static String getWhiteSpaces(int amount) {
		char[] spaces = new char[amount];
		Arrays.fill(spaces, ' ');
		
		return new String(spaces);
	}
	
	public static String printSingleElement(CBMCResultValueSingle single, int offset) {
		return getWhiteSpaces(offset) + single.getValue() + "\n";
	}

	public static String printOneDimResult(CBMCResultValueArray array, int offset) {
		
		String toReturn = getWhiteSpaces(offset);

		List<CBMCResultValueWrapper> arrayValues = array.getValues();

		for (int i = 0; i < arrayValues.size(); i++) {
			CBMCResultValueSingle singleValue = (CBMCResultValueSingle) arrayValues.get(i).getResultValue();

			toReturn = toReturn + singleValue.getValue() + " ";
		}

		return toReturn + "\n";
	}

	public static List<String> printTwoDimResult(CBMCResultValueArray array, int offset) {

		List<String> toReturn = new ArrayList<String>();
		
		List<CBMCResultValueWrapper> arrayValues = array.getValues();

		for (int i = 0; i < arrayValues.size(); i++) {
			CBMCResultValueArray current_array = (CBMCResultValueArray) arrayValues.get(i).getResultValue();
			
			toReturn.add(printOneDimResult(current_array, offset));
		}
		
		return toReturn;
	}
}