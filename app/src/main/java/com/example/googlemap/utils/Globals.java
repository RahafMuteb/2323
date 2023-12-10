package com.example.googlemap.utils;

import com.example.googlemap.models.Locker;
import com.example.googlemap.models.Model;
import com.example.googlemap.models.Vistors;

public class Globals
{

    public static Model currentPost;
    public static Locker selectedLocker;

    public static Vistors user;


    public static String booleanArrayToString(boolean[] array) {
        StringBuilder result = new StringBuilder();

        for (boolean value : array) {
            // Append '0' for false, '1' for true, and ',' as a separator
            result.append(value ? '1' : '0').append(',');
        }

        // Remove the trailing comma if the result is not empty
        if (result.length() > 0) {
            result.setLength(result.length() - 1);
        }

        return result.toString();
    }

    public static String convertToBinary(String input)
    {
        StringBuilder binaryStringBuilder = new StringBuilder();

        for (char character : input.toCharArray())
        {
            // Convert each character to its binary representation
            String binary = Integer.toBinaryString(character);

            // Ensure that each binary representation has 8 bits (pad with leading zeros if needed)
            binary = String.format("%8s", binary).replace(' ', '0');

            // Append each digit and a comma to the result
            for (char digit : binary.toCharArray())
            {
                binaryStringBuilder.append(digit).append(",");
            }
        }

        // Remove the trailing comma
        if (binaryStringBuilder.length() > 0)
        {
            binaryStringBuilder.setLength(binaryStringBuilder.length() - 1);
        }

        return binaryStringBuilder.toString();
    }

}
