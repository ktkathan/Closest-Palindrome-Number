package com.ktrivedi;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class Main {

    /**
     * Processes the given input string for closest palindrome
     *     
     * 1. inputArray = Convert the input string to character Array
     * 2. palindromeString = Convert the character Array into palindrome string
     * 3. prevPalindrome = Calculate the previous closest palindrome
     * 4. nextPalindrome = Calculate the next closest palindrome
     * 5. Find the absolute differences among all 
     * 6. Return the closest palindrome string
     *
     * @param sInputString - Input string from the TestCase.csv
     * @param bExpectedOutput - ExpectedOutput string from the TestCase.csv
     * @return - Returns the closest possible palindrome string
     */
    @org.junit.Test
    public static String sProcessStringForPalindrome(String sInputString, String bExpectedOutput) 
    {
        String sPalindromeOutput= "";
        try
        {
            // KT: Refer Assumptions-1
            // KT: If input string is empty, it is invalid input
            if(sInputString.isBlank() || sInputString == null || sInputString.isEmpty())
            {
                sPalindromeOutput = "Invalid String";
            }
            // KT: Refer Assumptions-2
            // KT: If length is shorter than one character, return the same string
            else if(sInputString.length()<=1)
            {
                sPalindromeOutput = sInputString;
            }
            else
            {
                char[] inputArray = sInputString.toCharArray();

                for (int i = 0, j = inputArray.length - 1; i < j; i++, j--)
                    inputArray[j] = inputArray[i];
    
                String palindromeString = String.valueOf(inputArray);
                String prevPalindrome = sGetClosestPalindrome(palindromeString, false);
                String nextPalindrome = sGetClosestPalindrome(palindromeString, true);

                long lngNum = Long.valueOf(sInputString);
                long lngCurrentPalindrome = Long.valueOf(palindromeString);
                long lngPreviousPalindrome = Long.valueOf(prevPalindrome);
                long lngNextPalindrome = Long.valueOf(nextPalindrome);

                long diff1 = Math.abs(lngNum - lngPreviousPalindrome);
                long diff2 = Math.abs(lngNum - lngCurrentPalindrome);
                long diff3 = Math.abs(lngNum - lngNextPalindrome);

                if (lngNum == lngCurrentPalindrome) {
                    sPalindromeOutput = diff1 <= diff3 ? prevPalindrome : nextPalindrome;
                } else if (lngNum > lngCurrentPalindrome) {
                    sPalindromeOutput = diff2 <= diff3 ? palindromeString : nextPalindrome;
                } else {
                    sPalindromeOutput = diff1 <= diff2 ? prevPalindrome : palindromeString;
                }
            }
            //KT: Check for test case, if fails raise assertion.
            assertEquals(bExpectedOutput, sPalindromeOutput);
        }
        catch (Exception e)
        {
            System.out.println("Unknown Error! Please contact administrator.");
        }
        catch (AssertionError ae)
        {
            System.out.println("Test case failed");
        }
        return sPalindromeOutput;
    }

    /**
     * Generates the next and previous closest palindrome strings
     * 
     * 1. mid = Find the middle index of sPalindromeString
     * 2. prev = Calculates the length of the left substring by subtracting the middle index
     * 3. l = Figure out the left substring based on prev
     * 4. l = Add 1 if calculating next palindrome, Subtract 1 if calculating previous palindrome
     * 5. Additional check if string is one character long then return 0 or 9
     * 6. leftPart = left half of the sPalindromeString
     * 7. rightPart = mirror of left half: return 9 if longer than left
     * 
     * @param sPalindromeString - Palindrome String
     * @param bGetNextPalindrome - Previous or Next
     * @return - returns the corresponding palindrome string
     */
    private static String sGetClosestPalindrome(String sPalindromeString, boolean bGetNextPalindrome) {
        int mid = sPalindromeString.length()/2;
        int prev = sPalindromeString.length() - mid;
        int l = Integer.valueOf(sPalindromeString.substring(0, prev));
        l += (bGetNextPalindrome ? 1 : -1);
        if (l == 0) 
            return mid == 0 ? "0" : "9";

        StringBuilder leftPart = new StringBuilder(String.valueOf(l));
        StringBuilder rightPart = new StringBuilder(leftPart).reverse();
        if (mid > leftPart.length()) 
            rightPart.append("9");
        return leftPart.append(rightPart.substring(rightPart.length() - mid)).toString();
    }

    /**
     * Read the TestCase.csv file for test cases
     * @return - Returns Hashmap created from TestCase.csv
     */
    public static HashMap<String,String> readCsvFile()
    {
        HashMap<String,String> inputTestCase = new HashMap<String,String>();
        String testCase = "TestCase.csv";
        File file = new File(testCase);
        try
        {
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            String line = "";
            int i=0;
            while ((line = csvReader.readLine()) != null)
            {
                if(i>0)
                {
                    var data = line.split(",");
                    inputTestCase.put(data[1], data[2]);
                }
                //KT: Skip the header values
                else { }
                i++;
            }
            csvReader.close();
            return inputTestCase;
        }
        catch (Exception e)
        {
            
        }
        return inputTestCase;
    }

    /**
     * Writes the list of test output in TestOutput-<Today's Date>.csv file
     * 
     * @param lOutput - Output List to be written in CSV file
     */
    public static void writeCSVFile(ArrayList<TestOutput> lOutput)
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String testOutputFileName = "TestOutput-"+dtf.format(LocalDateTime.now())+".csv";
        File file = new File(testOutputFileName);
        try {
            FileWriter csvWriter = new FileWriter(testOutputFileName);
            csvWriter.append("Test Case No.");
            csvWriter.append(",");
            csvWriter.append("Inputs");
            csvWriter.append(",");
            csvWriter.append("Expected Results");
            csvWriter.append(",");
            csvWriter.append("Actual Results");
            csvWriter.append(",");
            csvWriter.append("Status");
            csvWriter.append("\n");
            for(int i=0; i<lOutput.size();i++)
            {
                csvWriter.append(lOutput.get(i).testCaseNo);
                csvWriter.append(",");
                csvWriter.append(lOutput.get(i).inputCase);
                csvWriter.append(",");
                csvWriter.append(lOutput.get(i).expectedOutput);
                csvWriter.append(",");
                csvWriter.append(lOutput.get(i).actualOutput);
                csvWriter.append(",");
                csvWriter.append(lOutput.get(i).status);
                csvWriter.append("\n");
            }
            csvWriter.flush();
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) 
    {
        readCsvFile();
        try 
        {
            //KT: Read CSV file for inputs
            HashMap<String, String> testCases = readCsvFile();
            if(testCases.isEmpty() || testCases == null)
            { System.out.println("Invalid Test Cases!"); }
            else
            {
                int i=1;
                ArrayList<TestOutput> testOutputs = new ArrayList<>();
                for(Map.Entry<String,String> e : testCases.entrySet())
                {
                    //KT: Write output of the function to Model class object
                    TestOutput output = new TestOutput();
                    output.testCaseNo = String.valueOf(i);
                    output.inputCase = e.getKey();
                    output.expectedOutput = e.getValue();
                    System.out.println("Original String: "+e.getKey());
                    String sOutputPalindrome = sProcessStringForPalindrome(e.getKey(), e.getValue());
                    System.out.println("The shortest palindrome number is: "+ sOutputPalindrome);
                    output.actualOutput = sOutputPalindrome;
                    if(sOutputPalindrome.equals(e.getValue()))
                        output.status = "Pass";
                    else
                        output.status = "Fail";
                    //KT: Add objects in the list
                    testOutputs.add(output);
                    i++;
                }
                //KT: writes the list values to CSV file
                writeCSVFile(testOutputs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
