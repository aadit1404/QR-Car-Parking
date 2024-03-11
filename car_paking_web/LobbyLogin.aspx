<%@ Page Title="" Language="C#" MasterPageFile="~/LobbyMaster.master" AutoEventWireup="true" CodeFile="LobbyLogin.aspx.cs" Inherits="_Default" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" Runat="Server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" Runat="Server">

     <h2>Lobby Login</h2>
    <div>
        <table align="center" style="width: 318px">
            <tr>
                <td class="auto-style4">
                    <asp:Label ID="lblusername" runat="server" Text="Username"></asp:Label>
                </td>
                <td class="auto-style4">:</td>
                <td class="auto-style5">
                    <asp:TextBox ID="txtusername" runat="server" Height="22px" Width="158px"></asp:TextBox>
                </td>
                <td class="auto-style4">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator2" runat="server" ControlToValidate="txtusername" ForeColor="Red" ErrorMessage="*"></asp:RequiredFieldValidator>
                </td>
            </tr>
            <tr>
                <td class="auto-style4">
                    <asp:Label ID="lblpassword" runat="server" Text="Password"></asp:Label>
                </td>
                <td class="auto-style4">:</td>
                <td class="auto-style5">
                    <asp:TextBox ID="txtpassword" runat="server" TextMode="Password" Height="22px" Width="157px"></asp:TextBox>
                </td>
                <td class="auto-style4">
                    <asp:RequiredFieldValidator ID="RequiredFieldValidator1" runat="server" ControlToValidate="txtpassword" ForeColor="Red" ErrorMessage="*"></asp:RequiredFieldValidator>
                </td>
            </tr>
            <tr>
                <td colspan="3" align="center" class="auto-style6">
                    <asp:Button ID="btnsubmit" runat="server" Text="Login" Height="30px" OnClick="btnsubmit_Click" Width="91px" />
                    <br />
                    <asp:Label ID="Label1" runat="server" ForeColor="Red" Text="Label" Visible="False"></asp:Label>
                </td>
                <td class="auto-style6"></td>
            </tr>

        </table>
    </div>

</asp:Content>

