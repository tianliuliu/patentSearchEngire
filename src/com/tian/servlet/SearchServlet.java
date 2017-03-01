package com.tian.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.tian.dao.PatentDao;
import com.tian.dao.impl.HbasePatentDaoImpl;
import com.tian.dao.impl.HivePatentDaoImpl;
import com.tian.dao.impl.PatentDaoImpl;
import com.tian.entity.Patent;
import com.tian.lucene.LuceneIndex;

public class SearchServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */


	public SearchServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		
		String keyword = request.getParameter("keyword");
		int type = Integer.valueOf(request.getParameter("type"));
		String page = request.getParameter("pageNum");
		keyword = new String(keyword.getBytes("ISO-8859-1"), "UTF-8"); 
		
		int pageNum; 
		if(page == null||page.equals("")){
			pageNum =1;
		}else{
			pageNum = Integer.valueOf(page);
		}
		int pageSize = 10;
		//按照专利号查询
		if(type == 0){
			PatentDao patentDao = new PatentDaoImpl();
			Patent patent = patentDao.getPatentByZlNumber(keyword);
			
			request.setAttribute("keyword", keyword);
			request.setAttribute("type", type);	
			request.setAttribute("patent", patent);
			request.getRequestDispatcher("SingleResult.jsp").forward(request, response);
			
			
			
			
		}
		//按照关键字查询
		else{
			LuceneIndex luceneIndex = new LuceneIndex();
			PatentDao patentDao = new PatentDaoImpl();
			//PatentDao patentDao = new HbasePatentDaoImpl();
			//PatentDao patentDao = new HivePatentDaoImpl();
			//long current = System.currentTimeMillis();
			
			List<Long> pids = luceneIndex.searchPage("nameAndabstract",keyword,pageNum,pageSize);
			//long luceneindextime = System.currentTimeMillis();
			//System.out.print("lucene索引的时间为"+(luceneindextime-current)+ "ms######");
			//System.out.println(pids);
			List<Patent> patents = patentDao.getPatentsByPids(pids);
			//System.out.println("mysql查询的时间为"+(System.currentTimeMillis()-luceneindextime)+"ms");
			//System.out.println(patents);
			request.setAttribute("keyword", keyword);
			request.setAttribute("type", type);
			request.setAttribute("pageNum", pageNum);
			request.setAttribute("pageSize", pageSize);
			request.setAttribute("patents", patents);
			request.getRequestDispatcher("searchresult.jsp").forward(request, response);
			
		}
		
		
		
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
		
	
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
		
		
	}

}
