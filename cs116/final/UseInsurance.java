import java.util.*;
import java.io.*;


class UseInsurance{
	public static void main(String []args){
	//arg[0]: text file  containing the policy information

        List<InsuranceGroup> groups = new ArrayList<InsuranceGroup>();
        InsuranceGroup group = new InsuranceGroup();


		Scanner scan;
		String str;
		try {

			File myFile=new File(args[0]);
            scan=new Scanner(myFile);//each line has the format
			//name;age;policy-type{Health or Life}




			while(scan.hasNextLine()){
				str=scan.nextLine();
				String []tok=str.split(";");
				String name=tok[0];//name of the policy holder
				int age = Integer.parseInt(tok[1]);//age of the policy holder
				String type = tok[2];//type of the insurance values are "Health" or "Life"

                Insurance policy;
				if (type.compareTo("Health") == 0)
                    policy = new HealthInsurance(age, name);
				else
                    policy = new LifeInsurance(age, name);

                if (!group.canAdd()) {
                    groups.add(group);
                    group = new InsuranceGroup();
                    group.addPolicy(policy);
                } else {
                    group.addPolicy(policy);
                }
			}

			scan.close();
        }catch(IOException ioe){
			System.out.println("The file can not be read");
		}

        groups.add(group);

        for (InsuranceGroup ig : groups)
            System.out.println(ig);
	}
}
