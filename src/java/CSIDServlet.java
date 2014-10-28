
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CSIDServlet extends HttpServlet {
	
    private ServletContext ctxServletContext;
    
//    private Logger logger = Logger.getLogger("CSIDWebAppLogs");
//    private FileHandler fh = null;
    private String url;
    Connection DBConnectionObj = null;
    @Override
    public void init() throws ServletException {
        ctxServletContext = getServletConfig().getServletContext();
        try {
           DBConnection db=new DBConnection();
           DBConnectionObj=db.getConnection();    
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.init();
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/xml;charset=UTF-8");
        PrintWriter out = response.getWriter();

        //TimeRange Params
        String mintime = null;
        String maxtime = null;
        String API_Status="";
        String API_Name="";
        String cuid="";
        String tablename="";
  
        
        try {
            //ResourceBundle properties = ResourceBundle.getBundle("Properties.DBConnection");
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            url = request.getRequestURL().toString() + request.getQueryString();       //url to get parameter for exception
/*
            //<editor-fold defaultstate="collapsed" desc="Logger initialization">
            fh = (FileHandler) ctxServletContext.getAttribute("fileHandler");          //Separate Log file creation for Exception
            if (fh == null) {
                System.out.println("FileHandler is null");
                ctxServletContext.setAttribute("fileHandler", new FileHandler(properties.getString("LogFile") + "_" + sdf.format(new Date(System.currentTimeMillis())) + ".log", true));
                fh = (FileHandler) ctxServletContext.getAttribute("fileHandler");
                System.out.println("FileHandler retrieved from Context!");
            } else {
                System.out.println("FileHandler available");
            }*/
            
            SimpleFormatter formatter = new SimpleFormatter();
//            fh.setFormatter(formatter);
//            logger.addHandler(fh);
//            logger.setUseParentHandlers(false);
            //</editor-fold>
            
             DBConnectionObj = (Connection) ctxServletContext.getAttribute("con");
             if (DBConnectionObj == null || DBConnectionObj.isClosed()) {
             System.out.println("dbConnectionObj is null");
             ctxServletContext.setAttribute("con", new DBConnection().getConnection());
             DBConnectionObj = (Connection) ctxServletContext.getAttribute("con");
             System.out.println("dbConnectionObj retrieved from Context!::");
             } else {
             System.out.println("dbConnection available");
             }

         
            //TimeRange Params
            if (request.getParameter("mintime") != null) {
                mintime = request.getParameter("mintime");
            }
            if (request.getParameter("maxtime") != null) {
                maxtime = request.getParameter("maxtime");
            }
                                          
            
            if (request.getParameter("tablename") != null) {
            tablename= request.getParameter("tablename");
              
            }
            
            if (request.getParameter("API_Status") != null) {
               API_Status = request.getParameter("API_Status");
                
            }
            if (request.getParameter("API_Name") != null) {
             API_Name= request.getParameter("API_Name");
            }
            if (request.getParameter("cuid") != null) {
                cuid = request.getParameter("cuid");
            }
            
            //Device Screen Params
            if (request.getParameter("ErrorText") != null) {
                String ErrorText = request.getParameter("ErrorText");
          
            }            
            if (request.getParameter("ErrorType") != null) {
                String ErrorType = request.getParameter("ErrorType");
            }
            if (request.getParameter("Name") != null) {
                String Name = request.getParameter("Name");
            }         
            if (request.getParameterMap().containsKey("function")) {
                String strFunction = request.getParameter("function").trim();
                
                System.out.println(strFunction);
                if (strFunction.equals("getApiCount")) {
                	getApiCount(out,"1",mintime,maxtime,API_Name,API_Status,cuid,tablename);
                } 
            }
           // DBConnectionObj.close();
           // DBConnectionObj = null;
        } catch (Exception e) {
            e.printStackTrace();
//
//            logger.info(e.toString());
//            logger.info(url);
//            logger.logp(Level.WARNING, "CSIDServlet", "INIT", "Exception caught INIT", e);
        }
    }

   
    private void getApiCount(PrintWriter out, String id, String starttime, String endtime, String API_Name, String API_Status, String cuid,String tablename)
    {
        System.out.println("-------------------------------getAPICount--------------------------------");
        
        String strFields="cuid,API_Name,API_Status,API_Call_Count";
        //String APIQuery = "Select " + strFields + " from " + tablename + " "
             //   + "where Date >= '" + starttime + "' and Date < '" + endtime + "'";
       String APIQuery = "Select cuid,status,apimodule,memid from ITMTURESPONSESTATUS";
       // deviceQuery = whereClause(deviceQuery, content, plan, percentile, selfCongested, contentCategory, deviceCategory, Node, strContentFilterType, null, null, busy, strCMTS, strSite, strHeadend, strHub, strPort, tier, deviceMfg, deviceOS, strDeviceView, selfCongestedTonnage, null, region, state, zip, cpeDevice, cpeDeviceMfg, strSegmentFilter);
        APIQuery += " group by 1,2 order by 1,2  desc limit 100" ;

       Statement stmt=null;
            ResultSet set;
        try {
            out.println("<xml id='1'>");
            out.println("<Device>");
             System.out.println("API Query: " + "\n" + APIQuery);         
            set = stmt.executeQuery(APIQuery);
            
            System.out.println("API Query: " + "\n" + APIQuery);
            out.println("<Category>");
                while (set.next()) {
                    out.println("<Record cuid='" + set.getString(1) + "' API_Name='" + set.getString(2) +  "' API_Status='" + set.getString(3) + "'  API_Call_Count='" + set.getInt(4) + "'/>");
                }
                out.println("</Category>");

            out.println("</Device>");

            out.println("</xml>");

            set.close();
            set = null;
            stmt.close();
            stmt = null;
            System.out.println(url);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.info(url);
//            logger.info(e.toString());
//            logger.logp(Level.WARNING, "CSIDServlet", "getAPICount", "Exception caught in getDevices", e);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
