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
    public string cs = ConfigurationManager.AppSettings["Connect"].ToString();


    protected void Page_Load(object sender, EventArgs e)
    {

        if (Session["l_id"] != null)
        {
            Session.Abandon();
            Session.Clear();
        }
    }
    protected void btnsubmit_Click(object sender, EventArgs e)
    {
        conn = new SqlConnection(cs);
        conn.Open();
        da = new SqlDataAdapter();
        da.SelectCommand = new SqlCommand();
        da.SelectCommand.Connection = conn;
        da.SelectCommand.CommandText = "Select * from Area_master where email_ID=@username and Password=@password";
        da.SelectCommand.Parameters.AddWithValue("@username", txtusername.Text);
        da.SelectCommand.Parameters.AddWithValue("@password", txtpassword.Text);
        DataTable dt = new DataTable();
        da.Fill(dt);
        if (dt.Rows.Count > 0)
        {
            Session["l_id"] = dt.Rows[0]["Area_id"].ToString();
            Response.Redirect("AddBalance.aspx");
        }
        else
        {
            Label1.Visible = true;
            Label1.Text = "Invalid Details";
        }

    }
}