public interface BarberFunctions {

	public void addAppointment(long phone,String n,String d,String t, String s, int p) throws Exception;
	
	public void generateSchedule() throws Exception; // complete
	
	public void generateRevenueReport() throws Exception;
	
	public void deleteAppointment(long n) throws Exception;
	
}
