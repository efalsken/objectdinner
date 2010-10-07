using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using db4oDinnerMVC.Helpers;

namespace db4oDinnerMVC.Models {
	public class DinnerFormViewModel {

		public Dinner Dinner { get; private set; }
		public SelectList Countries { get; private set; }

		public DinnerFormViewModel(Dinner dinner) {
			Dinner = dinner;
			Countries = new SelectList(PhoneValidator.Countries, Dinner.Country);
		}
	}
}