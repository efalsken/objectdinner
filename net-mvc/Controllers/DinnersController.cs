using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Db4objects.Db4o;
using Db4objects.Db4o.Linq;
using Db4objects.Db4o.Web;
using db4oDinnerMVC.Models;
using db4oDinnerMVC.Helpers;

namespace db4oDinnerMVC.Controllers {
	public class DinnersController : Controller {
		DinnerRepository dinnerRepository;

		//
		// Dependency Injection enabled constructors
		public DinnersController()
			: this(new DinnerRepository(Db4oLocalModule.Client)) {
		}

		public DinnersController(DinnerRepository repository) {
			dinnerRepository = repository;
		}


		// GET: /Dinners/
		//      /Dinners/Page/2
		//      /Dinners?q=term
		public ActionResult Index(string q, int? page) {
			const int pageSize = 25;

			IQueryable<Dinner> dinners = null;

			//Searching?
			if (!string.IsNullOrWhiteSpace(q))
				dinners = dinnerRepository.FindDinnersByText(q).OrderBy(d => d.EventDate);
			else
				dinners = dinnerRepository.FindUpcomingDinners();

			var paginatedDinners = new PaginatedList<Dinner>(dinners, page ?? 0, pageSize);

			return View(paginatedDinners);
		}

		//
		// GET: /Dinners/Details/5
		public ActionResult Details(int? id) {
			if (id == null) {
				return new FileNotFoundResult { Message = "No Dinner found due to invalid dinner id" };
			}

			Dinner dinner = dinnerRepository.GetDinner(id.Value);

			if (dinner == null) {
				return new FileNotFoundResult { Message = "No Dinner found for that id" };
			}

			return View(dinner);
		}

		//
		// GET: /Dinners/Edit/5
		[Authorize]
		public ActionResult Edit(int id) {

			Dinner dinner = dinnerRepository.GetDinner(id);

			if (!dinner.IsHostedBy(User.Identity.Name))
				return View("InvalidOwner");

			return View(dinner);
		}

		//
		// POST: /Dinners/Edit/5
		[HttpPost, Authorize]
		public ActionResult Edit(int id, FormCollection collection) {
			Dinner dinner = null;

			// Validate the view model
			if (ModelState.IsValid) {

				// MVC is stateless, so get the Dinner we are modifying from the DB.
				dinner = dinnerRepository.GetDinner(id);

				// populate my current instance with updated form data
				UpdateModel(dinner);

				dinnerRepository.SaveChanges();
			}

			try {

				return RedirectToAction("Details", new { id = dinner.ID });
			}
			catch {
				return View(dinner);
			}
		}

		//
		// GET: /Dinners/Create
		[HttpGet, Authorize]
		public ActionResult Create() {

			Dinner prototype = new Dinner() {
				EventDate = DateTime.Now.AddDays(7),
				HostedBy = (Nerd)User.Identity
			};

			return View(new DinnerFormViewModel(prototype));
		}

		//
		// POST: /Dinners/Create
		[HttpPost, Authorize]
		public ActionResult Create(Dinner dinner) {

			if (ModelState.IsValid) {
				Nerd nerd = (Nerd)User.Identity;
				dinner.HostedBy = nerd;

				RSVP rsvp = new RSVP();
				rsvp.Attendee = nerd;
				dinner.RSVPs.Add(rsvp);

				dinnerRepository.Store(dinner);

				return RedirectToAction("Details", new { id = dinner.ID });
			}

			return View(new DinnerFormViewModel(dinner));
		}

		//
		// HTTP GET: /Dinners/Delete/1
		[Authorize]
		public ActionResult Delete(int id) {

			Dinner dinner = dinnerRepository.GetDinner(id);

			if (dinner == null)
				return View("NotFound");

			if (!dinner.IsHostedBy(User.Identity.Name))
				return View("InvalidOwner");

			return View(dinner);
		}

		// 
		// HTTP POST: /Dinners/Delete/1
		[HttpPost, Authorize]
		public ActionResult Delete(int id, string confirmButton) {

			Dinner dinner = dinnerRepository.GetDinner(id);

			if (dinner == null)
				return View("NotFound");

			if (!dinner.IsHostedBy(User.Identity.Name))
				return View("InvalidOwner");

			dinnerRepository.Delete(dinner);

			return View("Deleted");
		}


		protected override void HandleUnknownAction(string actionName) {
			throw new HttpException(404, "Action not found");
		}

		[Authorize]
		public ActionResult My() {

			Nerd nerd = (Nerd)User.Identity;
			var userDinners = from dinner in dinnerRepository.Dinners
							  where
								(
								(dinner.HostedBy.ID == nerd.ID)
									||
								dinner.RSVPs.Any(r => r.Attendee.ID == nerd.ID)
								)
							  orderby dinner.EventDate
							  select dinner;

			return View(userDinners);
		}
	}
}
