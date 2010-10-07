using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using System.Web.Security;
using System.Security.Principal;
using db4oDinnerMVC.Models;
using Db4objects.Db4o.Web;

namespace db4oDinnerMVC {
	// Note: For instructions on enabling IIS6 or IIS7 classic mode, 
	// visit http://go.microsoft.com/?LinkId=9394801

	public class MvcApplication : System.Web.HttpApplication {
		public override void Init() {
			this.PostAuthenticateRequest += MvcApplication_PostAuthenticateRequest;
			base.Init();
		}

		public static void RegisterRoutes(RouteCollection routes) {
			routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

			routes.MapRoute(
					"PrettyDetails",
					"{Id}",
						new { controller = "Dinners", action = "Details" },
						new { Id = @"\d+" }
					);


			routes.MapRoute(
					"UpcomingDinners",
					"Dinners/Page/{page}",
					new { controller = "Dinners", action = "Index" }
			);

			routes.MapRoute(
					"Default",                                              // Route name
					"{controller}/{action}/{id}",                           // URL with parameters
					new { controller = "Home", action = "Index", id = "" }  // Parameter defaults
			);

		}

		protected void Application_Start() {
			AreaRegistration.RegisterAllAreas();

			RegisterRoutes(RouteTable.Routes);
		}

		void MvcApplication_PostAuthenticateRequest(object sender, EventArgs e) {
			HttpCookie authCookie = HttpContext.Current.Request.Cookies[FormsAuthentication.FormsCookieName];
			if (authCookie != null) {
				string encTicket = authCookie.Value;
				if (!String.IsNullOrEmpty(encTicket)) {
					FormsAuthenticationTicket ticket = FormsAuthentication.Decrypt(encTicket);
					DinnerRepository dbContext = new DinnerRepository(Db4oLocalModule.Client);
					Nerd id = dbContext.GetNerdByEmail(ticket.Name);
					GenericPrincipal prin = new GenericPrincipal(id, null);
					HttpContext.Current.User = prin;
				}
			}
		}

	}
}