public class FindMax {
	public int getMax(int []nums){
        if (nums.length == 1)
            return nums[0];

        int [] remaining = new int[nums.length - 1];
        for (int i = 1; i < nums.length; i++)
            remaining[i - 1] = nums[i];


        // NOTE: recursion!
        final int lh = nums[0], rh = getMax(remaining);
        return lh > rh ? lh : rh;

	}
	public static void main(String []args){
		int []a = { -1, 44, 113, 2, 5, 23, 98 };
		FindMax max = new FindMax();
		System.out.println("Max no. is " + max.getMax(a));
	}
};
