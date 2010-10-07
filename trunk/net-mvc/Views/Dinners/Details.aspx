<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<db4oDinnerMVC.Models.Dinner>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
	Dinner Details - <%: Model.Title %>
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

	<p>
		<strong>When:</strong> 
		<abbr class="dtstart" title="<%: Model.EventDate.ToString("s") %>">
			<%: Model.EventDate.ToString("MMM dd, yyyy") %> 
			<strong>@</strong>
			<%: Model.EventDate.ToShortTimeString() %>
		</abbr>
	</p>

	<p>
		<strong>Where:</strong>
		<span class="location adr">
			<span class="extended-address"><%: Model.Address %></span>, 
			<span class="country-name"><%: Model.Country %></span>
		</span>
	</p>

	<p>
		<strong>Description:</strong> 
		<span class="description"><%: Model.Description %></span>
	</p>

	<p>
		<strong>Organizer:</strong>
		<span class="organizer">
			<span class="vcard">
				<span class="fn nickname"><%: Model.HostedBy.Name %></span>
				<span class="tel"> <%: Model.ContactPhone %></span>
			</span>
		</span>
	</p>

	<% Html.RenderPartial("RSVPStatus"); %>

	<p id="whoscoming">
		<strong>Who's Coming:</strong>
		<%if (Model.RSVPs.Count == 0){%>
				No one has registered.
		<% } %>
	</p>
	
	<%if(Model.RSVPs.Count > 0) {%>
		<div id="whoscomingDiv">
			<ul class="attendees">
				<%
				var RSVPs = Model.RSVPs;
				foreach (var RSVP in RSVPs){%>
					<li class="attendee">
							<%: RSVP.Attendee.Name %>
					</li>
				<% } %>
			</ul>
		</div>
	<%} %>

	<% Html.RenderPartial("EditAndDeleteLinks"); %>
</asp:Content>

<asp:Content ID="Content3" ContentPlaceHolderID="HeadArea" runat="server">
</asp:Content>
