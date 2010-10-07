using System.Web;
using System.Web.Mvc;
using System.Web.UI;
using db4oDinnerMVC.Models;

namespace NerdDinner {
	public partial class _Default : Page {
		public void Page_Load(object sender, System.EventArgs e) {
			HttpContext.Current.RewritePath(Request.ApplicationPath, false);
			IHttpHandler httpHandler = new MvcHttpHandler();
			httpHandler.ProcessRequest(HttpContext.Current);



			var dinner1 = new Dinner() { Title = "A Dinner" };
			var dinner2 = new Dinner() { Title = "A Dinner" };
			// FALSE  dinner1 == dinner2
			// TRUE   dinner1.Equals(dinner2)

		}
	}
}
