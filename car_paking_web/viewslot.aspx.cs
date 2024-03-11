using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data.SqlClient;
using System.Data;
using System.Configuration;
using System.IO;

public partial class _Default : System.Web.UI.Page
{
    SqlConnection conn;
    public string cs = ConfigurationManager.AppSettings["Connect"].ToString();
    
    protected void Page_Load(object sender, EventArgs e)
    {
        conn = new SqlConnection(cs);
        if (!IsPostBack)
        {
            DataTable dt = new DataTable();
            using (SqlCommand cmd = new SqlCommand("select Slot_no,Flag from ParkingSlot_master where Area_id=@Area_id", conn))
            {
                cmd.Parameters.AddWithValue("@Area_id", Session["l_id"]);
                using (SqlDataAdapter adp = new SqlDataAdapter(cmd))
                {
                    adp.Fill(dt);
                }
                GridView1.DataSource = dt;
                GridView1.DataBind();
            }
        }
    }
    protected void btnAddSlot_Click(object sender, EventArgs e)
    {
        try
        {
            string area_id = Session["l_id"].ToString();
            int last_slot_no = 0;

            SqlConnection conn = new SqlConnection(cs);
            SqlCommand cmd = new SqlCommand("select max(slot_no) as MaxSlot from parkingslot_master where area_id=@area_id", conn);
            cmd.Parameters.AddWithValue("@area_id", area_id);
            SqlDataAdapter sda = new SqlDataAdapter(cmd);
            DataTable dt = new DataTable();
            sda.Fill(dt);

            if (dt.Rows.Count > 0)
            {
                last_slot_no = Convert.ToInt32(dt.Rows[0]["MaxSlot"]);
                int slot_no = last_slot_no + 1;

                conn = new SqlConnection(cs);
                cmd = new SqlCommand("insert into parkingslot_master (area_id,Slot_no,Flag) values (@area_id,@slot_no,@flag)", conn);
                cmd.Parameters.AddWithValue("@area_id", area_id);
                cmd.Parameters.AddWithValue("@slot_no", slot_no);
                cmd.Parameters.AddWithValue("@flag", 0);
                conn.Open();
                int res = cmd.ExecuteNonQuery();
                if (res == 1)
                {
                    cmd = new SqlCommand("select count(*) as total from parkingslot_master where area_id = @area_id", conn);
                    cmd.Parameters.AddWithValue("@area_id", area_id);
                    SqlDataAdapter da = new SqlDataAdapter(cmd);
                    DataTable dt1 = new DataTable();
                    da.Fill(dt1);
                    if (dt1.Rows.Count > 0)
                    {
                        int no_of_slots = Convert.ToInt32(dt1.Rows[0]["total"]);
                        cmd = new SqlCommand("update area_master set total_slot=@total where area_id=@area_id", conn);
                        cmd.Parameters.AddWithValue("@area_id", area_id);
                        cmd.Parameters.AddWithValue("@total", no_of_slots);
                        int result = cmd.ExecuteNonQuery();
                        if (result == 1)
                            conn.Close();

                        Response.Redirect("viewslot.aspx");
                    }
                }
                //Response.Write(dt.Rows.Count);
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}