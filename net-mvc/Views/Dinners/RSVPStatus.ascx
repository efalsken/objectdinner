<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<db4oDinnerMVC.Models.Dinner>" %>

<script src="/Scripts/MicrosoftAjax.js" type="text/javascript"></script>
<script src="/Scripts/MicrosoftMvcAjax.js" type="text/javascript"></script>    

<script type="text/javascript">

    function AnimateRSVPMessage() {
        $("#rsvpmsg").animate({ fontSize: "1.5em" }, 400);
    }

</script>
    
<div id="rsvpmsg">

<% if (Request.IsAuthenticated) { %>

    <% if (Model.IsHostedBy((Nerd)Context.User.Identity)) { %>

        <p>You are the host for this event!</p>

    <% } else if (Model.IsUserRegistered(Context.User.Identity.Name)) { %>        
    
        <p>You are registered for this event!</p>
    
    <% } 
       else
       { %>  
    
        <%: Ajax.ActionLink("RSVP for this event",
                             "Register", "RSVP",
                             new { id = Model.ID },
                             new AjaxOptions { UpdateTargetId = "rsvpmsg", OnSuccess = "AnimateRSVPMessage" })%>         
    <% } %>
    
<% } else { %>

    <strong>RSVP for this event:</strong>
<% } %>
    
</div>    