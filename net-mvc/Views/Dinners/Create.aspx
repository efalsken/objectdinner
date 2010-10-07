<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<db4oDinnerMVC.Models.DinnerFormViewModel>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
	Create a Dinner
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

		<h2>Host a Dinner</h2>

		<% Html.RenderPartial("DinnerForm"); %>
		
		<div>
				<%: Html.ActionLink("Back to List", "Index") %>
		</div>

</asp:Content>

<asp:Content ID="Content3" ContentPlaceHolderID="HeadArea" runat="server">
</asp:Content>
