package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import domain.BofBean;
import domain.GameBean;
import java.text.SimpleDateFormat;

public class DBUtils {
 private Connection conn;
 private String url = "jdbc:mysql://localhost/bof_database?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&allowMultiQueries=true"; // 鎸囧畾杩炴帴鏁版嵁搴撶殑URL
 private String user = "root"; // 用户名
 private String password = "GWRAzure20010519"; // 用户密码
 private Statement sta;
 private ResultSet res; // 结果集

//基础连接
 public void openConnect() 
 {
	 try {
		 // 鍔犺浇鏁版嵁搴撻┍鍔�
		 Class.forName("com.mysql.cj.jdbc.Driver");
		 conn = DriverManager.getConnection(url,user,password);// 进行数据库连接
		 if (conn != null) {
			 System.out.println("连接成功！"); // 连接成功
		 }
		 else
		 {
			 System.out.println("数据库错误！");
		 }
		 } catch (ClassNotFoundException e) {
		     e.printStackTrace();
		     System.out.println("数据库错误！");
		 } catch (SQLException e) {
		     e.printStackTrace();
		     System.out.println("数据库错误！");
		 }
 }
 

 // 关闭数据库连接
 public void closeConnect() {
 try {
	 if (res != null) 
	 {
		 res.close();
	 }
	 if (sta != null) 
	 {
		 sta.close();
	 }
	 if (conn != null) 
	 {
		 conn.close();
	 }
	 System.out.println("关闭数据库连接成功");
	 } catch (SQLException e) 
 	 {
	 System.out.println("Error: " + e.getMessage());
	 }
 }
 //------------------Register---------------
 //检查注册的时候是否已经有用户注册相同昵称
 public boolean checkNicknameExists(String user_nickname)
 {
	 boolean flag=false;
	 try 
	 {
		 System.out.println("判断用户昵称是否已经存在");
		 sta = conn.createStatement(); // 执行SQL查询语句
		 res = sta.executeQuery("select * from user_info ");// 获得结果集
		 if (res != null) 
		 {
			 while (res.next()) 
			 { // 遍历结果集
				 if (res.getString("user_nickname").equals(user_nickname)) 
				 {
					 System.out.println("该昵称已存在");
				 flag= true;
				 break;
				 }
			 }
		 }
	  } catch (SQLException e) 
	 {
		 e.printStackTrace();
		 flag= false;
	 }
		 return flag;
 }
 
 public boolean insertRegisterUser(String user_nickname, String user_password)
 {
	 String sql = " insert into user_info ( user_nickname , user_password ) values ( " + "'" + user_nickname + "', " + "'" + user_password
 + "' )";
	 try 
	 {
		 sta = conn.createStatement();
		 // 执行SQL查询语句
		 return sta.execute(sql);
	 } 
	 catch (SQLException e)
	 {
		 e.printStackTrace();
	 }
	 return false;
 }
 
 public int getUserId(String user_nickname)
 {
	 int id;
	 id=-1;
	 String sql="select * from user_info where user_nickname='"+user_nickname+"'";
	 System.out.println(sql);
	 try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				if(user_nickname.equals(res.getString("user_nickname")))
					id=res.getInt("user_id");
				break;
			}
		}
	 } 
	 catch (SQLException e)
	 {
		// 
		e.printStackTrace();
	 }
	 return id;
 }
 
 public String getUserSex(String user_nickname)
 {
	 String sex="";
	 String sql="select * from user_info where user_nickname='"+user_nickname+"'";
	 System.out.println(sql);
	 try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				if(user_nickname.equals(res.getString("user_nickname")))
					sex=res.getString("user_sex");
				break;
			}
		}
	 } 
	 catch (SQLException e)
	 {
		// 
		e.printStackTrace();
	 }
	 return sex;
 }
 
 public String getUserBirthday(String user_nickname)
 {
	 String birthday="";
	 String sql="select * from user_info where user_nickname='"+user_nickname+"'";
	 System.out.println(sql);
	 try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				if(user_nickname.equals(res.getString("user_nickname")))
					birthday=res.getString("user_birthday");
				break;
			}
		}
	 } 
	 catch (SQLException e)
	 {
		// 
		e.printStackTrace();
	 }
	 return birthday;
 }
 
 
