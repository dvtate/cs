

// too lazy to actually do this assignment...	

class threeDimPrac{
	int grade;
	public threeDimPrac(int i, int j, int k)//notice the constructor takes in the dim1, dim 2 and dim 3 index values
	{
		this.grade=i*j*k;
	}
	public String toString(){
		String ret="";
		ret=ret+this.grade;
		return ret;
	}
	public static void main(String [] args){

	}
}
