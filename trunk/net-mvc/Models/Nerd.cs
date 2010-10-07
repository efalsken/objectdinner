using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Security.Principal;

namespace db4oDinnerMVC.Models {
	[DisplayColumn("Name", "Name")]
	public class Nerd : IIdentity {
		//A numerical ID is only necessary because it's easier to handle in the URL path.
		[Gamlor.ICOODB.Db4oUtils.AutoIncrement]
		public int ID { get; private set; }

		public string Name { get; set; }
		public string Email { get; set; }
		public string Password { get; set; }
		public string UserName { get; set; }


		public string AuthenticationType {
			get { return "Db4oNerd"; }
		}

		public bool IsAuthenticated {
			get { return true; }
		}
	}
}