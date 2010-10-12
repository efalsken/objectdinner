using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel;
using Db4objects.Db4o.AutoIncrement;

namespace db4oDinnerMVC.Models {
	public class RSVP{
		//A numerical ID is only necessary because it's easier to handle in the URL path.
		[AutoIncrement]
		public int ID { get; private set; }
		public Dinner Dinner { get; set; }
		public db4oDinnerMVC.Models.Nerd Attendee { get; set; }
	}
}