package domain;

public class BofBean {
	private int bof_id=0;
	private String bof_name=null;
	private int first_id=0;
	private int second_id=0;
	private int third_id=0;
	private String first_name;
	private String second_name;
	private String third_name;
	private String first_creator;
	private String second_creator;
	private String third_creator;
	
	public int getBofId()
	{
		return bof_id;
	}
	public String getBofName()
	{
		return bof_name;
	}
	public int getFirstId()
	{
		return first_id;
	}
	public int getSecondId()
	{
		return second_id;
	}
	public int getThirdId()
	{
		return third_id;
	}
	public void setBofId(int bof_id)
	{
		this.bof_id=bof_id;
	}
	public void setBofName(String bof_name)
	{
		this.bof_name=bof_name;
	}
	
	public void setFirstId(int first_id)
	{
		this.first_id=first_id;
	}
	public void setSecondId(int second_id)
	{
		this.second_id=second_id;
	}
	public void setThirdId(int third_id)
	{
		this.third_id=third_id;
	}
	public void setFirstName(String first_name)
	{
		this.first_name=first_name;
	}
	public void setSecondName(String second_name)
	{
		this.second_name=second_name;
	}
	public void setThirdName(String third_name)
	{
		this.third_name=third_name;
	}
	public void setFirstCreator(String first_creator)
	{
		this.first_creator=first_creator;
	}
	public void setSecondCreator(String second_creator)
	{
		this.second_creator=second_creator;
	}
	public void setThirdCreator(String third_creator)
	{
		this.third_creator=third_creator;
	}
	
}
