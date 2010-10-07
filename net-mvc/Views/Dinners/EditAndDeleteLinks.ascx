<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<db4oDinnerMVC.Models.Dinner>" %>

<% if (Model.IsHostedBy(Context.User.Identity.Name)) { %>

    <%: Html.ActionLink("Edit Dinner", "Edit", new { id=Model.ID })%>
    |
    <%: Html.ActionLink("Delete Dinner", "Delete", new { id = Model.ID })%>    

<% } %>