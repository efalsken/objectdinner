using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using db4oDinnerMVC.Models;
using Db4objects.Db4o.Web;

namespace db4oDinnerMVC.Controllers {
	public class HomeController : Controller {
		DinnerRepository dinnerRepository;

		//
		// Dependency Injection enabled constructors
		public HomeController()
			: this(new DinnerRepository(Db4oLocalModule.Client)) {
		}

		public HomeController(DinnerRepository repository) {
			dinnerRepository = repository;
		}

		public ActionResult Index() {

			IQueryable<Dinner> dinners = null;

			if(User.Identity.IsAuthenticated)
				dinners = dinnerRepository.GetDinnersByNerd((Nerd)User.Identity);
			
			return View(dinners);
		}

		public ActionResult About() {
			return View();
		}

	}
}
