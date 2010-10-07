<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<db4oDinnerMVC.Models.Dinner>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
	Edit:
	<%:Model.Title %>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
	<h2>
		Edit Dinner</h2>
	<% Html.EnableClientValidation(); %>
	<%: Html.ValidationSummary("Please correct the errors and try again.") %>
	<% using (Html.BeginForm()) { %>
	<fieldset>
		<div id="dinnerDiv">
			<%:Html.EditorForModel() %>
			<p>
				<input type="submit" value="Save" />
			</p>
		</div>
	</fieldset>
	<% } %>
</asp:Content>
<asp:Content ID="Content3" ContentPlaceHolderID="HeadArea" runat="server">
</asp:Content>
