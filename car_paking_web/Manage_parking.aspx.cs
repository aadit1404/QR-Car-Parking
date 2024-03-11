using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Net.Mail;
using System.IO;

public partial class _Default : System.Web.UI.Page
{
    SqlConnection conn;
    SqlDataAdapter adp;
    public string cs = ConfigurationManager.AppSettings["Connect"].ToString();
    DataTable dt;

    protected void Page_Load(object sender, EventArgs e)
    {
        if (!IsPostBack)
        {
            if (Session["a_id"] != null)
            {
                conn = new SqlConnection(cs);
                conn.Open();
                adp = new SqlDataAdapter("select Area_id,Area_name,total_slot from Area_master", conn);
                dt = new DataTable();
                adp.Fill(dt);
                if (dt.Rows.Count > 0)
                {
                    GridView1.DataSource = dt;
                    GridView1.DataBind();
                }
                else
                {
                    Label1.Visible = true;
                    Label1.Text = "No record Found";
                }
                conn.Close();
            }
            else
            {
                Response.Write("<script>alert('Log in first')</script>");
                Response.Redirect("AdminLogin.aspx");
            }

        }
    }

    protected void btnadd_Click(object sender, EventArgs e)
    {

        try
        {
            if (FileUpload1.HasFile)
            {
                string fileName = Path.GetFileName(FileUpload1.FileName);
                FileUpload1.SaveAs(Server.MapPath("Parking_Images/") + fileName);
                string filePath = "/Parking_Images/" + fileName;

                conn = new SqlConnection(cs);
                //Generate Random Password
                var chars = "QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsazxcvbnm0987654321";
                var stringargs = new char[8];
                var random = new Random();
                for (int i = 0; i < stringargs.Length; i++)
                {
                    stringargs[i] = chars[random.Next(chars.Length)];
                }
                string password = new String(stringargs);

                SqlCommand _cmd = new SqlCommand("Insert_area", conn);
                _cmd.CommandType = CommandType.StoredProcedure;
                _cmd.Parameters.AddWithValue("@area_name", txtarea.Text);
                _cmd.Parameters.AddWithValue("@total_slots", txtttlslots.Text);
                _cmd.Parameters.AddWithValue("@mail", txtMail.Text);
                _cmd.Parameters.AddWithValue("@pass", password);
                _cmd.Parameters.AddWithValue("@lat", txtLat.Text);
                _cmd.Parameters.AddWithValue("@lon", txtLon.Text);
                _cmd.Parameters.AddWithValue("@img", filePath);
                conn.Open();
                _cmd.ExecuteNonQuery();
                conn.Close();
                sms1(txtMail.Text, password);

                Response.Redirect("Manage_parking.aspx"); Response.Write("<script>alert('Added Successfully')</script>");
            }
            else
                Response.Write("<script>alert('Please upload image')</script>");
        }
        catch(Exception ex)
        {
            throw ex;
        }
    }

    public void sms1(string mailid, string password)
    {

        MailMessage mail = new MailMessage();
        SmtpClient SmtpServer = new SmtpClient("my-demo.in");
        mail.From = new MailAddress("test@my-demo.in");
        mail.To.Add(mailid);
        mail.Subject = "Registration Details";
        mail.Body = "Your Login-id is : " + mailid + "\n" + "Your Password is : " + password;
        SmtpServer.Port = 25;
        SmtpServer.Credentials = new System.Net.NetworkCredential("test@my-demo.in", "Password@123");
        SmtpServer.Send(mail);

    }

    public void Row_command(object sender, GridViewCommandEventArgs e)
    {

        if (e.CommandName == "Delete_row")
        {
            int index = Convert.ToInt32(e.CommandArgument);
            GridViewRow gr = GridView1.Rows[index];
            conn = new SqlConnection(cs);
            conn.Open();
            SqlCommand cmd2 = new SqlCommand("delete from Area_master where Area_id='" + gr.Cells[0].Text + "'", conn);
            cmd2.ExecuteNonQuery();
            conn.Close();
            Response.Write("<script>alert('Deleted')</script>");
            Response.Redirect("Manage_parking.aspx");
        }


    }


}