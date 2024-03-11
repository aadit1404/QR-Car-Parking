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
    public static string ph;
    public static Int32 bal;


    protected void Page_Load(object sender, EventArgs e)
    {
        if(!IsPostBack)
        {
            if(Session["l_id"]!=null)
            {
                tbl1.Visible = false;
            }
        }
    }
    protected void btnViewUser_Click(object sender, EventArgs e)
    {

        conn = new SqlConnection(cs);
        conn.Open();
        da = new SqlDataAdapter();
        da.SelectCommand = new SqlCommand();
        da.SelectCommand.Connection = conn;
        da.SelectCommand.CommandText = "select * from Customer_master where C_email=@username";
        da.SelectCommand.CommandType = CommandType.Text;
        da.SelectCommand.Parameters.AddWithValue("@username", TextBox1.Text);

        DataTable dt = new DataTable();

        da.Fill(dt);
        conn.Close();
        if (dt.Rows.Count > 0)
        {
            Label17.Visible = false;
            tbl1.Visible=true;
            lblId.Text = dt.Rows[0]["C_id"].ToString();
            lblMail.Text = dt.Rows[0]["C_email"].ToString();
            lblName.Text = dt.Rows[0]["C_fname"].ToString() + " " + dt.Rows[0]["C_lname"].ToString();
            lblBal.Text = dt.Rows[0]["C_balance"].ToString();
            ph = dt.Rows[0]["C_ph"].ToString();
            //bal = dt.Rows[0]["C_balance"].ToString();
        }
        else
        {
            tbl1.Visible = false;
            Label17.Visible = true;
            Label17.Text = "Invalid Details";
        }
    }
    protected void btnAddBal_Click(object sender, EventArgs e)
    {
        conn = new SqlConnection(cs);
        conn.Open();
        SqlDataAdapter adp = new SqlDataAdapter();
        adp.SelectCommand = new SqlCommand("Update Customer_master set C_balance=@bal where C_id=@id");
        adp.SelectCommand.Connection = conn;
        bal=Convert.ToInt32(lblBal.Text)+Convert.ToInt32(txtBalance.Text);
        adp.SelectCommand.Parameters.AddWithValue("@bal",bal);
        adp.SelectCommand.Parameters.AddWithValue("@id", lblId.Text);
        adp.SelectCommand.ExecuteNonQuery();
        conn.Close();
        Response.Redirect("AddBalance.aspx");
    }
}