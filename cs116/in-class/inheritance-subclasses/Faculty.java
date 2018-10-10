package subclass;
import superclass.CommunityMember;
import java.util.Vector;
public class Faculty extends CommunityMember{
    private String academicDept;
	private Vector<String> courses;
	

	public Faculty(String dept) {
		super("no-name", 0, 0);
  		this.academicDept="academic";
		this.courses=new Vector<String>();//empty list of courses
    }

	//default constructor
	public Faculty(){
		this("academic");		
	}
	public Faculty(String name, int age, float salary) {
		super(name, age, salary);
		super.ID = "F" + super.ID;
	}
	
	//accessor methods
	public String getAcademicDept(){
		return this.academicDept;
	}
	
	public Vector getCourses(){
		return this.courses;
	}
	
	//mutator methods
	
	public void setAcademicDept(String dept){
		this.academicDept=dept;
	}
	public void setCourses(Vector<String> courses){
		this.courses=courses;
	}
	
	//other methods 
	public String listCourses(){
		String retString="";
		Vector courses = this.getCourses();
		if(courses==null){
			retString="No courses taught\n";
			
		}else if(courses.size()==0){
			retString="No courses taught\n";
		}else{			
			for(int i=0;i<courses.size();++i){
				retString = retString + courses.get(i) + "\n";
			}
		}
		
		return retString;
	}
	
}
