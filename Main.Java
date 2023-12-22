import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

class Main {


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean play;

        do {
            // ask if they want 6 colors or 8,,
            System.out.println("How many colors do you want to play with? the max is 10, and min is 2");

            int end = sc.nextInt();
            while (end > 10 || 2 > end) {
                if (end > 10) {
                    System.out.println("You entered a wrong value. The MAXIMUM is 10. Try again?");
                } else {
                    System.out.println("You cannot play wth less than 2 Try again?");
                }
                end = sc.nextInt();

            }


            // *INSERT INSTRUCTIONS*

            char[] colors = howmanycolors(0, end);


            System.out.println("How many rounds do you want to be the limit?\nMax is 35, Min is 1");

            int maxRound = sc.nextInt();
            while (maxRound > 35 || 1 > maxRound) {
                if (maxRound > 35) {
                    System.out.println("You entered a wrong value. The MAXIMUM is 35. Try again?");
                } else {
                    System.out.println("You cannot play wth less than 1 round, that's not how time works. Try again?");
                }
                maxRound = sc.nextInt();
            }

            final int maxsize = 4;
            int roundused = 0;

            //generate secret code
            String code = generateSecretCode(colors);

            System.out.println("The computer will generate a 4 character code based on colours. \nThe Possible Colors are:" + Arrays.toString(colors) + "\nYour job is to guess the code. \nThe computer will tell you two pieces of information:\n1- An 'X' for each proper position AND colour.\n2- An 'O' for each correct colour only.\nYou can Press I at any time (after this message) to print instructions, and q to quit.\nYou have: " + maxRound + " rounds.\nGOOD LUCK :D");
            do {
                //for testing:-System.out.println(code);
                String userans = takecodeUser(maxsize, colors);

                //checks if code is same. if not, gives feedback with pegs
                if (userans.equals(code)) {
                    System.out.println("Congrats! you win!");
                    roundused = 13;

                } else {
                    int[] feedback = feedbackguess(code, userans, maxsize);
                    System.out.println("You have " + feedback[0] + " 'X' pegs and " + feedback[1] + " 'O' pegs.");
                    roundused++;
                    System.out.println("You used " + roundused + " out of " + maxRound + " rounds.\n");
                }

            } while (maxRound > roundused);

            if (maxRound == roundused) {
                System.out.println("\nRounds exceeded! The code was: " + (code));
            }

            System.out.println("\nDo you want to play again?\nThe possible answers are 'Yes' and 'No'");

            char playagai = sc.next().toUpperCase().charAt(0);
            play = playagai == 'Y';
        } while (play);
    }


    //make secret code
    public static String generateSecretCode(char[] c) {
        Random generator = new Random();
        StringBuilder result = new StringBuilder();
        int index, numberOfPegs = 4;
        for (int i = 0; i < numberOfPegs; i++) {
            index = generator.nextInt(c.length);
            result.append(c[index]);

        }
        return result.toString();

    }

    //how many colors method
    public static char[] howmanycolors(int start, int end) {

        char[] colors2 = {'B', 'G', 'O', 'P', 'R', 'Y', 'V', 'L', 'C', 'W'};
        char[] slice = new char[end - start];


        if (slice.length >= 0) System.arraycopy(colors2, start, slice, 0, slice.length);


        return slice;
    }

    // CHECKS IF USER WANTS instructions or to quit
    public static String checkinstr(String ans, char[] colors) {

        while (ans.equals("I") || ans.equals("Q")) {
            if (ans.equals("Q")) {
                System.exit(1);
            } else {
                Scanner sc = new Scanner(System.in);
                System.out.println("Here are the Instructions:\nThe computer will generate a 4 character code based on colours. \nThe Possible Colors are" + Arrays.toString(colors) + ".\nYour job is to guess the code.");
                System.out.println("Insert your guess:");
                ans = sc.next().toUpperCase();
            }
        }
        return ans;
    }

    /* takes the user code+ checks if it is the correct length and if it is the correct values */

    public static String takecodeUser(int maxsize, char[] colors) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Insert your guess");
        String ans = checkinstr(sc.next().toUpperCase(), colors);

        int ansL = ans.length();

        while (ansL != maxsize) {
            System.out.println("The length of your input is wrong, enter 4 digits.");
            ans = checkinstr(sc.next().toUpperCase(), colors);
            ansL = ans.length();
        }

        for (int i = 0; i < ansL; i++)
            while (!isValid(ans.charAt(i), colors)) {
                System.out.println("You entered an invalid color, The correct colors are" + Arrays.toString(colors) + ".\nTry again?");
                ans = checkinstr(sc.next().toUpperCase(), colors);
                //System.out.println(ans);
                ansL = ans.length();
            }
        return ans;
    }


    //checks for correct colors
    private static boolean isValid(char letter, char[] colors) {
        boolean valid = false;
        for (char color : colors) {
            if (letter == color) {
                valid = true;
                break;
            }


        }
        return valid;
    }


    //gives feedback
    public static int[] feedbackguess(String code, String userans, int maxsize) {

        int[] pegs = new int[2];
        String remainder;
        String coderemainder;
        int xPegs = 0;
//initializing string builder for remainder
        StringBuilder coderemainderBuilder = new StringBuilder();
        StringBuilder remainderBuilder = new StringBuilder();
        //checks for x pegs, sees if any letters are same location
        for (int i = 0; i < maxsize; i++) {
            char letter = userans.charAt(i);
            char codeLetter = code.charAt(i);
            if (letter == codeLetter) {
                xPegs++;
                pegs[0] = xPegs;
            } else {
                //if not, puts them in a variable for remainder
                remainderBuilder.append(letter);
                coderemainderBuilder.append(codeLetter);
            }
        }
        //puts them back into string
        remainder = remainderBuilder.toString();
        coderemainder = coderemainderBuilder.toString();

        //check each char to see if any are similar and if yes, then replace all of that char (so you dont have duplicates).
        int remainderlen = remainder.length();
        int coderemainlen = coderemainder.length();
        int oPegs = 0;
        for (int j = 0; j < coderemainlen; j++) {

            char ocodeLetter = coderemainder.charAt(j);
            for (int m = 0; m < remainderlen; m++) {
                char oletter = remainder.charAt(m);
                if (ocodeLetter == oletter) {

                    oPegs++;
                    pegs[1] = oPegs;
                    remainder = userans.replaceAll(String.valueOf(oletter), "Z");
                }

            }

        }

        return pegs;
    }
}





