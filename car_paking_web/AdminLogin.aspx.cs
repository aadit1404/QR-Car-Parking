using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;

public partial class _Default : System.Web.UI.Page
{
    SqlConnection conn;
    SqlCommand comm;
    SqlDataAdapter da;
    DataSet ds;
    public string cs=ConfigurationManager.AppSettings["Connect"].ToString();

    protected void Page_Load(object sender, EventArgs e)
    {
        if(Session["a_id"]!=null)
        {
            Session.Abandon();
            Session.Clear();
        }
    }
    protected void btnsubmit_Click(object sender, EventArgs e)
    {
        conn=new SqlConnection(cs);
        conn.Open();
        da = new SqlDataAdapter();
        da.SelectCommand=new SqlCommand();
        da.SelectCommand.Connection = conn;
        da.SelectCommand.CommandText="Ad_login";
        da.SelectCommand.CommandType=CommandType.StoredProcedure;
        da.SelectCommand.Parameters.AddWithValue("@username",txtusername.Text);
        da.SelectCommand.Parameters.AddWithValue("@password",txtpassword.Text);
        DataTable dt=new DataTable();
       
        da.Fill(dt);
        conn.Close();
        if(dt.Rows.Count>0)
        {
            Session["a_id"]=dt.Rows[0]["Ad_id"].ToString();
            Response.Redirect("Home.aspx");
        }
        else
        {
            Label1.Visible=true;
            Label1.Text="Invalid Details";
        }

    }
}