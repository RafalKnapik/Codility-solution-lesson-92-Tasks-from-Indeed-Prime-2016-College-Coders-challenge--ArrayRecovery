import java.util.ArrayList;
import java.util.List;

public class Solution {
    /*counts number of correct arrays, running time is O(n^2)*/
    public static int solution(int[] A, int M) {
        int count = 1;
        for (int i = 0; i < A.length; i++) {
            if (i != A.length - 1 && A[i] < A[i + 1]) {
                continue;
            }
            if (i > 0 && A[i - 1] == A[i]) {
                continue;
            }
            if (i != A.length - 1 && A[i] == A[i + 1]) {
                count = count * countForEqualElements(A, i, M);
                continue;
            }
            if (i > 0 && A[i - 1] > A[i]) {
                count = count * (min(A, i) - A[i]);
                continue;
            }
            if (i > 0 && A[i - 1] < A[i]) {
                count = count * (M - A[i]);
            }
        }
        return count;
    }
    /*counts number of decreasing sequences between two integers */
    public static int countNumberOfDecreasingSequences(int sequenceLength, int first, int last) {
//        int numberOfPossibilities = last - first + 1;
//        int count = 0;
//        if (sequenceLength > 1) {
//            for (int i = 0; i < numberOfPossibilities; i++) {
//                count = count + countNumberOfDecreasingSequences(sequenceLength - 1, first, last - i);
//            }
//        } else if (sequenceLength == 1) {
//            count = numberOfPossibilities;
//        }
//        return count;
        int N = last - first + sequenceLength;
        return factorial(N)/(factorial(sequenceLength) * factorial(N - sequenceLength));
    }

    private static int factorial(int n) {
        if(n == 0) {
            return 1;
        }
        else {
           return  n * factorial(n - 1);
        }
    }

    /*gives minimal A[i] grater than A[index] for i such that j < i < index, where j is firs index in array, that A[j] = A[index]*/
    private static int min(int[] A, int index) {
        int min = A[index - 1];
        for (int i = index - 1; A[i] > A[index]; i--) {
            if (A[i] < min) {
                min = A[i];
            }
        }
        return min;
    }
    /*counts number of possible pieces of array, when A contains sequence of equal elements*/
    public static int countForEqualElements(int[] A, int index, int M) {

        if (index > 0 && A[index - 1] > A[index]) {
            M = min(A, index);
        }

        int sequenceLength = 0;

        for (int i = index; i < A.length; i++) {
            if (i == A.length - 1 || A[i + 1] < A[i]) {
                sequenceLength++;
                return countNumberOfDecreasingSequences(sequenceLength, A[i] + 1, M);
            } else if (A[i] == A[i + 1]) {
                sequenceLength++;
            } else {
                return countNumberOfDecreasingSequences(sequenceLength, A[i + 1], M);
            }
        }
        return 0;
    }
    /*gives an array of all correct arrays*/
    public static ArrayList<ArrayList<Integer>> solution_2(int[] A, int M) {
        ArrayList<ArrayList<ArrayList<Integer>>> B = new ArrayList<ArrayList<ArrayList<Integer>>>();

        for (int i = 0; i < A.length; i++) {
            if (i != A.length - 1 && A[i] < A[i + 1]) {
                ArrayList<ArrayList<Integer>> X = new ArrayList<ArrayList<Integer>>(1);
                ArrayList<Integer> Y = new ArrayList<Integer>();
                Y.add(A[i + 1]);
                X.add(Y);
                B.add(X);
                continue;
            }
            if (i != 0 && A[i - 1] == A[i]) {
                continue;
            }
            if (i != A.length - 1 && A[i] == A[i + 1]) {
                B.add(generateSequencesForEqualElements(A, i, M));
                continue;
            }
            if (i > 0 && A[i - 1] > A[i]) {
                ArrayList<ArrayList<Integer>> X = new ArrayList<ArrayList<Integer>>();
                for (int j = A[i] + 1; j <= min(A, i); j++) {
                    ArrayList<Integer> Y = new ArrayList<Integer>();
                    Y.add(j);
                    X.add(Y);
                }
                B.add(X);
                continue;
            }
            if (i > 0 && A[i - 1] < A[i]) {
                ArrayList<ArrayList<Integer>> X = new ArrayList<ArrayList<Integer>>();
                for (int j = A[i] + 1; j <= M; j++) {
                    ArrayList<Integer> Y = new ArrayList<Integer>();
                    Y.add(j);
                    X.add(Y);
                }
                B.add(X);
            }
        }
        return generateArrayOfSolutions(B);
    }

    public static ArrayList<ArrayList<Integer>> generateSequencesForEqualElements(int[] A, int index, int M) {
        if (index != 0 && A[index - 1] > A[index]) {
            M = min(A, index);
        }

        int sequenceLength = 0;

        for (int i = index; i < A.length; i++) {
            if (i == A.length - 1 || A[i + 1] < A[i]) {
                sequenceLength++;
                return generateDecreasingSequences(sequenceLength, A[i] + 1, M);
            } else if (A[i] == A[i + 1]) {
                sequenceLength++;
            } else {
                return generateDecreasingSequences(sequenceLength, A[i + 1], M);
            }
        }
        return new ArrayList<ArrayList<Integer>>();
    }

    private static ArrayList<ArrayList<Integer>> generateDecreasingSequences(int sequenceLength, int first, int last) {
        ArrayList<ArrayList<Integer>> sequences = new ArrayList<ArrayList<Integer>>();
        if (sequenceLength > 1) {
            for (int i = first; i <= last; i++) {
                for (List<Integer> x : generateDecreasingSequences(sequenceLength - 1, first, i)) {
                    ArrayList<Integer> B = new ArrayList<Integer>();
                    B.add(i);
                    B.addAll(x);
                    sequences.add(B);
                }
            }
        } else if (sequenceLength == 1) {
            for (int i = first; i <= last; i++) {
                ArrayList<Integer> B = new ArrayList<Integer>();
                B.add(i);
                sequences.add(B);
            }
        }
        return sequences;
    }

    public static ArrayList<ArrayList<Integer>> generateArrayOfSolutions(ArrayList<ArrayList<ArrayList<Integer>>> A) {
        ArrayList<ArrayList<Integer>> arrayOfSolutions = new ArrayList<ArrayList<Integer>>();
        if (A.size() == 1) {
            arrayOfSolutions = A.get(0);
        } else {
            for (ArrayList<Integer> B : A.get(0)) {
                ArrayList<ArrayList<ArrayList<Integer>>> X = new ArrayList<ArrayList<ArrayList<Integer>>>(A);
                X.remove(0);
                for (ArrayList<Integer> Y : generateArrayOfSolutions(X)) {
                    ArrayList<Integer> Z = new ArrayList<Integer>(B);
                    Z.addAll(Y);
                    arrayOfSolutions.add(Z);
                }
            }

        }
        return arrayOfSolutions;
    }



    public static void main(String[] args) {
        int[] A = {0, 1, 3, 8, 3, 1, 2, 0, 1, 2, 2};
        System.out.println(solution(A, 9));
        System.out.println(solution_2(A, 9).size());
    }


}
