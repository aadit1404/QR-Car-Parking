<%@ Page Title="" Language="C#" MasterPageFile="~/Admin_master.master" AutoEventWireup="true" CodeFile="Manage_parking.aspx.cs" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <style type="text/css">
        .auto-style1 {
            height: 42px;
        }
        .auto-style2 {
            height: 38px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="Server">
    <div>
        <a href="#">
            <h2>>>Add new Parking Place</h2>
        </a>
        <p>
            <asp:Label ID="Label1" runat="server" ForeColor="Red" Text="Label" Visible="False"></asp:Label>
        </p>
        <br /><div style="margin-left: 320px">
        <table>
            <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label2" runat="server" Text="Area Name" Font-Size="Medium"></asp:Label></td>
                <td class="auto-style2">
                    <asp:Label ID="Label3" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style2">
                    <asp:TextBox ID="txtarea" runat="server" Height="25px" Width="156px"></asp:TextBox></td>
                <td class="auto-style2">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" ErrorMessage="*" ControlToValidate="txtarea" ForeColor="Red"></asp:RequiredFieldValidator>
                </td>
            </tr>
            <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label4" runat="server" Text="Parking Slots" Font-Size="Medium"></asp:Label></td>
                <td class="auto-style2">
                    <asp:Label ID="Label5" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style2">
                    <asp:TextBox ID="txtttlslots" runat="server" Height="26px" Width="155px"></asp:TextBox></td>
                <td class="auto-style2">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" Display="Dynamic" ErrorMessage="*" ForeColor="Red" ControlToValidate="txtttlslots"></asp:RequiredFieldValidator>
                    <asp:RegularExpressionValidator ID="RegularExpressionValidator1" Disply="Dynamic" ForeColor="Red" runat="server" ErrorMessage="Allow digit only" ControlToValidate="txtttlslots" ValidationExpression="^\d+$"></asp:RegularExpressionValidator>
                </td>
            </tr>
            <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label6" runat="server" Text="Enter Email ID" Font-Size="Medium"></asp:Label>
                </td>
                <td class="auto-style2">
                    <asp:Label ID="Label7" runat="server" Text=":"></asp:Label>
                </td>
                <td class="auto-style2">
                    <asp:TextBox ID="txtMail" runat="server" Height="24px" Width="155px"></asp:TextBox></td>
                <td class="auto-style2">
                    <asp:RegularExpressionValidator ID="RegularExpressionValidator2" runat="server" ControlToValidate="txtMail" Display="Dynamic" ErrorMessage="Invalid emailid" ForeColor="Red" ValidationExpression="^([\w\.\-]+)@([\w\-]+)((\.(\w){2,3})+)$"></asp:RegularExpressionValidator>
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator3" runat="server" ControlToValidate="txtMail" Display="Dynamic" ErrorMessage="*" ForeColor="Red"></asp:RequiredFieldValidator>
                </td>
            </tr>
             <tr>
                <td class="auto-style2">
                    <asp:Label ID="Label8" runat="server" Text="Enter Latitude" Font-Size="Medium"></asp:Label>
                </td>
                <td class="auto-style2">
                    <asp:Label ID="Label9" runat="server" Text=":"></asp:Label>
                </td>
                <td class="auto-style2">
                    <asp:TextBox ID="txtLat" runat="server" Height="26px" Width="156px" ></asp:TextBox></td>
                    <asp:RegularExpressionValidator ID="RegularExpressionValidator3" runat="server" ControlToValidate="txtLat" ForeColor="Red" ErrorMessage="Not a Valid Latitude." 
                        ValidationExpression="^-?([1-9]?[0-9])\.{1}\d{1,12}"></asp:RegularExpressionValidator>
                <td class="auto-style2">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator4" runat="server" ControlToValidate="txtLat" Display="Dynamic" ErrorMessage="*" ForeColor="Red"></asp:RequiredFieldValidator>
                </td>
            </tr>
             <tr>
                <td class="auto-style1">
                    <asp:Label ID="Label10" runat="server" Text="Enter Longitude" Font-Size="Medium"></asp:Label>
                </td>
                <td class="auto-style1">
                    <asp:Label ID="Label11" runat="server" Text=":"></asp:Label>
                </td>
                <td class="auto-style1">
                    <asp:TextBox ID="txtLon" runat="server" Height="25px" Width="155px"></asp:TextBox></td>
                    <asp:RegularExpressionValidator ID="RegularExpressionValidator4" runat="server" ControlToValidate="txtLon" ForeColor="Red" ErrorMessage="Not a Valid Longitude." 
                        ValidationExpression="^-?([1-9]?[0-9])\.{1}\d{1,12}"></asp:RegularExpressionValidator>
                <td class="auto-style1">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator5" runat="server" ControlToValidate="txtLon" Display="Dynamic" ErrorMessage="*" ForeColor="Red"></asp:RequiredFieldValidator>
                </td>
            </tr>
            <tr>
                <td colspan="4">
                    <asp:FileUpload ID="FileUpload1" runat="server" />
                </td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr>
                <td></td>
            </tr>
            <tr align="center">
                <td colspan="3">
                    <asp:Button ID="btnadd" runat="server" Text="Submit" OnClick="btnadd_Click" Height="35px" Width="72px" />&nbsp;&nbsp;&nbsp;&nbsp;
                    <asp:Button ID="btnclrs" runat="server" Text="Clear" CausesValidation="False" Height="34px" Width="63px" /></td>
            </tr>
        </table></div>
        <br />
        <div style="margin-left: 320px"><p>
            <asp:GridView ID="GridView1" OnRowCommand="Row_command" runat="server" AutoGenerateColumns="False" BackColor="White" BorderColor="#CC9966" BorderStyle="None" BorderWidth="1px" CellPadding="4" Width="358px">
                <Columns>
                    <asp:BoundField DataField="Area_id" HeaderText="Area ID" SortExpression="Ph_no" />
                    <asp:BoundField DataField="Area_name" HeaderText="Area name" SortExpression="Ph_no" />
                    <asp:BoundField DataField="total_slot" HeaderText="Total Slots" SortExpression="Ph_no" />
                    <asp:ButtonField Text="Delete" CommandName="Delete_row" />
                </Columns>
                <FooterStyle BackColor="#FFFFCC" ForeColor="#330099" />
                <HeaderStyle BackColor="#990000" Font-Bold="True" ForeColor="#FFFFCC" />
                <PagerStyle BackColor="#FFFFCC" ForeColor="#330099" HorizontalAlign="Center" />
                <RowStyle BackColor="White" ForeColor="#330099" />
                <SelectedRowStyle BackColor="#FFCC66" Font-Bold="True" ForeColor="#663399" />
                <SortedAscendingCellStyle BackColor="#FEFCEB" />
                <SortedAscendingHeaderStyle BackColor="#AF0101" />
                <SortedDescendingCellStyle BackColor="#F6F0C0" />
                <SortedDescendingHeaderStyle BackColor="#7E0000" />
            </asp:GridView>
        </p></div>
        <p>
            &nbsp;
        </p>
    </div>

</asp:Content>

