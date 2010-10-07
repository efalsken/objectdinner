using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace db4oNetService {
	public class ServiceConfigurationException : ApplicationException {
		public ServiceConfigurationException(string message) : base(message) { }
		public ServiceConfigurationException(string message, Exception innerException) : base(message, innerException) { }
	}
}
