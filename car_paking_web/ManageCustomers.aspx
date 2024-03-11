<%@ Page Title="" Language="C#" MasterPageFile="~/Admin_master.master" AutoEventWireup="true" CodeFile="ManageCustomers.aspx.cs" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="Server">
    <h2>Customers</h2>
    <p>
        <asp:Label ID="Label1" runat="server" forecolor="Red" Text="Label" Visible="False"></asp:Label></p>
    <p>
        <asp:GridView ID="GridView1" runat="server" OnRowCommand="Row_command" AutoGenerateColumns="False" BackColor="White" BorderColor="White" BorderStyle="Ridge" BorderWidth="2px" CellPadding="3" CellSpacing="1" GridLines="None" Width="494px">
            <Columns>
                <asp:BoundField DataField="C_id" HeaderText="Customer ID" SortExpression="Ph_no" />
                <asp:BoundField DataField="C_fname" HeaderText="Name" SortExpression="Ph_no" />
                <asp:BoundField DataField="C_ph" HeaderText="Phone_no" SortExpression="Ph_no" />
                <asp:BoundField DataField="C_email" HeaderText="Email ID" SortExpression="Ph_no" />
                <asp:BoundField DataField="C_balance" HeaderText="Balance" SortExpression="Ph_no" />
                <asp:ButtonField Text="Delete" CommandName="Delete_row" />
            <%--    <asp:ButtonField Text="Action" />--%>
            </Columns>

            <FooterStyle BackColor="#C6C3C6" ForeColor="Black" />
            <HeaderStyle BackColor="#4A3C8C" Font-Bold="True" ForeColor="#E7E7FF" />
            <PagerStyle BackColor="#C6C3C6" ForeColor="Black" HorizontalAlign="Right" />
            <RowStyle BackColor="#DEDFDE" ForeColor="Black" />
            <SelectedRowStyle BackColor="#9471DE" Font-Bold="True" ForeColor="White" />
            <SortedAscendingCellStyle BackColor="#F1F1F1" />
            <SortedAscendingHeaderStyle BackColor="#594B9C" />
            <SortedDescendingCellStyle BackColor="#CAC9C9" />
            <SortedDescendingHeaderStyle BackColor="#33276A" />

        </asp:GridView>
    </p>
</asp:Content>

