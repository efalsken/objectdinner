using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using db4oDinnerMVC.Models;
using Db4objects.Db4o.Web;

namespace db4oDinnerMVC.Controllers {
	public class RSVPController : Controller {
		DinnerRepository dinnerRepository;

		public RSVPController() : this(new DinnerRepository(Db4oLocalModule.Client)) { }

		public RSVPController(DinnerRepository dbContext) {
			dinnerRepository = dbContext;
		}

		[Authorize, HttpPost]
		public ActionResult Register(int id) {

			Dinner dinner = dinnerRepository.GetDinner(id);
			Nerd nerd = dinnerRepository.GetNerdByEmail(User.Identity.Name);

			if (!dinner.IsUserRegistered(User.Identity.Name)) {

				RSVP rsvp = new RSVP() {
					Attendee = nerd,
					Dinner = dinner
				};

				dinner.RSVPs.Add(rsvp);
				dinnerRepository.Store(rsvp);
			}

			return Content("Thanks - we'll see you there!");
		}

	}
}
