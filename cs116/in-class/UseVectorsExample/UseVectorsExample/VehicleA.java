public class VehicleA 
{
	private String vehicle_name;
	private double velocity;
	private double acceleration;
	private double distance;
	private int time;
	private static int vehicleID;
	private int currentID;
	private double init_v;
	private double init_d;
	private VehicleType vt;
    
	public VehicleA()
	{
		vehicle_name=" ";
		velocity=0.0;
		acceleration=0.0;
		time=0;
		distance=0.0;
		currentID=0;
		init_v=0.0;
		init_d=0.0;
	}

	public VehicleA(String name, double a, int t, double initv, double initd)
	{
		vehicleID++;
		currentID=vehicleID;
		vehicle_name=name;
		acceleration=a;
		time=t;
		init_v=initv;
		init_d=initd;
	
	}

	public String getVehicleName()
	{
		return vehicle_name;
	}

	public double getVelocity()
	{
		return velocity;
	}

	public double getAcceleration()
	{

		return acceleration;
	}

	public double getDistance()
	{
		return distance;
	}

	public int getTime()
	{
		return time;
	}

	public int getVehicleID()
	{
		return vehicleID;
	}

	public int getCurrentID()
	{

		return currentID;
	}

	public double getInitV()
	{
		return init_v;
	}

	public double getInitD()
	{

		return init_d;
	}

	public void setInitV(double inv)
	{
		init_v=inv;
	}

	public void setInitD(double ind)
	{

		init_d=ind;
	}

	public void setTime(int t)
	{

		time=t;
	}

	public void setAcceleration(double acc)
	{
			acceleration=acc;

	}

	public void setDistance(double dist)
	{

		distance=dist;
	}

	public String toString()
	{

		String str="The vehicle ID is:"+" "+currentID+" "+"The name of the vehicle is:"+" "+vehicle_name+" "+
			"The initial velocity is"+" "+init_v+" "+"The initial distance is"+" "+init_d+" "+"The final velocity is:"+" "+velocity+"m/sec"+" "+"The final distance is:"+" "+distance+"m";
		return str;
	}

	public void calculateV()
	{
		
		this.velocity=acceleration*time+init_v;
	}

	public void calculateD()
	{
		
		double timed=(double)time;
		double t=Math.pow(timed, 2.0);
		this.distance=0.5*acceleration*t+init_v*time+init_d;
	}

	public String licensePlate()
	{

		String substr=vehicle_name.substring(3);
		String strid=String.valueOf(currentID);
		String substr1=substr+strid;
		return substr1;

	}

    public void setVehicleType(VehicleType vehty)
	{
		this.vt=vehty;
	}

	public VehicleType getVehicleType()
	{
		 return vt;
	}

	public boolean equals(Object o)
	{
		// if o is not an Auto object, return false
		if ( ! ( o instanceof VehicleA ) )
			return false;
		else
		{
			// type cast o to an Auto object
			VehicleA objVeh = ( VehicleA) o; 
			if (this.init_v==objVeh.init_v&&this.init_d==objVeh.init_d&&this.vt.equals(objVeh.vt)&&this.time==objVeh.time)
					return true;
			else
					return false;
		}
	}

    public void setCalcutedTime(double dist, double acc,double iv)
	{

			int t=0;
			double d=dist;
			double a=acc;
			double inve=iv;
			t=(int)(((Math.pow(inve,2)+Math.sqrt(Math.pow(inve,2)+2*a*d-2*a*init_d))/a));
			time=t;

	}

	
}
