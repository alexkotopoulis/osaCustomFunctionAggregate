package com.oracle.osacs;

import com.oracle.cep.api.annotations.OsaFunction;

interface AggregateOperation {
	double aggregate(int count, double prevAgg, double newVal);
}

public class CustomFunctionAggregate {
	
	@OsaFunction(name = "sumInList", description = "Calculates sum from elements in delimited list string. List can have groups of tuples where one element each has the relevant value")
	/**
	 * Calculates sum from elements in delimited list string. List can have groups of tuples where one element each has the relevant value
	 * @param input Delimited string
	 * @param delimiter Delimiter, used for both delimiting tuples and tuple elements (Flat list)
	 * @param tupleSize Amount of elements per tuple
	 * @param position Position of relevant element in tuple (0 to n-1)
	 * @return Sum of all relevant elements in list
	 */
    public static double sumInList(String input, String delimiter, int tupleSize, int position ) {
		return aggInList (input, delimiter, tupleSize, position, (count, prevAgg, newVal) -> prevAgg + newVal);
	
	}
	
	@OsaFunction(name = "avgInList", description = "Calculates average from elements in delimited list string. List can have groups of tuples where one element each has the relevant value")
	/**
	 * Calculates average from elements in delimited list string. List can have groups of tuples where one element each has the relevant value
	 * @param input Delimited string
	 * @param delimiter Delimiter, used for both delimiting tuples and tuple elements (Flat list)
	 * @param tupleSize Amount of elements per tuple
	 * @param position Position of relevant element in tuple (0 to n-1)
	 * @return Average of all relevant elements in list
	 */
    public static double avgInList(String input, String delimiter, int tupleSize, int position ) {
		return aggInList (input, delimiter, tupleSize, position, (count, prevAgg, newVal) -> (prevAgg*(count-1) + newVal)/count);
	
	}

	/**
	 * Calculates aggregate from elements in delimited list string. List can have groups of tuples where one element each has the relevant value
	 * @param input Delimited string
	 * @param delimiter Delimiter, used for both delimiting tuples and tuple elements (Flat list)
	 * @param tupleSize Amount of elements per tuple
	 * @param position Position of relevant element in tuple (0 to n-1)
	 * @param op Lambda function performing aggregation
	 * @return Aggregate of all relevant elements in list
	 */	
	private static double aggInList(String input, String delimiter, int tupleSize, int position, AggregateOperation op ) {
		double result= 0.0f;
		if (null != input) {
			String[] splitStr = input.split(delimiter);
			int posInTuple=0;
			int countTuples=0;
			
			for (String valStr:splitStr) {
		        if (posInTuple==position) {
		        	countTuples++;
		        	double val=Double.parseDouble(valStr);
		        	result = op.aggregate(countTuples,result,val);
		        }
		        posInTuple++;
		        if (posInTuple==tupleSize)
		        	posInTuple=0;
		            
			}
			
		}
		return result;
	}

	/**
	 * Test main function
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(sumInList("1,2,3,4",",",1,0));
		System.out.println(sumInList("a~4.0~b~2.0~c~3.0~d~4.0","~",2,1));
		System.out.println(avgInList("a:4.0:1:b:2.0:0:c:3.0:1:d:4.0:1",":",3,1));
	}

}
