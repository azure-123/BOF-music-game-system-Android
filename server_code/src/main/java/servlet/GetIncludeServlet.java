package servlet;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.xml.ws.Response;

import domain.BaseBean; 
import domain.UserBean;
import database.DBUtils;
import com.google.gson.Gson;

public class GetIncludeServlet extends HttpServlet {

	@Override
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	 throws ServletException, IOException {
	 doPost(request, response);
	 }
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException 
	{
		System.out.println("request--->"+request.getRequestURL()+"===="+request.getParameterMap().toString());
		String gameName=request.getParameter("game_name");
		response.setContentType("text/html;charset=utf-8");
		// �������ݿ�
		DBUtils dbUtils = new DBUtils();
		dbUtils.openConnect();
		// �����ݿ�����
		BaseBean data = new BaseBean(); // ������󣬻ش����ͻ��˵�json����
	 
		//����������ƣ���øñ����������б�
		ResultSet rs=dbUtils.getInclude(gameName);
		//��List��װ���ص�����
		List<Map<String,Object>> includeList=new ArrayList<Map<String,Object>>();

		if (rs != null) 
		{
			try 
			{
				while (rs.next()) 
				{
					//��HashMapװ��������
					Map<String,Object> map=new HashMap<String,Object>();

					
					//map.put("gameName", rs.getString("game_name"));
					map.put("musicName",rs.getString("music_name"));
					map.put("level1",Float.toString(rs.getFloat("include_level1")));
					map.put("level2",Float.toString(rs.getFloat("include_level2")));
					map.put("level3",Float.toString(rs.getFloat("include_level3")));
					map.put("level4",Float.toString(rs.getFloat("include_level4")));
					map.put("includeFlag",rs.getInt("include_flag"));
					
					System.out.println("Success!");
					includeList.add(map);
				}
				data.setData(includeList);
				data.setCode(1);
			} catch (SQLException e) 
			{
				e.printStackTrace();
			}
		}
		else
		{
			data.setCode(101);
		}
		
		Gson gson = new Gson();
		String json = gson.toJson(data);
		// ������ת����json�ַ���
		try 
		{
			response.getWriter().println(json);
			// ��json���ݴ����ͻ���
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally
		{
			response.getWriter().close(); // �ر����������Ȼ�ᷢ�������
		}
		dbUtils.closeConnect(); // �ر����ݿ�����}
	}
}
