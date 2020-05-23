//assignment 1 - programming part, version 1
//comp 352 summer 2020
//nicholas yiphoiyen - 40117237
//winkel yin - 40129707
//due on the 15th of may 2020

/*The time complexity or Big-O of this algorithm is O(n*n!). This is because there are n! permutations depending on the length
of the string provided and it requires n time to check if the short string is contained in the long string and to print the permutations.
Consequently, the time complexity is O(n*n!)

The solution doesn't really have a acceptable complexity, because a factorial number gets very big very quickly. For example, for the permutations of a short 
string of 10 characters, it takes 7.753s which this number just growing and growing with the length of the string provided. 

Because recursion is employed, if the short string provided is too big, the stack of one's computer could simply overflow, making this algorithm not very scalable. This is why 
a better algorithm is provided in version 2 to make it somewhat more scalable.*/

public class Main {

	//main method just to test method
    public static void main(String[] args) {
        permu("", "bakkd", "hhhlajkjgabckkkkcbakkdfjknbbca");
    }

    /**
     * 
     * @param permutation one by one, the letters are passed from the remaining parameter to generate the permutations
     * @param remaining the letters are send to the permutations parameter for recursion to work and for the permutations to be generated
     * @param longStr string that we want to check if the permutations is within it
     */
    public static void permu(String permutation, String remaining, String longStr) {
        //printing the permutation if all letters have been passed from remaining to permutations
    	if (remaining.equals("")) {
            System.out.println(permutation);
            
            //checking if the permutations is contained within the long string
            for (int i = 0; i < longStr.length() - permutation.length() + 1; i++) {
                if (permutation.equals(longStr.substring(i, i + permutation.length()))) {
                    System.out.println("Found one match: " + permutation + " is in " + longStr + " at location " + i);
                }
            }
            return;
        }
    	//recursion, swapping the letters to generate a different permutation
        for (int i = 0; i < remaining.length(); i++) {
            permu(permutation + remaining.charAt(i), remaining.substring(0, i) + remaining.substring(i + 1), longStr);
        }
    }
}
