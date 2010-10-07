using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Db4objects.Db4o;
using Db4objects.Db4o.Linq;
using Db4objects.Db4o.Web;

namespace db4oDinnerMVC.Models {
	[Bind(Include = "Title,Description,EventDate,Address,Country,ContactPhone,Latitude,Longitude")]
	[MetadataType(typeof(Dinner_Validation))]
	public class Dinner {
		//A numerical ID is only necessary because it's easier to handle in the URL path.
		[Gamlor.ICOODB.Db4oUtils.AutoIncrement]
		public int ID { get; private set; }
		public string Title { get; set; }
		public DateTime EventDate { get; set; }
		public string Description { get; set; }
		public Nerd HostedBy { get; set; }
		public string ContactPhone { get; set; }
		public string Address { get; set; }
		public string Country { get; set; }
		public string Latitude { get; set; }
		public string Longitude { get; set; }

		protected IList<RSVP> _RSVPs = new List<RSVP>();
		public IList<RSVP> RSVPs {
			get {
				return _RSVPs;
			}
		}

		public IEnumerable<Nerd> Attendees {
			get {
				return from RSVP r in _RSVPs
					   select r.Attendee;
			}
		}

		public bool IsHostedBy(Nerd nerd) {
			return HostedBy == nerd;
		}

		public bool IsHostedBy(int nerdId) {
			return HostedBy.ID == nerdId;
		}

		public bool IsHostedBy(string email) {
			return HostedBy.Email == email;
		}

		public bool IsUserRegistered(string email) {
			return RSVPs.Any(rsvp => rsvp.Attendee.Email == email);
		}
	}

	public class Dinner_Validation {
		[HiddenInput(DisplayValue = false)]
		public int ID { get; set; }

		[Required(ErrorMessage = "Title is required")]
		[StringLength(50, ErrorMessage = "Title may not be longer than 50 characters")]
		public string Title { get; set; }

		[Required(ErrorMessage = "Description is required")]
		[StringLength(265, ErrorMessage = "Description may not be longer than 256 characters")]
		public string Description { get; set; }

		public Nerd HostedBy { get; set; }

		[Required(ErrorMessage = "Address is required")]
		[StringLength(50, ErrorMessage = "Address may not be longer than 50 characters")]
		public string Address { get; set; }

		[Required(ErrorMessage = "Country is required")]
		[StringLength(30, ErrorMessage = "Country may not be longer than 30 characters")]
		[UIHint("CountryDropDown")]
		public string Country { get; set; }

		[Required(ErrorMessage = "Contact phone is required")]
		[StringLength(20, ErrorMessage = "Contact phone may not be longer than 20 characters")]
		public string ContactPhone { get; set; }

		[HiddenInput(DisplayValue = false)]
		public double Latitude { get; set; }

		[HiddenInput(DisplayValue = false)]
		public double Longitude { get; set; }
	}
}