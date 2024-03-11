<%@ Page Title="" Language="C#" MasterPageFile="~/LobbyMaster.master" AutoEventWireup="true" CodeFile="AddBalance.aspx.cs" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="Server">
    <style type="text/css">
        .auto-style1 {
            height: 39px;
        }

        .auto-style2 {
            height: 40px;
        }

        .auto-style3 {
            height: 39px;
            text-align: center;
        }
        .auto-style4 {
            height: 32px;
        }
        .auto-style5 {
            height: 31px;
        }
        .auto-style6 {
            height: 30px;
        }
    </style>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="Server">
    <br />
    <div class="clr"></div>
    <div style="margin-left: 300px">
        <table>
            <tr>
                <td class="auto-style3">
                    <asp:Label ID="Label2" runat="server" Text="Enter User Mail Id" Font-Size="Medium"></asp:Label>
                </td>
                <td class="auto-style3">
                    <asp:Label ID="Label1" runat="server" Text=":"></asp:Label>
                </td>
                <td class="auto-style1">
                    <asp:TextBox ID="TextBox1" runat="server" Height="24px" Width="186px"></asp:TextBox>
                </td>
                <td class="auto-style3">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" ErrorMessage="*" Font-Size="X-Large" ForeColor="Red" ControlToValidate="TextBox1"></asp:RequiredFieldValidator>
                </td>
            </tr>
            <tr align="center">
                <td colspan="3" class="auto-style2">
                    <asp:Button ID="btnViewUser" runat="server" Text="Submit" Width="106px" OnClick="btnViewUser_Click" />
                    <br />
                    <asp:Label ID="Label17" runat="server" ForeColor="Red" Text="Label" Visible="False"></asp:Label>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <div class="clr"></div>

    <div style="margin-left:300px">

        <table runat="server" id="tbl1">
            <tr>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="Label3" runat="server" Text="User Id"></asp:Label></td>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="Label4" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="lblId" runat="server" Text="Label"></asp:Label></td>
            </tr>
            <tr>
                <td class="auto-style5">
                    <asp:Label  Font-Size="Medium" ID="Label6" runat="server" Text="Name"></asp:Label></td>
                <td class="auto-style5">
                    <asp:Label  Font-Size="Medium" ID="Label7" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style5">
                    <asp:Label Font-Size="Medium" ID="lblName" runat="server" Text="Label"></asp:Label></td>
            </tr>
            <tr>
                <td class="auto-style6">
                    <asp:Label  Font-Size="Medium" ID="Label9" runat="server" Text="Mail"></asp:Label></td>
                <td class="auto-style6">
                    <asp:Label Font-Size="Medium" ID="Label10" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style6">
                    <asp:Label Font-Size="Medium" ID="lblMail" runat="server" Text="Label"></asp:Label></td>
            </tr>
                 <tr>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="Label5" runat="server" Text="Currrent Balance"></asp:Label></td>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="Label8" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style4">
                    <asp:Label Font-Size="Medium" ID="lblBal" runat="server" Text="Label"></asp:Label></td>
            </tr>
            <tr>
                <td class="auto-style5"><asp:Label Font-Size="Medium" ID="Label15" runat="server" Text="Balance"></asp:Label></td>
                <td class="auto-style5"><asp:Label Font-Size="Medium" ID="Label16" runat="server" Text=":"></asp:Label></td>
                <td class="auto-style5">
                    <asp:TextBox Font-Size="Medium" ID="txtBalance" runat="server" ValidationGroup="aa"></asp:TextBox></td>
                <td class="auto-style5">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ErrorMessage="*" ControlToValidate="txtBalance" Font-Size="X-Large" ForeColor="Red"></asp:RequiredFieldValidator>
                    <asp:RegularExpressionValidator ID="RegularExpressionValidator2" runat="server" ControlToValidate="txtBalance" Display="Dynamic" ErrorMessage="Invalid No" ForeColor="Red" ValidationExpression="^[0-9]{1,9}$" ValidationGroup="aa"></asp:RegularExpressionValidator>
                </td>
            </tr>
            <tr align="center">
                <td colspan="3">
                    <asp:Button ID="btnAddBal" runat="server" Text="Add" OnClick="btnAddBal_Click" Height="36px" ValidationGroup="aa" Width="100px" />
                </td>
            </tr>
            </table>

    </div>
</asp:Content>

