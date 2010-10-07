<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<db4oDinnerMVC.Helpers.PaginatedList<db4oDinnerMVC.Models.Dinner>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
	Upcoming Nerd Dinners
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
	<h2>Upcoming Nerd Dinners</h2>

	<ul class="upcomingdinners">
		<% foreach (var dinner in Model) { %>
		<li>
			<%: Html.ActionLink(dinner.Title, "Details", new { id=dinner.ID }) %>
			on <strong>
				<%: dinner.EventDate.ToString("yyyy-MMM-dd")%>
				<%: dinner.EventDate.ToString("HH:mm tt")%></strong> at
			<%: dinner.Address + " " + dinner.Country %>
		</li>
		<% } %>
	</ul>
	<div class="pagination">
		<% if (Model.HasPreviousPage) { %>
		<%: Html.RouteLink("<<< Previous Page", 
											 "UpcomingDinners", 
											 new { page=(Model.PageIndex-1) }) %>
		<% } %>
		<% if (Model.HasNextPage) { %>
		<%: Html.RouteLink("Next Page >>>", 
											 "UpcomingDinners", 
											 new { page = (Model.PageIndex + 1) })%>
		<% } %>
	</div>

	<div>
		<%= Html.ActionLink("Create a dinner", "Create", "Dinners") %>
	</div>

</asp:Content>

<asp:Content ContentPlaceHolderID="HeadArea" runat="server">
</asp:Content>