//---------------Login------------------
 public boolean checkPassword(String user_nickname,String user_password)
 {
	 String sql="select user_password from user_info where user_nickname='"+user_nickname+"'";
	 System.out.println(sql);
	 String temp_password="";
	 try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				temp_password=res.getString("user_password");
				System.out.println(temp_password);
				System.out.println(user_password);
				break;
			}
		}
		
		if(temp_password.equals(user_password))
			return true;
	 } 
	 catch (SQLException e)
	 {
		e.printStackTrace();
	 }
	 return false;
 }
 
 public boolean checkAdminPassword(String admin_id,String admin_password)
 {
	 String sql="select admin_password from admin_info where admin_id='"+admin_id+"'";
	 System.out.println(sql);
	 String temp_password="";
	 try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				temp_password=res.getString("admin_password");
				System.out.println(temp_password);
				System.out.println(admin_password);
				break;
			}
		}
		
		if(temp_password.equals(admin_password))
			return true;
	 } 
	 catch (SQLException e)
	 {
		e.printStackTrace();
	 }
	 return false;
 }
 

//----------------------BOF--------------
public BofBean getBofBean(String bof_name)
{
	String sql="select * from bof where bof_name='"+bof_name+"'";
	BofBean bof =new BofBean();
	System.out.println(sql);
	try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				bof.setBofName(bof_name);
				bof.setFirstId(res.getInt("first_id"));
				bof.setSecondId(res.getInt("second_id"));
				bof.setThirdId(res.getInt("third_id"));
				bof.setBofId(res.getInt("bof_id"));
				break;
			}
		}
	 } 
	 catch (SQLException e)
	 {
		e.printStackTrace();
	 }
	return bof;
}
public BofBean getMusicInfo(int first_id,int second_id,int third_id,BofBean bof) {
	
	//String sql="select * from bof where bof_name='"+bof_name+"'";
	String sql="select * from music_info where music_id="+first_id+" or music_id="+second_id+" or music_id="+third_id+" order by music_id asc";
	
	System.out.println(sql);
	try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			res.next();
			bof.setFirstName(res.getString("music_name"));
			bof.setFirstCreator(res.getString("music_creator"));
			System.out.println(res.getString("music_creator"));
			//second info
			res.next();
			bof.setSecondName(res.getString("music_name"));
			bof.setSecondCreator(res.getString("music_creator"));
			System.out.println(res.getString("music_creator"));
			//third info
			res.next();
			bof.setThirdName(res.getString("music_name"));
			bof.setThirdCreator(res.getString("music_creator"));
			System.out.println(res.getString("music_creator"));
		}
	 } 
	 catch (SQLException e)
	 {
		e.printStackTrace();
	 }
	 return bof;
}
//-------------------music list----------------
public ResultSet getMusicList(String bofName) {
	//创建 statement对象
		try 
		{
			sta = conn.createStatement(); // 执行SQL查询语句
//			res = sta.executeQuery("(select bof_id%100*100 as id from bof where bof_name='"+bofName+"')"
//					+ " and music_id<"
//					+ " (select (bof_id%100+1)*100 as id from bof where bof_name='"+bofName+"')"
//					+ " order by music_id asc");
			res=sta.executeQuery("select * from music_info where music_id>\r\n"
					+ "(select bof_id%100*100 as id from bof where bof_name='"+bofName+"')\r\n"
					+ " and music_id<\r\n"
					+ " (select (bof_id%100+1)*100 as id from bof where bof_name='"+bofName+"')\r\n"
					+ " order by music_id asc;\r\n"
					+ "");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return res;
	}
//-------------------game list--------------
public ResultSet getGameList()
{
	try
	{
		sta=conn.createStatement();
		res=sta.executeQuery("select * from game_info");
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
	return res;
}
//------------game info---------
public GameBean getGameBean(String game_name)
{
	String sql="select * from game_info where game_name='"+game_name+"'";
	GameBean game=new GameBean();
	System.out.println(sql);
	try 
	 {
		sta=conn.createStatement();
		res=sta.executeQuery(sql);
		if(res!=null)
		{
			while(res.next())
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String gameTime=(sdf.format(res.getDate("game_time")));
				game.setGamename(game_name);
				game.setGamecname(res.getString("game_cname"));
				game.setGametime(gameTime);
				game.setGamecompany(res.getString("game_company"));
				game.setGameplatform(res.getString("game_platform"));
				game.setGameintro(res.getString("game_intro"));
				break;
			}
		}
	 } 
	 catch (SQLException e)
	 {
		e.printStackTrace();
	 }
	return game;
}
//-------------------include list--------------
public ResultSet getInclude(String gameName)
{
	try
	{
		sta=conn.createStatement();
		res=sta.executeQuery("select music_name,include_level1,include_level2,include_level3,include_level4,include_flag from include,music_info where include.include_id=music_info.music_id and include_game='"+gameName+"'");
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
	return res;
}
//--------------search----------------
public ResultSet searchMusic(String keyword)
{
	try
	{
		sta=conn.createStatement();
		res=sta.executeQuery("select * from music_info where lower(music_name) like '%"+keyword.toLowerCase()+"%' or lower(music_creator) like '%"+keyword.toLowerCase()+"%'");
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
	return res;
}
//-----------like------------
public boolean insertLike(String userId,String musicId,String likeFlag)
{
	String sql;
	if(likeFlag.equals("true"))
	{
		sql ="delete from like_info where user_id="+userId+" and music_id="+musicId;
	}
	else
	{
		sql ="insert into like_info values("+userId+","+musicId+")";
	}
	try 
	{
		sta = conn.createStatement();
		boolean result=sta.execute(sql);
		System.out.println("result:"+result);
		return result;
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
public ResultSet getLike(String userId)
{
	try
	{
		sta=conn.createStatement();
		res=sta.executeQuery("select * from like_info natural join music_info where user_id="+userId);
	}
	catch(SQLException e)
	{
		e.printStackTrace();
	}
	
	return res;
}
public boolean checkLikeExists(String user_id,String music_id)
{
	 boolean flag=false;
	 try 
	 {
		 System.out.println("判断收藏记录是否已经存在");
		 sta = conn.createStatement(); // 执行SQL查询语句
		 res = sta.executeQuery("select * from like_info where user_id='"+user_id+"' and music_id="+music_id);// 获得结果集
		 if (res != null) 
		 {
			 while (res.next()) 
			 { // 遍历结果集
				 if (res.getInt("user_id")==Integer.parseInt(user_id)&&res.getInt("music_id")==Integer.parseInt(music_id)) 
				 {
					 System.out.println("该收藏记录已存在");
				 flag= true;
				 break;
				 }
			 }
		 }
	  } catch (SQLException e) 
	 {
		 e.printStackTrace();
		 flag= false;
	 }
		 return flag;
}

//------------alter file---------
public boolean alterNickname(String userId,String userNickname)
{
	String sql="update user_info set user_nickname='"+userNickname+"' where user_id="+userId;
	try 
	{
		sta = conn.createStatement();
		return sta.execute(sql);
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
public boolean alterSex(String userId,String userSex)
{
	String sql="update user_info set user_sex='"+userSex+"' where user_id="+userId;
	try 
	{
		sta = conn.createStatement();
		return sta.execute(sql);
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
public boolean alterBirthday(String userId,String userBirthday)
{
	String sql="update user_info set user_birthday='"+userBirthday+"' where user_id="+userId;
	try 
	{
		sta = conn.createStatement();
		return sta.execute(sql);
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
//----------------admin---------------
public ResultSet getUserList() {
	//创建 statement对象
		try 
		{
			sta = conn.createStatement(); // 执行SQL查询语句
			res=sta.executeQuery("select * from user_info");
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return res;
	}
public boolean deleteUser(String userId)
{
	String sql="delete from user_info where user_id="+userId;
	try 
	{
		sta = conn.createStatement();
		return sta.execute(sql);
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
public boolean resetPassword(String userId)
{
	String sql="update user_info set user_password=111111 where user_id="+userId;
	try 
	{
		sta = conn.createStatement();
		return sta.execute(sql);
	} 
	catch (SQLException e)
	{
		 e.printStackTrace();
	}
	return false;
}
}

