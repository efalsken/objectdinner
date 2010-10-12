using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Db4objects.Db4o;
using Db4objects.Db4o.Linq;
using Db4objects.Db4o.AutoIncrement;

namespace db4oDinnerMVC.Models {
	public class DinnerRepository {
		IObjectContainer dbContext;

		public DinnerRepository(IObjectContainer db) {
			dbContext = db;
			AutoIncrementSupport.Install(dbContext);
			db.Ext().Configure().ObjectClass(typeof(Dinner)).CascadeOnUpdate(true);
			db.Ext().Configure().ObjectClass(typeof(Dinner)).ObjectField("_RSVPs").CascadeOnUpdate(true);
		}

		public IQueryable<Dinner> Dinners {
			get { return dbContext.AsQueryable<Dinner>(); }
		}

		public IQueryable<RSVP> RSVPs{
			get { return dbContext.AsQueryable<RSVP>(); } 
		}

		public IQueryable<Nerd> Nerds {
			get { return dbContext.AsQueryable<Nerd>(); }
		}

		public IQueryable<Dinner> FindUpcomingDinners(int daysAhead = 14) {
			return from d in Dinners
				   where d.EventDate > DateTime.Now
					  && d.EventDate < DateTime.Now.AddDays(daysAhead)
				   select d;
		}

		public IQueryable<Dinner> FindDinnersByText(string q) {
			return from d in Dinners
				   where d.Description.Contains(q)
				   select d;
		}

		public Dinner GetDinner(int id) {
			var r = from d in Dinners
				    where d.ID == id
				    select d;
			return r.SingleOrDefault();
		}

		public void Delete(Dinner dinner) {
			//Delete the associated RSVPs.
			foreach(RSVP r in dinner.RSVPs)
				dbContext.Delete(r);
			//Delete the dinner.
			dbContext.Delete(dinner);
			dbContext.Commit();
		}

		public void Delete(RSVP rsvp) {
			rsvp.Dinner.RSVPs.Remove(rsvp);
			dbContext.Store(rsvp.Dinner);
			dbContext.Delete(rsvp);
			dbContext.Commit();
		}

		public void Store(Dinner dinner) {
			dbContext.Store(dinner);
			dbContext.Commit();
		}

		public void Store(Nerd nerd) {
			dbContext.Store(nerd);
			dbContext.Commit();
		}

		public void Store(RSVP rsvp) {
			dbContext.Store(rsvp);
			//dbContext.Store(rsvp.Dinner);
			//dbContext.Store(rsvp.Dinner.RSVPs);
			dbContext.Commit();
		}

		public Nerd GetNerdByEmail(string email) {
			return dbContext.Query<Nerd>().SingleOrDefault(n => n.Email == email);
		}

		public Nerd GetNerdById(int id) {
			return dbContext.Query<Nerd>().SingleOrDefault(d => d.ID == id);
		}

		public Dinner GetDinnerById(int id) {
			return dbContext.Query<Dinner>().SingleOrDefault(d => d.ID == id);
		}

		public RSVP GetRSVPById(int id) {
			return dbContext.Query<RSVP>().SingleOrDefault(d => d.ID == id);
		}

		internal IQueryable<Dinner> GetDinnersByNerd(Nerd nerd) {
			return from d in Dinners
				   where d.HostedBy == nerd ||
				         d.RSVPs.Any(r => r.Attendee == nerd)
				   select d;
		}

		internal void SaveChanges() {
			dbContext.Commit();
		}
	}
}