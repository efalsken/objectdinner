<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<db4oDinnerMVC.Models.DinnerFormViewModel>" %>

<% Html.EnableClientValidation(); %>
<%: Html.ValidationSummary("Please correct the errors and try again.") %>

<% using (Html.BeginForm()) {%>
	<fieldset>
		<legend>Fields</legend>
		<p>
				<label for="Title">Dinner Title:</label>
				<%: Html.EditorFor(m => Model.Dinner.Title) %>
		</p>
		<p>
				<label for="EventDate">Event Date:</label>
				<%: Html.EditorFor(m => m.Dinner.EventDate) %>
		</p>
		<p>
				<label for="Description">Description:</label>
				<%: Html.TextAreaFor(m => Model.Dinner.Description, 6, 35, null) %>
		</p>
		<p>
				<label for="Address">Address:</label>
				<%: Html.EditorFor(m => Model.Dinner.Address)%>
		</p>
		<p>
				<label for="Country">Country:</label>
				<%: Html.DropDownList("Dinner.Country", Model.Countries) %>                
		</p>
		<p>
				<label for="ContactPhone">Contact Info:</label>
				<%: Html.EditorFor(m => Model.Dinner.ContactPhone)%>
		</p>
		<p>
			<input type="submit" value="Save" />
		</p>
	</fieldset>
<% } %>
