package student;

public class Student {

	protected String m_firstName, m_lastName;
	protected int m_age;

	void setName(String firstName, String lastName){
		if (firstName != null)
			m_firstName = firstName;
		if (lastName != null)
			m_lastName = lastName;
	}

	public String getFirstName()
		{ return m_firstName; }
	public String getLastName()
		{ return m_lastName; }

	Student(int age)
		{ m_age = age; }
};
