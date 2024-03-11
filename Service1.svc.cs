using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Data;
using System.Data.SqlClient;
using System.Configuration;
using System.Net.Mail;
using System.Net;
using System.IO;
using System.Device.Location;

namespace QR_Car_parking_service
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "Service1" in code, svc and config file together.
    // NOTE: In order to launch WCF Test Client for testing this service, please select Service1.svc or Service1.svc.cs at the Solution Explorer and start debugging.
    public class Service1 : IService1
    {
        SqlConnection conn;
        SqlCommand comm;
        SqlDataReader dr;
        SqlDataAdapter sda;
        DataTable dt;
        DataSet ds;
        private string cs = ConfigurationManager.ConnectionStrings["connect"].ConnectionString;

        public Cust_log login(string email, string pass)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            comm = new SqlCommand("select * from Customer_master where C_email=@mail and C_password=@pass", conn);
            comm.Parameters.AddWithValue("@mail", email);
            comm.Parameters.AddWithValue("@pass", pass);
            sda = new SqlDataAdapter(comm);
            dt = new DataTable();
            sda.Fill(dt);

            if (dt.Rows.Count > 0)
            {
                int cust_id = Convert.ToInt32(dt.Rows[0]["C_id"]);

                conn = new SqlConnection(cs);
                comm = new SqlCommand("select * from Booking_master where Cust_id=@custid and dateadd(MINUTE, 10, dt) < getdate() and Status='1'", conn);
                comm.Parameters.AddWithValue("@custid", cust_id);
                sda = new SqlDataAdapter(comm);
                dt = new DataTable();
                sda.Fill(dt);

                if (dt.Rows.Count > 0)
                {
                    int book_id = Convert.ToInt32(dt.Rows[0]["Booking_id"]);
                    int area_id = Convert.ToInt32(dt.Rows[0]["Area_id"]);
                    int slot_id = Convert.ToInt32(dt.Rows[0]["Slot_id"]);

                    conn = new SqlConnection(cs);
                    comm = new SqlCommand("delete from Booking_master where Booking_id=@bookid", conn);
                    comm.Parameters.AddWithValue("@bookid", book_id);
                    conn.Open();
                    comm.ExecuteNonQuery();
                    conn.Close();

                    conn = new SqlConnection(cs);
                    comm = new SqlCommand("update ParkingSlot_master set Flag='1' where Area_id=@areaid and Slot_no=@slotno", conn);
                    comm.Parameters.AddWithValue("@areaid", area_id);
                    comm.Parameters.AddWithValue("@slotno", slot_id);
                    conn.Open();
                    comm.ExecuteNonQuery();
                    conn.Close();
                }
            }
            conn.Close();

            conn = new SqlConnection(cs);
            conn.Open();
            using (comm = new SqlCommand("Select * from Customer_master where C_password=@pass and acc_status=1 and  C_email=@mail or C_ph=@mail", conn))
            {
                comm.Parameters.AddWithValue("@mail", email);
                comm.Parameters.AddWithValue("@pass", pass);
                using (dr = comm.ExecuteReader())
                {
                    if (dr.Read())
                    {
                        return new Cust_log
                        {
                            cust_id = dr.GetValue(0).ToString(),
                            bal = dr.GetValue(7).ToString()
                        };

                    }
                    else
                    {
                        return new Cust_log();
                    }
                }
                // conn.Close();            
            }
        }

        public List<Park_list> Getlist(string cust_id, string area_name)
        {
            string a_name_ = area_name.Replace("_", " ");
            conn = new SqlConnection(cs);
            List<Park_list> lst = new List<Park_list>();
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp = new SqlDataAdapter();//("View_slots", conn);
            adp.SelectCommand = new SqlCommand();
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.CommandText = "View_slots";
            adp.SelectCommand.CommandType = CommandType.StoredProcedure;
            adp.SelectCommand.Parameters.AddWithValue("@area_name", a_name_);
            adp.Fill(ds, "Park_slot");
            conn.Close();
            if (ds.Tables["Park_slot"].Rows.Count > 0)
            {
                for (int i = 0; i < ds.Tables["Park_slot"].Rows.Count; i++)
                {
                    Park_list objvsess = new Park_list
                    {
                        slot_id = ds.Tables["Park_slot"].Rows[i]["Slot_id"].ToString(),
                        slot_no = ds.Tables["Park_slot"].Rows[i]["Slot_no"].ToString(),
                        slot_url = ds.Tables["Park_slot"].Rows[i]["slot_url"].ToString()
                    };
                    lst.Add(objvsess);
                }
                return lst;
            }
            else
            {
                return lst;
            }
        }


        public Slot_log GetPark_log(string slot_id)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp = new SqlDataAdapter();
            adp.SelectCommand = new SqlCommand();
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.CommandText = "Select_parkslot";
            adp.SelectCommand.CommandType = CommandType.StoredProcedure;
            adp.SelectCommand.Parameters.AddWithValue("@slot_id", slot_id);
            adp.Fill(ds, "slot_detalis");
            conn.Close();
            if (ds.Tables["slot_detalis"].Rows.Count > 0)
            {
                return new Slot_log()
                {
                    msg = "Booked"
                };
            }
            else
            {//Slot_details
                conn.Open();
                SqlDataAdapter adp2 = new SqlDataAdapter();
                adp2.SelectCommand = new SqlCommand();
                adp2.SelectCommand.Connection = conn;
                adp2.SelectCommand.CommandText = "Slot_details";
                adp2.SelectCommand.CommandType = CommandType.StoredProcedure;
                adp2.SelectCommand.Parameters.AddWithValue("@slot_id", slot_id);
                adp2.Fill(ds, "slot_info");
                conn.Close();
                if (ds.Tables["slot_info"].Rows.Count > 0)
                {
                    return new Slot_log()
                    {
                        area_name = ds.Tables["slot_info"].Rows[0]["Area_name"].ToString(),
                        slot_no = ds.Tables["slot_info"].Rows[0]["Slot_no"].ToString()
                    };
                }
                else
                {
                    return new Slot_log();
                }

            }
        }


        public Book Bookslot(string slot_id, string cust_id)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            SqlCommand comm3 = new SqlCommand("select * from Booking_master where Cust_id=@cust_id and cast(dt as date)=cast(getdate() as date) and Status=1 or Status=2  ");
            SqlDataAdapter da2 = new SqlDataAdapter(comm3);
            comm3.Connection = conn;
            comm3.Parameters.AddWithValue("@cust_id", cust_id);
            DataTable dt2 = new DataTable();
            da2.Fill(dt2);
            conn.Close();
            if (dt2.Rows.Count > 0)
            {
                return new Book();
            }
            else
            {
                conn = new SqlConnection(cs);
                conn.Open();
                SqlCommand comm2 = new SqlCommand();
                SqlDataAdapter da = new SqlDataAdapter(comm2);
                comm2.Connection = conn;
                comm2.CommandText = "Book_slot";
                comm2.CommandType = CommandType.StoredProcedure;
                comm2.Parameters.AddWithValue("@slot_id", slot_id);
                comm2.Parameters.AddWithValue("@cust_id", cust_id);
                DataTable dt = new DataTable();
                da.Fill(dt);
                conn.Close();
                if (dt.Rows.Count > 0)
                {
                    return new Book
                    {
                        msg2 = dt.Rows[0]["Booking_id"].ToString()
                    };
                }
                else
                {
                    return new Book();
                }
            }

        }


        public List<Fill_list> fill_area(string ulat, string ulon, string temp)
        {
            conn = new SqlConnection(cs);
            List<Fill_list> lst2 = new List<Fill_list>();
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp = new SqlDataAdapter("select Area_name,lat,lon from Area_master", conn);
            adp.Fill(ds, "Area_names");
            conn.Close();
            if (ds.Tables["Area_names"].Rows.Count > 0)
            {
                for (int i = 0; i < ds.Tables["Area_names"].Rows.Count; i++)
                {
                    if (ds.Tables["Area_names"].Rows[i]["lat"].ToString() != String.Empty && ds.Tables["Area_names"].Rows[i]["lon"] != String.Empty)
                    {
                        double dlatitude = Convert.ToDouble(ds.Tables["Area_names"].Rows[i]["lat"]);
                        double dlongitude = Convert.ToDouble(ds.Tables["Area_names"].Rows[i]["lon"]);

                        var sCoord = new GeoCoordinate(Convert.ToDouble(ulat), Convert.ToDouble(ulon));
                        var eCoord = new GeoCoordinate(dlatitude, dlongitude);
                        Fill_list objf_l = new Fill_list
                        {
                            a_name = ds.Tables["Area_names"].Rows[i]["Area_name"].ToString(),
                            lat = ds.Tables["Area_names"].Rows[i]["lat"].ToString(),
                            lon = ds.Tables["Area_names"].Rows[i]["lon"].ToString(),
                            InformationDistance = sCoord.GetDistanceTo(eCoord)
                        };
                        lst2.Add(objf_l);
                    }
                }
                //return lst2;
                return lst2.OrderBy(e => e.InformationDistance).ToList();
            }
            else
            {
                return lst2;
            }
        }

        public List<Fill_list> fill_area_by(string byType)
        {
            conn = new SqlConnection(cs);
            List<Fill_list> lst2 = new List<Fill_list>();
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp;
            if (byType.Equals("Rating"))//or nos-number_of_slot
                adp = new SqlDataAdapter("select Area_id,Area_name,lat,lon,case when avgrate is null then 0 else avgrate end as avgrate from Area_master as am left join (select areaid,avg(rating) avgrate from Rate_Master group by areaid) as avgtb on am.Area_id=avgtb.areaid order by avgrate desc", conn);
            else
                adp = new SqlDataAdapter("select Area_id, Area_name, total_slot as avgrate,  lat, lon from Area_master order by total_slot desc", conn);
            adp.Fill(ds, "Area_names");
            conn.Close();
            if (ds.Tables["Area_names"].Rows.Count > 0)
            {
                for (int i = 0; i < ds.Tables["Area_names"].Rows.Count; i++)
                {
                    Fill_list objf_l = new Fill_list
                     {
                         a_name = ds.Tables["Area_names"].Rows[i]["Area_name"].ToString(),
                         lat = ds.Tables["Area_names"].Rows[i]["lat"].ToString(),
                         lon = ds.Tables["Area_names"].Rows[i]["lon"].ToString(),
                         byValue = ds.Tables["Area_names"].Rows[i]["avgrate"].ToString()
                     };
                    lst2.Add(objf_l);

                }
                return lst2;
            }
            else
            {
                return lst2;
            }
        }

        public Booking_info BookingID(string cust_id)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            SqlCommand comm2 = new SqlCommand("select * from Booking_master as b left join ParkingSlot_master as p on b.Slot_id=p.Slot_id where Cust_id=@cust_id and cast(dt as date)=cast(getdate() as date) and Status=1 or Status=2");
            SqlDataAdapter da = new SqlDataAdapter(comm2);
            comm2.Connection = conn;
            comm2.Parameters.AddWithValue("@cust_id", cust_id);
            DataTable dt = new DataTable();
            da.Fill(dt);
            conn.Close();
            if (dt.Rows.Count > 0)
            {
                return new Booking_info
                {
                    booking_id = dt.Rows[0]["Booking_id"].ToString(),
                    path = dt.Rows[0]["slot_url"].ToString()
                };
            }
            else
            {
                return new Booking_info();
            }
        }

        public RegResp register(UserData udata)
        {
            try
            {

                conn = new SqlConnection(cs);
                conn.Open();
                SqlDataAdapter da = new SqlDataAdapter();
                da.SelectCommand = new SqlCommand("select * from Customer_master where C_email=@mail;delete from Customer_master where C_email=@mail and acc_status=0");
                da.SelectCommand.Connection = conn;
                da.SelectCommand.Parameters.AddWithValue("@mail", udata.Email);
                DataTable de = new DataTable();
                da.Fill(de);
                if (de.Rows.Count > 0)
                {
                    return new RegResp { msg = "Invalid" };
                }
                else
                {
                    ds = new DataSet();
                    SqlDataAdapter adp = new SqlDataAdapter();
                    adp.SelectCommand = new SqlCommand("Insert into Customer_master(C_fname, C_lname,C_Address, C_ph, C_email, C_password,C_balance,OTP,acc_status) values (@fname,@lname,@addr,@ph,@mail,@pass,0,@otp,0)");
                    adp.SelectCommand.Connection = conn;
                    adp.SelectCommand.Parameters.AddWithValue("@fname", udata.Fname);
                    adp.SelectCommand.Parameters.AddWithValue("@lname", udata.Lname);
                    adp.SelectCommand.Parameters.AddWithValue("@addr", udata.Addr);
                    adp.SelectCommand.Parameters.AddWithValue("@mail", udata.Email);
                    adp.SelectCommand.Parameters.AddWithValue("@pass", udata.Pass);
                    adp.SelectCommand.Parameters.AddWithValue("@ph", udata.Ph);
                    #region generate otp
                    var chars = "0123456789";
                    var stringargs = new char[4];
                    var random = new Random();
                    for (int i = 0; i < stringargs.Length; i++)
                    {
                        stringargs[i] = chars[random.Next(chars.Length)];
                    }
                    string otp = new String(stringargs);
                    #endregion
                    adp.SelectCommand.Parameters.AddWithValue("@otp", otp);

                    adp.SelectCommand.ExecuteNonQuery();
                    conn.Close();
                    sendotp(udata.Email, udata.Ph, otp);
                    return new RegResp
                    {
                        msg = "Inserted",
                        otp = otp
                    };
                }
            }
            catch (Exception e)
            {
                return new RegResp();
            }
        }

        public void sendotp(string mail, string ph, string otp)
        {
            try
            {
                WebRequest MyRssRequest = WebRequest.Create("https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=c5a0a9b5-24a5-49b7-9a45-77a9ab765f99&senderid=SMSTST&channel=1&DCS=0&flashsms=0&number=" + ph + "&text=Your OTP " + otp + " \n&route=13");
                WebResponse MyRssResponse = MyRssRequest.GetResponse();
                Stream MyRssStream = MyRssResponse.GetResponseStream();

                //SmtpClient smtpserver = new SmtpClient("demoproject.in");
                //MailMessage maill = new MailMessage();
                //smtpserver.Credentials = new System.Net.NetworkCredential("demo@demo", "AB1234cd@");
                //smtpserver.Port = 25;
                //smtpserver.EnableSsl = true;
                //maill = new MailMessage();
                //maill.From = new MailAddress("projectmailnew2018@gmail.com");
                //maill.To.Add(mail);
                //maill.Subject = "Account Details";
                //maill.Body = "ParK IN" + "\n" + "Your OTP is " + otp + ".";
                //smtpserver.Send(maill);


                MailMessage mail1 = new MailMessage();
                SmtpClient SmtpServer = new SmtpClient("demoproject.in");
                mail1.From = new MailAddress("test@demoproject.in");
                mail1.To.Add(mail);
                mail1.Subject = "New Registration";
                mail1.Body = "ParK IN" + "\n" + "Your OTP is " + otp + ".";
                SmtpServer.Port = 25;
                SmtpServer.Credentials = new System.Net.NetworkCredential("test@demoproject.in", "Password@123");
                SmtpServer.Send(mail1);



            }
            catch (Exception e)
            {

            }
        }

        public respFeed checkOtp(string phno, string otp)
        {
            try
            {
                conn = new SqlConnection(cs);
                //conn.Open();
                SqlDataAdapter da = new SqlDataAdapter();
                da.SelectCommand = new SqlCommand("select * from Customer_master where C_email=@mail and OTP=@otp and acc_status=0");
                da.SelectCommand.Connection = conn;
                da.SelectCommand.Parameters.AddWithValue("@mail", phno);
                da.SelectCommand.Parameters.AddWithValue("@otp", otp);
                DataTable de = new DataTable();
                da.Fill(de);
                if (de.Rows.Count > 0)
                {
                    SqlDataAdapter adp = new SqlDataAdapter();
                    adp.SelectCommand = new SqlCommand("update Customer_master set acc_status=1 where C_id=@cid");
                    adp.SelectCommand.Connection = conn;
                    adp.SelectCommand.Parameters.AddWithValue("@cid", de.Rows[0]["C_id"].ToString());
                    conn.Open();
                    adp.SelectCommand.ExecuteNonQuery();
                    conn.Close();
                    return new respFeed { msg = "valid" };
                }
                else
                {
                    return new respFeed { msg = "invalid" };
                }
            }
            catch (Exception ex)
            {
                return new respFeed { msg = ex.ToString() };
            }
        }

        public respPLLog parkingLotLogin(string mail, string pass)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            SqlDataAdapter da = new SqlDataAdapter();
            da.SelectCommand = new SqlCommand("select * from Area_master where email_ID=@mail and Password=@pass");
            da.SelectCommand.Connection = conn;
            da.SelectCommand.Parameters.AddWithValue("@mail", mail);
            da.SelectCommand.Parameters.AddWithValue("@pass", pass);
            DataTable de = new DataTable();
            da.Fill(de);
            conn.Close();
            if (de.Rows.Count > 0)
            {
                return new respPLLog
                {
                    areaId = de.Rows[0]["Area_id"].ToString(),
                    areaName = de.Rows[0]["Area_name"].ToString()
                };
            }
            else
            {
                return new respPLLog();
            }
        }

        public respCheck checkBookLog(string areaId, string bookingId)
        {
            try
            {

                conn = new SqlConnection(cs);
                conn.Open();
                SqlDataAdapter da = new SqlDataAdapter();
                da.SelectCommand = new SqlCommand("select * from Booking_master as b left join ParkingSlot_master as p on b.Slot_id=p.Slot_id where Booking_id=@bid and cast(dt as date)=cast(getdate() as date)");
                da.SelectCommand.Connection = conn;
                da.SelectCommand.Parameters.AddWithValue("@bid", bookingId);
                DataTable de = new DataTable();
                da.Fill(de);
                if (de.Rows.Count > 0)
                {
                    int slotId = Convert.ToInt32(de.Rows[0]["Slot_id"]);
                    if (areaId == de.Rows[0]["Area_id"].ToString())
                    {
                        if (de.Rows[0]["Status"].ToString() == "1")
                        {
                            DataTable dt = new DataTable();
                            SqlDataAdapter adp = new SqlDataAdapter();
                            adp.SelectCommand = new SqlCommand("select * from Customer_master where C_id=@cid");
                            adp.SelectCommand.Connection = conn;
                            adp.SelectCommand.Parameters.AddWithValue("@cid", de.Rows[0]["Cust_id"].ToString());
                            adp.Fill(dt);
                            conn.Close();
                            return new respCheck
                            {
                                custId = dt.Rows[0]["C_id"].ToString(),
                                custName = dt.Rows[0]["C_fname"].ToString() + " " + dt.Rows[0]["C_lname"].ToString(),
                                slotId = de.Rows[0]["Slot_no"].ToString()
                            };
                        }
                        else if (de.Rows[0]["Status"].ToString() == "2")
                        {
                            SqlDataAdapter adp3 = new SqlDataAdapter();
                            adp3.SelectCommand = new SqlCommand("select getdate() as d", conn);
                            DataTable dt3 = new DataTable();
                            adp3.Fill(dt3);
                            DateTime todatetime = Convert.ToDateTime(dt3.Rows[0]["d"].ToString());
                            DateTime fromdatetime = Convert.ToDateTime(de.Rows[0]["dt2"].ToString());
                            var hours = (todatetime - fromdatetime).TotalHours;
                            int totalhours = Convert.ToInt32(Math.Ceiling(hours));

                            int totalcharge = totalhours * 20;

                            DataTable dt = new DataTable();
                            SqlDataAdapter adp = new SqlDataAdapter();
                            adp.SelectCommand = new SqlCommand("select * from Customer_master where C_id=@cid");
                            adp.SelectCommand.Connection = conn;
                            adp.SelectCommand.Parameters.AddWithValue("@cid", de.Rows[0]["Cust_id"].ToString());
                            adp.Fill(dt);

                            int newbalance = Convert.ToInt32(dt.Rows[0]["C_balance"].ToString()) - totalcharge;

                            adp = new SqlDataAdapter();
                            adp.SelectCommand = new SqlCommand("update Customer_master set C_balance=@bal where C_id=@custid");
                            adp.SelectCommand.Connection = conn;
                            adp.SelectCommand.Parameters.AddWithValue("@bal", newbalance);
                            adp.SelectCommand.Parameters.AddWithValue("@custid", dt.Rows[0]["C_id"].ToString());
                            adp.SelectCommand.ExecuteNonQuery();
                            adp = new SqlDataAdapter();
                            adp.SelectCommand = new SqlCommand("update Booking_master set Status='3',cost=@c where Booking_id=@bid");
                            adp.SelectCommand.Parameters.AddWithValue("@bid", bookingId);
                            adp.SelectCommand.Parameters.AddWithValue("@c", totalcharge);
                            adp.SelectCommand.Connection = conn;
                            adp.SelectCommand.ExecuteNonQuery();

                            adp = new SqlDataAdapter();
                            adp.SelectCommand = new SqlCommand("update ParkingSlot_master set Flag='0' where Area_id=@area and Slot_id=@slot");
                            adp.SelectCommand.Parameters.AddWithValue("@area", areaId);
                            adp.SelectCommand.Parameters.AddWithValue("@slot", slotId);
                            adp.SelectCommand.Connection = conn;
                            adp.SelectCommand.ExecuteNonQuery();

                            string u_name = dt.Rows[0]["C_fname"].ToString() + " " + dt.Rows[0]["C_lname"].ToString();
                            string u_mail = dt.Rows[0]["C_email"].ToString();
                            string u_ph = dt.Rows[0]["C_ph"].ToString();
                            SendDeductedNotification(u_name, totalcharge, newbalance, u_mail, u_ph);

                            return new respCheck { slotId = "Deducted" };
                        }
                        else { return new respCheck { custName = "Invalid" }; }
                    }
                    else
                    {
                        return new respCheck { custName = "Invalid" };
                    }
                }
                else
                {
                    return new respCheck { custName = "invalid" };
                }
            }
            catch (Exception e)
            {
                return new respCheck { custName = e.ToString() };
            }
        }

        public respAllocate allocateSlot(string bookingId)
        {
            try
            {
                conn = new SqlConnection(cs);
                ds = new DataSet();
                conn.Open();
                SqlDataAdapter adp = new SqlDataAdapter();
                adp.SelectCommand = new SqlCommand("allocateSlot");
                adp.SelectCommand.Connection = conn;
                adp.SelectCommand.CommandType = CommandType.StoredProcedure;
                adp.SelectCommand.Parameters.AddWithValue("@bookingid", bookingId);
                adp.SelectCommand.ExecuteNonQuery();
                conn.Close();

                return new respAllocate { msg = "updated" };
            }
            catch (Exception e)
            {
                return new respAllocate
                {
                    msg = e.ToString()
                };
            }
        }

        public List<transLog> getLog(string cid)
        {
            conn = new SqlConnection(cs);
            List<transLog> lst = new List<transLog>();
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp = new SqlDataAdapter();
            adp.SelectCommand = new SqlCommand();
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.CommandText = "select * from Booking_master as b left join Area_master as a on a.Area_id=b.Area_id where Cust_id=@cid and cost is not null";
            adp.SelectCommand.CommandType = CommandType.Text;
            adp.SelectCommand.Parameters.AddWithValue("@cid", cid);
            adp.Fill(ds, "trans");
            conn.Close();
            if (ds.Tables["trans"].Rows.Count > 0)
            {
                for (int i = 0; i < ds.Tables["trans"].Rows.Count; i++)
                {
                    transLog objvsess = new transLog
                    {
                        bid = ds.Tables["trans"].Rows[i]["Booking_id"].ToString(),
                        cost = ds.Tables["trans"].Rows[i]["cost"].ToString(),
                        date = ds.Tables["trans"].Rows[i]["dt"].ToString(),
                        mallname = ds.Tables["trans"].Rows[i]["Area_name"].ToString()
                    };
                    lst.Add(objvsess);
                }
                return lst;
            }
            else
            {
                return lst;
            }
        }

        public respAdd addBalance(string cid, string ac, string bal)
        {
            conn = new SqlConnection(cs);
            conn.Open();

            SqlDataAdapter adp = new SqlDataAdapter();
            adp.SelectCommand = new SqlCommand("select * from Customer_master where C_id=@cid");
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.Parameters.AddWithValue("@cid", cid);
            DataTable dt = new DataTable();
            adp.Fill(dt);
            int bal1 = Convert.ToInt32(bal) + Convert.ToInt32(dt.Rows[0]["C_balance"].ToString());
            adp = new SqlDataAdapter();
            adp.SelectCommand = new SqlCommand("update Customer_master set C_balance=@bal where C_id=@cid");
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.Parameters.AddWithValue("@cid", cid);
            adp.SelectCommand.Parameters.AddWithValue("@bal", bal1);
            adp.SelectCommand.ExecuteNonQuery();
            conn.Close();
            return new respAdd { msg = "added", bal = bal1.ToString() };

        }

        public respForget forgetPassword(string mail)
        {
            try
            {

                conn = new SqlConnection(cs);
                conn.Open();
                SqlDataAdapter da = new SqlDataAdapter();
                da.SelectCommand = new SqlCommand("select * from Customer_master where C_email=@mail");
                da.SelectCommand.Connection = conn;
                da.SelectCommand.Parameters.AddWithValue("@mail", mail);
                DataTable de = new DataTable();
                da.Fill(de);
                if (de.Rows.Count > 0)
                {
                    try
                    {
                        string password = de.Rows[0]["C_password"].ToString();
                        SmtpClient smtpserver = new SmtpClient();
                        MailMessage maill = new MailMessage();
                        smtpserver.Credentials = new System.Net.NetworkCredential("projectmailnew2018@gmail.com", "AB1234cd@");
                        smtpserver.Port = 25;
                        smtpserver.EnableSsl = true;
                        smtpserver.Host = "smtp.gmail.com";
                        maill = new MailMessage();
                        maill.From = new MailAddress("projectmailnew2018@gmail.com");
                        maill.To.Add(mail);
                        maill.Subject = "Registration Details";
                        maill.Body = "ParK IN" + "\n" + "Your Login-id is : " + mail + "\n" + "Your Password is : " + password;
                        smtpserver.Send(maill);
                    }
                    catch (Exception) { return new respForget { msg = "sent" }; }
                    return new respForget { msg = "sent" };
                }
                else
                {
                    return new respForget { msg = "Invalid" };
                }
            }
            catch (Exception e)
            {
                return new respForget { msg = e.ToString() };
            }
        }

        public respFeed postFeed(string feed, string mail)
        {
            conn = new SqlConnection(cs);
            conn.Open();
            ds = new DataSet();
            SqlDataAdapter adp = new SqlDataAdapter();
            adp.SelectCommand = new SqlCommand("insert into feedback_master values(@feed,@id,GETDATE())");
            adp.SelectCommand.Connection = conn;
            adp.SelectCommand.CommandType = CommandType.Text;
            adp.SelectCommand.Parameters.AddWithValue("@feed", feed.Replace("_", " "));
            adp.SelectCommand.Parameters.AddWithValue("@id", mail);
            adp.SelectCommand.ExecuteNonQuery();
            conn.Close();
            return new respFeed
            {
                msg = "Inserted"
            };
        }

        public void SendDeductedNotification(string name, int charge, int new_bal, string mail, string ph)
        {
            try
            {
                WebRequest MyRssRequest = WebRequest.Create("https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=c5a0a9b5-24a5-49b7-9a45-77a9ab765f99&senderid=TESTIN&channel=1&DCS=0&flashsms=0&number=" + ph + "&text=You Have Been charge " + charge + " rupees for parking \n Current Balance=" + new_bal + " \n&route=13");
                WebResponse MyRssResponse = MyRssRequest.GetResponse();
                Stream MyRssStream = MyRssResponse.GetResponseStream();

                SmtpClient smtpserver = new SmtpClient();
                MailMessage maill = new MailMessage();
                smtpserver.Credentials = new System.Net.NetworkCredential("projectmailnew2018@gmail.com", "AB1234cd@");
                smtpserver.Port = 25;
                smtpserver.EnableSsl = true;
                smtpserver.Host = "smtp.gmail.com";
                maill = new MailMessage();
                maill.From = new MailAddress("projectmailnew2018@gmail.com");
                maill.To.Add(mail);
                maill.Subject = "Account Details";
                maill.Body = "Park In" + "\n" + "Hi " + name + " you have been charge " + charge + "for parking \n" + "Your Current Balance is : " + new_bal;
                smtpserver.Send(maill);


            }
            catch (Exception e)
            {

            }
        }

        public resp checkRating(string areaId, string userid)
        {
            SqlDataAdapter adp = new SqlDataAdapter();
            DataTable dtData = new DataTable();
            conn = new SqlConnection(cs);
            try
            {
                adp.SelectCommand = new SqlCommand("select * from Rate_Master where userid=@userid and areaid=(select Area_id from Booking_master where Booking_id=@bid)", conn);
                adp.SelectCommand.Parameters.AddWithValue("@userid", userid);
                adp.SelectCommand.Parameters.AddWithValue("@bid", areaId);
                adp.Fill(dtData);
                if (dtData.Rows.Count > 0)
                {
                    return new resp { rate = dtData.Rows[0]["rating"].ToString(), msg = "given" };
                }
                else
                {
                    return new resp { msg = "not_given" };
                }
            }
            catch (Exception ex)
            {
                return new resp { msg = ex.ToString() };
            }
            finally
            {
                adp.Dispose(); dtData.Dispose();
            }
        }

        public resp giverating(string rating, string areaId, string userid)
        {
            SqlDataAdapter adp = new SqlDataAdapter();
            DataTable dtData = new DataTable();
            conn = new SqlConnection(cs);
            try
            {
                adp.SelectCommand = new SqlCommand("updateRating", conn);
                adp.SelectCommand.CommandType = CommandType.StoredProcedure;
                adp.SelectCommand.Parameters.AddWithValue("@userid", userid);
                adp.SelectCommand.Parameters.AddWithValue("@areaid", areaId);
                adp.SelectCommand.Parameters.AddWithValue("@rate", rating);
                adp.Fill(dtData);
                if (dtData.Rows.Count > 0)
                {
                    return new resp { rate = dtData.Rows[0]["rating"].ToString(), msg = "inserted" };
                }
                else
                {
                    return new resp { msg = "inserted" };
                }
            }
            catch (Exception ex)
            {
                return new resp { msg = ex.ToString() };
            }
            finally
            {
                adp.Dispose(); dtData.Dispose();
            }
        }

        public respReg forgotPassword(ForgotPass fp)
        {
            try
            {
                conn = new SqlConnection(cs);
                conn.Open();
                SqlCommand cmd = new SqlCommand("select c_password from customer_master where c_email=@mail", conn);
                cmd.Parameters.AddWithValue("@mail", fp.email);
                SqlDataAdapter da = new SqlDataAdapter(cmd);
                DataTable dt = new DataTable();
                da.Fill(dt);
                if (dt.Rows.Count > 0)
                {
                    string password = dt.Rows[0]["c_password"].ToString();
                    MailMessage mail = new MailMessage();
                    SmtpClient SmtpServer = new SmtpClient("demoproject.in");
                    mail.From = new MailAddress("test@demoproject.in");
                    mail.To.Add(fp.email);
                    mail.Subject = "Password";
                    mail.Body = "Your password is:" + password;
                    SmtpServer.Port = 25;
                    SmtpServer.Credentials = new System.Net.NetworkCredential("test@demoproject.in", "Password@123");
                    SmtpServer.Send(mail);
                    return new respReg { msg = "Please check your email" };
                }
                else
                {
                    return new respReg { msg = "Not found" };
                }

            }
            catch (Exception ex)
            {
                return new respReg { msg = ex.ToString() };
            }
        }


    }
}
