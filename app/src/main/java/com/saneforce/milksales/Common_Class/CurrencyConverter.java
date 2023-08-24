package com.saneforce.milksales.Common_Class;

public class CurrencyConverter {

    private static final String[] ones = {
            "", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine",
            "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"
    };

    private static final String[] tens = {
            "", "", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"
    };

    private static final String[] thousands = {
            "", "Thousand", "Lakh", "Crore"
    };

    public static String convertToIndianWords(long number) {
        if (number == 0) {
            return "Zero";
        }

        int groupIndex = 0;
        String words = "";

        do {
            long currentGroup = number % 1000;
            if (currentGroup != 0) {
                words = convertGroupToWords(currentGroup) + " " + thousands[groupIndex] + " " + words;
            }

            number /= 1000;
            groupIndex++;
        } while (number > 0);

        return words.trim();
    }

    private static String convertGroupToWords(long group) {
        StringBuilder groupWords = new StringBuilder();

        int hundreds = (int) (group / 100);
        int remaining = (int) (group % 100);

        if (hundreds > 0) {
            groupWords.append(ones[hundreds]).append(" Hundred");
        }

        if (remaining > 0) {
            if (hundreds > 0) {
                groupWords.append(" and ");
            }

            if (remaining < 20) {
                groupWords.append(ones[remaining]);
            } else {
                int tensDigit = remaining / 10;
                int onesDigit = remaining % 10;
                groupWords.append(tens[tensDigit]).append(" ").append(ones[onesDigit]);
            }
        }

        return groupWords.toString();
    }

    public static String convertAmountToWords(double amount) {
        if (amount < 0 || amount > 1000000000) {
            throw new IllegalArgumentException("Amount must be between 0 and 100 crores.");
        }

        long integerPart = (long) amount;
        int decimalPart = (int) Math.round((amount - integerPart) * 100); // Rounding to nearest paise

        String integerPartWords = convertToIndianWords(integerPart);
        String decimalPartWords = convertToIndianWords(decimalPart);

        String result = integerPartWords + " rupees";

        if (decimalPart > 0) {
            result += " and " + decimalPartWords + " paise";
        }

        return result;
    }

    public static String convert(double amount) {
        return "Amount in words: " + convertAmountToWords(amount);
    }
}