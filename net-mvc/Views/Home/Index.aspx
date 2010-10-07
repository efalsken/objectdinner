<%@ Page Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<System.Collections.Generic.IEnumerable<Dinner>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
		Db4oNerdDinner
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
	<h2>Db4o Nerd Dinner</h2>
	<p></p>
	<hr />
	<% if (User.Identity.IsAuthenticated) { %>
		<h3>Your upcoming dinners:</h3>
		<% if (Model.Count() == 0) {%>
			<p>You have not RSVP'd to any dinners.</p>
		<%}%>
		<ul class="mydinners">
			<% foreach (var dinner in Model) { %>
			<li>
				<%: Html.ActionLink(dinner.Title, "Details", new { id = dinner.ID })%>
				on <strong>
					<%: dinner.EventDate.ToString("yyyy-MMM-dd")%>
					<%: dinner.EventDate.ToString("HH:mm tt")%></strong> at
				<%: dinner.Address + " " + dinner.Country%>
			</li>
			<% } %>
		</ul>
	<% }
		else {%>
		<p>Before you can RSVP for dinners, you need to login or register.</p>
	<% }%>
</asp:Content>
