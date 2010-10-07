using System;
using System.Linq;
using System.Web.Security;
using Db4objects.Db4o.Web;
using Enumerable = System.Linq.Enumerable;
using Db4objects.Db4o;

namespace db4oDinnerMVC.Models {
	public class Db4oAccountMembershipService : IMembershipService {
		public const int minPasswordLenth = 4;
		IObjectContainer dbContext;

		public Db4oAccountMembershipService(IObjectContainer database) {
			dbContext = database;
		}

		public int MinPasswordLength {
			get {
				return minPasswordLenth;
			}
		}

		public bool ValidateUser(string email, string password) {
			if (String.IsNullOrEmpty(email)) throw new ArgumentException("Value cannot be null or empty.", "userName");
			if (String.IsNullOrEmpty(password)) throw new ArgumentException("Value cannot be null or empty.", "password");

			return Enumerable.Any<Nerd>(dbContext.Query<Nerd>(), nerd => nerd.Email == email && nerd.Password == password);
		}

		public MembershipCreateStatus CreateUser(string realname, string password, string email) {
			if (String.IsNullOrEmpty(realname)) throw new ArgumentException("Value cannot be null or empty.", "realname");
			if (String.IsNullOrEmpty(password)) throw new ArgumentException("Value cannot be null or empty.", "password");
			if (String.IsNullOrEmpty(email)) throw new ArgumentException("Value cannot be null or empty.", "email");

			if(Enumerable.Any<Nerd>(dbContext.Query<Nerd>(), nerd => nerd.Email == email))
				return MembershipCreateStatus.DuplicateEmail;

			Nerd newnerd = new Nerd() {
			                          	Name = realname,
			                          	Password = password,
			                          	Email = email
			                          };
			dbContext.Store(newnerd);
			dbContext.Commit();
			return MembershipCreateStatus.Success;
		}

		public bool ChangePassword(string email, string oldPassword, string newPassword) {
			if (String.IsNullOrEmpty(email)) throw new ArgumentException("Value cannot be null or empty.", "email");
			if (String.IsNullOrEmpty(oldPassword)) throw new ArgumentException("Value cannot be null or empty.", "oldPassword");
			if (String.IsNullOrEmpty(newPassword)) throw new ArgumentException("Value cannot be null or empty.", "newPassword");

			Nerd nerd = dbContext.Query<Nerd>().Single(n => n.Email == email);
			if (nerd.Password != oldPassword)
				return false;
			nerd.Password = newPassword;
			dbContext.Store(nerd);
			dbContext.Commit();
			return true;
		}
	}
}