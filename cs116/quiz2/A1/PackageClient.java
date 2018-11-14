import java.io.*;
import java.util.*;


public class PackageClient {


	public static void main(String []args){
		//read the package information from the file
		//file format is
		//from-address:to-address:wt:package-type
		//package-type=1 (two day) 2(overnight)


		List<Package> packages = new ArrayList<Package>();

		String line=new String();
		try{
			//read the file and create the objects
			File textFile = new File("package.txt");
			Scanner scan = new Scanner(textFile);
			while(scan.hasNextLine())
			{
				line=scan.nextLine();
				String []tok=line.split(":");
				String fromAddress=tok[0];
				String toAddress=tok[1];
				float wt=Float.parseFloat(tok[2]);
				int type=Integer.parseInt(tok[3]);

				Package pkg;
				if (type == 1)
					pkg = new TwoDayPackage(fromAddress, toAddress, wt);
				else
					pkg = new OverNightPackage(fromAddress, toAddress, wt);

				packages.add(pkg);

			}

		}catch(IOException e){
			System.out.println("Issuse with reading the file. Aborting");
			System.out.println(e.getMessage());
		}


		for (Package pkg : packages)
			System.out.println("====\n" + pkg.printRecipt() + "====\n");
	}
};
